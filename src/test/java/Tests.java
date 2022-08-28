import org.bibernate.orm.SessionFactory;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.Assert;
import org.junit.Test;
import testobjects.Products;
import testobjects.TestEntity;

import javax.sql.DataSource;
import java.util.function.Supplier;

public class Tests {
    @Test
    public void testTable(){
        Supplier<DataSource> dataSourceSupplier = getDatasourceSupplier();

        var sessionFactory = new SessionFactory(dataSourceSupplier);
        var session = sessionFactory.createSession();
        var entity = session.find(TestEntity.class, 1);
        Assert.assertEquals(entity.productName, "Orange");
    }

    @Test
    public void testTableWithDefaultNames(){
        Supplier<DataSource> dataSourceSupplier = getDatasourceSupplier();

        var sessionFactory = new SessionFactory(dataSourceSupplier);
        var session = sessionFactory.createSession();
        var entity = session.find(Products.class, 1);
        Assert.assertEquals(entity.name, "Orange");
    }

    @Test
    public void extractingFromCash(){
        Supplier<DataSource> dataSourceSupplier = getDatasourceSupplier();

        var sessionFactory = new SessionFactory(dataSourceSupplier);
        var session = sessionFactory.createSession();
        var entity = session.find(TestEntity.class, 1);
        Assert.assertEquals(entity.productName, "Orange");
        var secondEntity = session.find(TestEntity.class, 1);
        Assert.assertTrue(entity == secondEntity);
    }

    private Supplier<DataSource> getDatasourceSupplier() {

        return () -> {
            var dataSource = new JdbcDataSource();
            dataSource.setURL("jdbc:h2:file:target/db/testdb");
            dataSource.setUser("sa");
            dataSource.setPassword("Abcd1234");
            return dataSource;
        };
    }
}