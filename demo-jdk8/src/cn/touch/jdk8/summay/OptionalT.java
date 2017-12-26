package cn.touch.jdk8.summay;

import java.util.Optional;

/**
 * Created by touchnan on 2015/12/14.
 */
public class OptionalT {
    public static void main(String[] args) {
        Optional<String> o = java.util.Optional.ofNullable(null);
        System.out.println("o.isPresent() = " + o.isPresent());
        System.out.println("o.map(o1 -> sout) = " + o.map(s -> {
            return "Hello," + s;
        }).orElseGet(() -> {
            return "aaa";
        }));

        Optional<String> firstName = Optional.of("Tom");
        System.out.println("First Name is set? " + firstName.isPresent());
        System.out.println("First Name: " + firstName.orElseGet(() -> "[none]"));
        System.out.println(firstName.map(s -> "Hey " + s + "!").orElse("Hey Stranger!"));
        System.out.println();


    }
}
