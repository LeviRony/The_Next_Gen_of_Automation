package db;

import java.io.InputStream;
import java.util.Properties;
import java.util.Arrays;

public class DbConfig {
    public final String url, user, pass, schema;
    public final int maxPool;

    private DbConfig(String url, String user, String pass, String schema, int maxPool) {
        this.url = url;
        this.user = user;
        this.pass = pass;
        this.schema = schema;
        this.maxPool = maxPool;
    }

    public static DbConfig load(String envName) {
        String key = normalize(envName);
        String[] candidates = new String[] {
                "db/" + key + ".properties",
                "db-" + key + ".properties"
        };

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) cl = DbConfig.class.getClassLoader();

        Properties p = null;
        String foundAt = null;
        for (String path : candidates) {
            try (InputStream is = cl.getResourceAsStream(path)) {
                if (is != null) {
                    Properties tmp = new Properties();
                    tmp.load(is);
                    p = tmp;
                    foundAt = path;
                    break;
                }
            } catch (Exception e) {
                // try next candidate
            }
        }

        if (p == null) {
            throw new IllegalArgumentException(
                "DB config not found. Tried: " + Arrays.toString(candidates) +
                " (env='" + envName + "'). Ensure one of these files exists on the classpath (e.g., src/main/resources)."
            );
        }

        try {
            String url = resolve(p.getProperty("db.url"));
            String user = resolve(p.getProperty("db.user"));
            String pass = resolve(p.getProperty("db.pass"));
            String schema = p.getProperty("db.schema", "public");
            int max = Integer.parseInt(p.getProperty("db.pool.max", "5"));
            return new DbConfig(url, user, pass, schema, max);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load DB config for " + envName + (foundAt != null ? " from '" + foundAt + "'" : ""), e);
        }
    }

    private static String normalize(String env) {
        if (env == null || env.isBlank()) return "ci";
        return env.trim().toLowerCase()
                .replace('-', ' ')
                .replace('_', ' ')
                .replaceAll("^db\\s+", "")
                .replaceAll("\\s+", "");
    }

    private static String resolve(String v) {
        if (v == null) return null;
        if (!(v.startsWith("${") && v.endsWith("}"))) return v;
        String inner = v.substring(2, v.length() - 1);
        String name;
        String def = null;
        int idx = inner.indexOf(':');
        if (idx >= 0) {
            name = inner.substring(0, idx);
            def = inner.substring(idx + 1);
        } else {
            name = inner;
        }
        String env = System.getenv(name);
        return env != null ? env : def;
    }
}