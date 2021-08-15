package io.github.wcnnkh.taxi.simple;

import io.github.wcnnkh.taxi.core.dto.NearbyTaxiQuery;
import io.github.wcnnkh.taxi.core.dto.Taxi;
import io.github.wcnnkh.taxi.core.dto.TaxiStatus;
import io.github.wcnnkh.taxi.core.dto.Trace;
import io.github.wcnnkh.taxi.core.dto.TraceLocation;
import io.github.wcnnkh.taxi.core.service.TaxiService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
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

import scw.context.annotation.Provider;
import scw.core.Ordered;
import scw.lucene.DefaultLuceneTemplete;
import scw.lucene.LuceneTemplate;
import scw.lucene.ScoreDocMapper;
import scw.lucene.SearchParameters;
import scw.lucene.SearchResults;
import scw.validation.FastValidator;

@Provider(order = Ordered.LOWEST_PRECEDENCE)
public class LuceneTaxiService implements TaxiService {
	private final LuceneTemplate luceneTemplate = new DefaultLuceneTemplete("taxi");
	private SpatialContext spatialContext = SpatialContext.GEO;
	private SpatialStrategy strategy;

	public LuceneTaxiService() {
		// SpatialPrefixTree也可以通过SpatialPrefixTreeFactory工厂类构建
		SpatialPrefixTree grid = new GeohashPrefixTree(spatialContext, GeohashPrefixTree.getMaxLevelsPossible());
		this.strategy = new RecursivePrefixTreeStrategy(grid, "geoField");
	}

	private void writeDocument(Document document, Trace trace) {
		luceneTemplate.wrap(document, trace);
		luceneTemplate.wrap(document, trace.getLocation());
		Point point = spatialContext.getShapeFactory().pointXY(trace.getLocation().getLongitude(),
				trace.getLocation().getLatitude());
		Field[] fields = strategy.createIndexableFields(point);
		for (Field field : fields) {
			document.add(field);
		}
	}

	@Override
	public void report(Trace trace) {
		trace.getLocation().setTime(System.currentTimeMillis());
		FastValidator.validate(trace);
		Document document = new Document();
		writeDocument(document, trace);
		Term term = new Term("id", trace.getId());
		luceneTemplate.saveOrUpdate(term, document);
	}

	@Override
	public List<Taxi> getNearbyTaxis(NearbyTaxiQuery query) {
		Shape shape = spatialContext.getShapeFactory().circle(query.getLocation().getLongitude(), query.getLocation().getLatitude(), DistanceUtils.dist2Degrees(query.getDistance(),
				DistanceUtils.EARTH_MEAN_RADIUS_KM));
		SpatialArgs args = new SpatialArgs(SpatialOperation.Intersects, shape);
		Query luceneQuery = strategy.makeQuery(args);
		SearchParameters parameters = new SearchParameters(luceneQuery, query.getCount());
		SearchResults<Taxi> results = luceneTemplate.search(parameters, mapper);
		return results.streamAll().filter((taxi) -> {
			//超过60秒未上传心跳就忽略
			if(System.currentTimeMillis() - taxi.getLocation().getTime() > 60000L){
				return false;
			}
			return true;
		}).limit(query.getCount()).collect(Collectors.toList());
	}

	private ScoreDocMapper<Taxi> mapper = new ScoreDocMapper<Taxi>() {

		@Override
		public Taxi map(IndexSearcher indexSearcher,
				ScoreDoc scoreDoc) throws IOException {
			Document document = indexSearcher.doc(scoreDoc.doc);
			Taxi taxi = new Taxi();
			luceneTemplate.mapping(document, taxi);
			taxi.setLocation(luceneTemplate.mapping(document,
					new TraceLocation()));
			taxi.setTaxiStatus(luceneTemplate.mapping(document,
					new TaxiStatus()));
			return taxi;
		}
	};

	@Override
	public Taxi getTaxi(String taxiId) {
		
		TermQuery termQuery = new TermQuery(new Term("id", taxiId));
		return luceneTemplate.search(new SearchParameters(termQuery, 1),
				mapper).first();
	}

	@Override
	public void setStatus(String taxiId, TaxiStatus taxiStatus) {
		Taxi taxi = getTaxi(taxiId);
		if(taxi == null){
			return ;
		}
		
		taxi.setTaxiStatus(taxiStatus);
		Document document = new Document();
		luceneTemplate.mapping(document, taxi);
		taxi.setLocation(luceneTemplate.mapping(document,
				new TraceLocation()));
		taxi.setTaxiStatus(luceneTemplate.mapping(document,
				new TaxiStatus()));
		luceneTemplate.update(new Term("id", taxiId), document);
	}

}
