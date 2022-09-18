package at.htl.entity;


import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

public class Examinee extends PanacheEntity {
    public String firstName;

    public String lastName;

    public Examinee(){}

    public Examinee(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
