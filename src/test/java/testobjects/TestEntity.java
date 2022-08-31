package testobjects;

import org.bibernate.orm.annotation.Column;
import org.bibernate.orm.annotation.Id;
import org.bibernate.orm.annotation.Table;

@Table("products")
public class TestEntity {
    @Id
    public Integer id;

    @Column("name")
    public String productName;
}