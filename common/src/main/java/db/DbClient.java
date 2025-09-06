package db;

import com.zaxxer.hikari.*;

import java.sql.*;
import java.util.*;

public class DbClient implements AutoCloseable {
    private final HikariDataSource ds;

    public DbClient(DbConfig cfg) {
        HikariConfig hc = new HikariConfig();
        hc.setJdbcUrl(cfg.url);
        hc.setUsername(cfg.user);
        hc.setPassword(cfg.pass);
        hc.setMaximumPoolSize(cfg.maxPool);
        this.ds = new HikariDataSource(hc);
    }

    public int exec(String sql, Object... args) {
        try (Connection c = ds.getConnection(); PreparedStatement ps = bind(c.prepareStatement(sql), args)) {
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> List<T> query(String sql, RowMapper<T> mapper, Object... args) {
        try (Connection c = ds.getConnection(); PreparedStatement ps = bind(c.prepareStatement(sql), args)) {
            try (ResultSet rs = ps.executeQuery()) {
                List<T> out = new ArrayList<>();
                while (rs.next()) out.add(mapper.map(rs));
                return out;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private PreparedStatement bind(PreparedStatement ps, Object... args) throws SQLException {
        for (int i = 0; i < args.length; i++) ps.setObject(i + 1, args[i]);
        return ps;
    }

    @Override
    public void close() {
        ds.close();
    }

    @FunctionalInterface
    public interface RowMapper<T> {
        T map(ResultSet rs) throws SQLException;
    }
}
