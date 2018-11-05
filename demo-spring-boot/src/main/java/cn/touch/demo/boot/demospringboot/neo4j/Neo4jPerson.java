package cn.touch.demo.boot.demospringboot.neo4j;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * Created by chengqiang.han on 2018/11/5.
 */

@NodeEntity(label = "Person")
public class Neo4jPerson {

    //    @Id @GeneratedValue(strategy = InternalIdStrategy.class)
//    @Id @GeneratedValue(strategy = UuidStrategy.class)
    @Id
    @GeneratedValue
    Long id;

    @Indexed(unique = false)
    @Property(name = "name")
    private String name;

    //    @Index(unique = true)
    @Id
    @Property(name = "mobile")
    private long mobile;

//    implements AttributeConverter
//    implements CompositeAttributeConverter
//    @Convert(<implements ? AttributeConverter>.class)
//    private BigDecimal money;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getMobile() {
        return mobile;
    }

    public void setMobile(long mobile) {
        this.mobile = mobile;
    }
}
