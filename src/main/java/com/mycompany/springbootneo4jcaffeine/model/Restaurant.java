package com.mycompany.springbootneo4jcaffeine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.id.UuidStrategy;

import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = "city")
@ToString(exclude = "city")
@NodeEntity
public class Restaurant {

    @Id
    @GeneratedValue(strategy = UuidStrategy.class)
    private String id;

    private String name;

    @JsonIgnore
    @Relationship(type = "LOCATED_IN")
    private City city;

    @Relationship(type = "HAS")
    private Set<Meal> meals = new HashSet<>();

}
