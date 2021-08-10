package io.github.wcnnkh.taxi;

import scw.beans.annotation.Bean;
import scw.boot.support.MainApplication;
import scw.db.DB;
import scw.env.Sys;
import scw.sqlite.SQLiteDB;
import scw.web.support.StaticResourceRegistry;

public class TaxiExampleApplication {
	public static void main(String[] args) {
		MainApplication.run(TaxiExampleApplication.class, args);
	}
	
	@Bean
	public StaticResourceRegistry getStaticResourceRegistry() {
		StaticResourceRegistry registry = new StaticResourceRegistry();
		registry.add("/client/**", "classpath*:/client/");
		return registry;
	}
	
	/**
	 * 使用内嵌的sqlite为示例数据库
	 * 
	 * @return
	 */
	@Bean
	public DB getDB() {
		return new SQLiteDB(Sys.env.getWorkPath()
				+ "/taxi-example.db");
	}
}
