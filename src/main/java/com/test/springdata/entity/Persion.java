package com.test.springdata.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * @author tian
 */
@Table(name="JPA_PERSION")
@Entity
public class Persion {
    private Integer id;
    private String lastName;
    private String email;
    private Date brith;

    // 自增id
    @GeneratedValue
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBrith() {
        return brith;
    }

    public void setBrith(Date brith) {
        this.brith = brith;
    }

    @Override
    public String toString() {
        return "Persion{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", brith=" + brith +
                '}';
    }
}
