package testobjects;

import org.bibernate.orm.annotation.Column;
import org.bibernate.orm.annotation.Id;
import org.bibernate.orm.annotation.Table;

@Table
public class Products {

    @Id
    public Integer id;

    @Column
    public String name;
}