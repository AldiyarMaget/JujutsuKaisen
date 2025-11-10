package org.example.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.util.Optional;

import static java.lang.System.*;

public final class DataSourceProvider {
    public static Optional<DataSource> fromEnv() {
        String url = getenv("DB_URL");
        String user = getenv("DB_USER");
        String pass = getenv("DB_PASSWORD");
        if (url == null || user == null || pass == null) return Optional.empty();

        HikariConfig cfg = new HikariConfig();
        cfg.setJdbcUrl(url);
        cfg.setUsername(user);
        cfg.setPassword(pass);
        cfg.setMaximumPoolSize(2);
        cfg.setPoolName("magic-battle-pool");
        return Optional.of(new HikariDataSource(cfg));
    }
}
