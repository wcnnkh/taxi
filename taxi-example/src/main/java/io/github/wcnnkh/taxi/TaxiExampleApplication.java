package io.github.wcnnkh.taxi;

import io.basc.framework.boot.support.MainApplication;
import io.basc.framework.context.annotation.Bean;
import io.basc.framework.db.DB;
import io.basc.framework.env.Sys;
import io.basc.framework.sqlite.SQLiteDB;
import io.basc.framework.web.resource.StaticResourceRegistry;

public class TaxiExampleApplication {
	public static void main(String[] args) {
		MainApplication.run(TaxiExampleApplication.class, args);
	}

	@Bean
	public StaticResourceRegistry getStaticResourceRegistry() {
		StaticResourceRegistry registry = new StaticResourceRegistry();
		registry.add("/client/**", "classpath:");
		return registry;
	}

	/**
	 * 使用内嵌的sqlite为示例数据库
	 * 
	 * @return
	 */
	@Bean
	public DB getDB() {
		return new SQLiteDB(Sys.getEnv().getWorkPath() + "/taxi-example.db");
	}
}
