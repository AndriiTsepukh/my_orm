package testobjects;

import org.bibernate.orm.annotation.Column;
import org.bibernate.orm.annotation.Table;

@Table("products")
public class TestEntity {
    @Column("name")
    public String productName;
}