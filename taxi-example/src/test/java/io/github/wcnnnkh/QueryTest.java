package io.github.wcnnnkh;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.TermQuery;

import scw.lucene.DefaultLuceneTemplate;
import scw.lucene.LuceneTemplate;
import scw.lucene.SearchParameters;

public class QueryTest {
	public static void main(String[] args) {
		LuceneTemplate luceneTemplate = new DefaultLuceneTemplate("test");

		TestBean bean1 = new TestBean();
		bean1.setName("a");
		bean1.setValue("sss");

		TestBean bean2 = new TestBean();
		bean2.setName("a");
		bean2.setValue("adsfdsfsf");

		luceneTemplate.saveOrUpdate(new Term("name", "a"), bean1);
		luceneTemplate.saveOrUpdate(new Term("name", "a"), bean2);

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
