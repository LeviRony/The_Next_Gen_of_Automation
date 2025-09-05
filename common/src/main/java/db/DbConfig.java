package db;

import java.io.InputStream;
import java.util.Properties;

import static configurations.BaseUri.urlDbPracticeTest;

public class DbConfig {
    public final String url, user, pass, schema;
    public final int maxPool;
    private DbConfig(String url, String user, String pass, String schema, int maxPool) {
        this.url=url; this.user=user; this.pass=pass; this.schema=schema; this.maxPool=maxPool;
    }
    public static DbConfig load(String envName) {
        envName = urlDbPracticeTest();
        try (InputStream in = DbConfig.class.getResourceAsStream("/db-" + envName + ".properties")) {
            Properties p = new Properties(); p.load(in);
            String url = resolve(p.getProperty("db.url"));
            String user = resolve(p.getProperty("db.user"));
            String pass = resolve(p.getProperty("db.pass"));
            String schema = p.getProperty("db.schema","public");
            int max = Integer.parseInt(p.getProperty("db.pool.max","5"));
            return new DbConfig(url,user,pass,schema,max);
        } catch (Exception e) { throw new RuntimeException("Failed to load DB config for "+envName, e); }
    }
    private static String resolve(String v){ return v != null && v.startsWith("${") ? System.getenv(v.substring(2, v.length()-1)) : v; }
}