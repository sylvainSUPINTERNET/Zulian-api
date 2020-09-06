package org.zulian.entities;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="rolesSeq")
    public long id;

    public String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
