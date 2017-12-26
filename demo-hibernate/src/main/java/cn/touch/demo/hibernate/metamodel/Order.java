/**
 * 
 */
package cn.touch.demo.hibernate.metamodel;

import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on Aug 3, 2017.
 */
@Entity
public class Order {
    @Id
    @GeneratedValue
    Integer id;
    @ManyToOne
    Customer customer;
    @OneToMany
    Set<Item> items;
    BigDecimal totalCost;
    // standard setter/getter methods
}
