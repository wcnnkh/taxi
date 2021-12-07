package io.github.wcnnkh.taxi;

import java.util.concurrent.ExecutionException;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.TermQuery;

import io.basc.framework.lucene.DefaultLuceneTemplate;
import io.basc.framework.lucene.LuceneTemplate;
import io.basc.framework.lucene.LuceneWriteException;
import io.basc.framework.lucene.SearchParameters;

public class QueryTest {
	public static void main(String[] args) throws LuceneWriteException, InterruptedException, ExecutionException {
		LuceneTemplate luceneTemplate = new DefaultLuceneTemplate("test");

		TestBean bean1 = new TestBean();
		bean1.setName("a");
		bean1.setValue("sss");

		TestBean bean2 = new TestBean();
		bean2.setName("a");
		bean2.setValue("adsfdsfsf");

		luceneTemplate.saveOrUpdate(new Term("name", "a"), bean1).get();
		luceneTemplate.saveOrUpdate(new Term("name", "a"), bean2).get();

		luceneTemplate
				.search(new SearchParameters(new TermQuery(
						new Term("name", "a")), 10), (document) -> {
					TestBean testBean = new TestBean();
					testBean.setName(document.get("name"));
					testBean.setValue(document.get("value"));
					return testBean;

				}).stream().forEach((b) -> {
					System.out.println(b);
				});
	}
}
