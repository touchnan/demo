package cn.touch.demo.boot.demospringboot.neo4j;

import org.neo4j.ogm.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * Created by chengqiang.han on 2018/11/5.
 */
@NodeEntity(label = "Movie")
public class Neo4jMovie {
        //    @Id @GeneratedValue(strategy = InternalIdStrategy.class)
//    @Id @GeneratedValue(strategy = UuidStrategy.class)
        @Id
        @GeneratedValue
        Long id;

        @Index(unique = true)
        @Property(name = "title")
        private String title;

        @Relationship(type="DIRECT_TO", direction = Relationship.INCOMING)
        Neo4jPerson director;

        @Relationship(type="ACTS_IN", direction = Relationship.INCOMING)
        Set<Neo4jPerson> actors;

        @Relationship(type = "RATED", direction = Relationship.INCOMING)
        List<Neo4jRating> ratings;


        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Neo4jPerson getDirector() {
            return director;
        }

        public void setDirector(Neo4jPerson director) {
            this.director = director;
        }

        public Set<Neo4jPerson> getActors() {
            return actors;
        }

        public void setActors(Set<Neo4jPerson> actors) {
            this.actors = actors;
        }

        public List<Neo4jRating> getRatings() {
            return ratings;
        }

        public void setRatings(List<Neo4jRating> ratings) {
            this.ratings = ratings;
        }
}
