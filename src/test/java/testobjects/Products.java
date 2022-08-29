package testobjects;

import org.bibernate.orm.annotation.Column;
import org.bibernate.orm.annotation.Table;

@Table
public class Products {

    @Column
    public String name;
}