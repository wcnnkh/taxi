package io.github.wcnnkh.taxi.simple;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.spatial.SpatialStrategy;
import org.apache.lucene.spatial.prefix.RecursivePrefixTreeStrategy;
import org.apache.lucene.spatial.prefix.tree.GeohashPrefixTree;
import org.apache.lucene.spatial.prefix.tree.SpatialPrefixTree;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import io.github.wcnnkh.taxi.core.dto.Passenger;
import io.github.wcnnkh.taxi.core.dto.Trace;
import io.github.wcnnkh.taxi.core.dto.TraceLocation;
import io.github.wcnnkh.taxi.core.service.PassengerService;
import scw.context.annotation.Provider;
import scw.core.Ordered;
import scw.lucene.DefaultLuceneTemplate;
import scw.lucene.LuceneTemplate;
import scw.lucene.ScoreDocMapper;
import scw.lucene.SearchParameters;
import scw.validation.FastValidator;

@Provider(order = Ordered.LOWEST_PRECEDENCE)
public class LucenePassengerService implements PassengerService{
	private final LuceneTemplate luceneTemplate = new DefaultLuceneTemplate("passenger");
	private SpatialContext spatialContext = SpatialContext.GEO;
	private SpatialStrategy strategy;
	
	public LucenePassengerService(){
		// SpatialPrefixTree也可以通过SpatialPrefixTreeFactory工厂类构建
		SpatialPrefixTree grid = new GeohashPrefixTree(spatialContext, 11);
		this.strategy = new RecursivePrefixTreeStrategy(grid, "geoField");
	}
	
	private void writeDocument(Document document, Trace trace) {
		luceneTemplate.wrap(document, trace);
		luceneTemplate.wrap(document, trace.getLocation());
		Point point = spatialContext.getShapeFactory().pointXY(trace.getLocation().getLongitude(), trace.getLocation().getLatitude());
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
	
	private ScoreDocMapper<Passenger> mapper = new ScoreDocMapper<Passenger>() {

		@Override
		public Passenger map(IndexSearcher indexSearcher,
				ScoreDoc scoreDoc) throws IOException {
			Document document = indexSearcher.doc(scoreDoc.doc);
			Passenger passenger = new Passenger();
			luceneTemplate.mapping(document, passenger);
			passenger.setLocation(luceneTemplate.mapping(document,
					new TraceLocation()));
			return passenger;
		}
	};

	@Override
	public Passenger getPassenger(String passengerId) {
		TermQuery termQuery = new TermQuery(new Term("id", passengerId));
		return luceneTemplate.search(new SearchParameters(termQuery, 1),
				mapper).first();
	}

}
