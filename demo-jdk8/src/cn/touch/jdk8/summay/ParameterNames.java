package cn.touch.jdk8.summay;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * Created by touchnan on 2015/12/14.
 */
public class ParameterNames {

    /*-
     * 使用–parameters参数来编译这个类
     *
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.1</version>
            <configuration>
                <compilerArgument>-parameters</compilerArgument>
                <source>1.8</source>
                <target>1.8</target>
            </configuration>
        </plugin>
     *
     */
    public static void main(String[] args) throws Exception {
        Method method = ParameterNames.class.getMethod( "main", String[].class );
        for( final Parameter parameter: method.getParameters() ) {
            System.out.println( "Parameter: " + parameter.getName() );
        }
    }
}
