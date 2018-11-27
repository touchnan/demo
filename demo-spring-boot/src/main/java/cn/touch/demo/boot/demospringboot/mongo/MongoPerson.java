package cn.touch.demo.boot.demospringboot.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by chengqiang.han on 2018/10/29.
 */
@Document(collection = "person")
//@TypeAlias("")  //对象间引用时可以用别名来代替类名
public class MongoPerson {
    @Id
    private String id;

    @Field("name")
    @Indexed
    private String name;
}
