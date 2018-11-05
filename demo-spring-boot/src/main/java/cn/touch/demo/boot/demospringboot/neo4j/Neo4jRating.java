package cn.touch.demo.boot.demospringboot.neo4j;

import org.neo4j.ogm.annotation.*;
import org.neo4j.ogm.annotation.typeconversion.DateLong;

import java.util.Date;

/**
 * Created by chengqiang.han on 2018/11/5.
 */
@RelationshipEntity(type = "RATED")
public class Neo4jRating {

    @Id
    @GeneratedValue
    Long id;

    @Property(name = "star")
    int rating;

    @Property(name = "time")
//    @DateString("yyyy-MM-dd HH:mm:ss")
    @DateLong()
    private Date rateDate;

    @StartNode
    private Neo4jPerson person;

    @EndNode
    private Neo4jMovie movie;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Date getRateDate() {
        return rateDate;
    }

    public void setRateDate(Date rateDate) {
        this.rateDate = rateDate;
    }

    public Neo4jPerson getPerson() {
        return person;
    }

    public void setPerson(Neo4jPerson person) {
        this.person = person;
    }

    public Neo4jMovie getMovie() {
        return movie;
    }

    public void setMovie(Neo4jMovie movie) {
        this.movie = movie;
    }
}
