package cn.touch.jdk8.summay;

import java.lang.annotation.*;

/**
 * Created by touchnan on 2015/12/14.
 */
public class RepeatingAnnotations {
    @Target( ElementType.TYPE )
    @Retention( RetentionPolicy.RUNTIME )
    public @interface Filters {
        Filter[] value();
    }

    @Target( ElementType.TYPE )
    @Retention( RetentionPolicy.RUNTIME )
    @Repeatable( Filters.class )
    public @interface Filter {
        String value();
    };

    @Filter( "filter1" )
    @Filter( "filter2" )
    public interface Filterable {
    }

    public static void main(String[] args) {
        Filter[] types = Filterable.class.getAnnotationsByType(Filter.class);
        for (Filter type : types) {
            System.out.println(type.value());
        }
    }

}
