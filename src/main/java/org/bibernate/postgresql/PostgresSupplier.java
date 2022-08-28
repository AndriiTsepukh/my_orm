package org.bibernate.postgresql;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.util.function.Supplier;

public class PostgresSupplier implements Supplier<DataSource> {

    public PostgresSupplier(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    String url;
    String username;
    String password;

    @Override
    public DataSource get() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL(url);
        dataSource.setUser(username);
        dataSource.setPassword(password);
        return dataSource;
    }
}
