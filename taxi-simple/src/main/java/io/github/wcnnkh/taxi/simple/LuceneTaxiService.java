package io.github.wcnnkh.taxi.simple;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.spatial.SpatialStrategy;
import org.apache.lucene.spatial.prefix.RecursivePrefixTreeStrategy;
import org.apache.lucene.spatial.prefix.tree.GeohashPrefixTree;
import org.apache.lucene.spatial.prefix.tree.SpatialPrefixTree;
import org.apache.lucene.spatial.query.SpatialArgs;
import org.apache.lucene.spatial.query.SpatialOperation;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.distance.DistanceUtils;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.Shape;

import io.basc.framework.context.annotation.Provider;
import io.basc.framework.convert.ReverseTransformer;
import io.basc.framework.convert.TypeDescriptor;
import io.basc.framework.core.Ordered;
import io.basc.framework.lucene.DefaultLuceneTemplate;
import io.basc.framework.lucene.LuceneException;
import io.basc.framework.lucene.LuceneTemplate;
import io.basc.framework.lucene.SearchParameters;
import io.basc.framework.lucene.SearchResults;
import io.basc.framework.validation.FastValidator;
import io.github.wcnnkh.taxi.core.dto.NearbyTaxiQuery;
import io.github.wcnnkh.taxi.core.dto.Taxi;
import io.github.wcnnkh.taxi.core.dto.TaxiStatus;
import io.github.wcnnkh.taxi.core.dto.Trace;
import io.github.wcnnkh.taxi.core.service.TaxiService;

@Provider(order = Ordered.LOWEST_PRECEDENCE)
public class LuceneTaxiService implements TaxiService {
	private final LuceneTemplate luceneTemplate = new DefaultLuceneTemplate("taxi");
	private SpatialContext spatialContext = SpatialContext.GEO;
	private SpatialStrategy strategy;

	public LuceneTaxiService() {
		// SpatialPrefixTree也可以通过SpatialPrefixTreeFactory工厂类构建
		SpatialPrefixTree grid = new GeohashPrefixTree(spatialContext, GeohashPrefixTree.getMaxLevelsPossible());
		this.strategy = new RecursivePrefixTreeStrategy(grid, "geoField");
		luceneTemplate.getMapper().registerStructure(Trace.class,
				luceneTemplate.getMapper().getStructure(Trace.class).withEntitys().all());
		luceneTemplate.getMapper().registerStructure(Taxi.class,
				luceneTemplate.getMapper().getStructure(Taxi.class).withEntitys().all());
		luceneTemplate.getMapper().registerReverseTransformer(Trace.class,
				new ReverseTransformer<Trace, Document, LuceneException>() {

					@Override
					public void reverseTransform(Trace source, TypeDescriptor sourceType, Document target,
							TypeDescriptor targetType) throws LuceneException {
						luceneTemplate.getMapper().reverseTransform(source,
								luceneTemplate.getMapper().getStructure(Trace.class), target);
						Point point = spatialContext.getShapeFactory().pointXY(source.getLocation().getLongitude(),
								source.getLocation().getLatitude());
						Field[] fields = strategy.createIndexableFields(point);
						for (Field field : fields) {
							target.add(field);
						}
					}
				});
	}

	@Override
	public void report(Trace trace) {
		trace.getLocation().setTime(System.currentTimeMillis());
		FastValidator.validate(trace);
		Document document = new Document();
		luceneTemplate.getMapper().reverseTransform(trace, document);
		Term term = new Term("id", trace.getId());
		luceneTemplate.saveOrUpdate(term, document);
	}

	@Override
	public List<Taxi> getNearbyTaxis(NearbyTaxiQuery query) {
		Shape shape = spatialContext.getShapeFactory().circle(query.getLocation().getLongitude(),
				query.getLocation().getLatitude(),
				DistanceUtils.dist2Degrees(query.getDistance(), DistanceUtils.EARTH_MEAN_RADIUS_KM));
		SpatialArgs args = new SpatialArgs(SpatialOperation.Intersects, shape);
		Query luceneQuery = strategy.makeQuery(args);
		SearchParameters parameters = new SearchParameters(luceneQuery, query.getCount());
		SearchResults<Taxi> results = luceneTemplate.search(parameters, Taxi.class);
		return results.streamAll().filter((taxi) -> {
			// 超过10秒未上传心跳就忽略
			if (System.currentTimeMillis() - taxi.getLocation().getTime() > 10000L) {
				return false;
			}
			return true;
		}).limit(query.getCount()).collect(Collectors.toList());
	}

	@Override
	public Taxi getTaxi(String taxiId) {
		TermQuery termQuery = new TermQuery(new Term("id", taxiId));
		return luceneTemplate.search(new SearchParameters(termQuery, 1), Taxi.class).first();
	}

	@Override
	public void setStatus(String taxiId, TaxiStatus taxiStatus) {
		Taxi taxi = getTaxi(taxiId);
		if (taxi == null) {
			return;
		}

		taxi.setTaxiStatus(taxiStatus);
		Document document = new Document();
		luceneTemplate.getMapper().reverseTransform(taxi, document);
		luceneTemplate.update(new Term("id", taxiId), document);
	}

}
