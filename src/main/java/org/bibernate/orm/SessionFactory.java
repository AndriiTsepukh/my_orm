package org.bibernate.orm;

import javax.sql.DataSource;
import java.util.function.Supplier;

public class SessionFactory {
    DataSource dataSource;

    public SessionFactory (Supplier<DataSource> dataSourceSupplier){
        this.dataSource = dataSourceSupplier.get();
    }

    public Session createSession() {
        return new Session(dataSource);
    }
}