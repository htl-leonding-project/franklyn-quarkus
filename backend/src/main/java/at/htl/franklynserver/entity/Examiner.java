package at.htl.franklynserver.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;

import javax.persistence.*;

@Entity
@Table(name = "F_EXAMINER")
public class Examiner extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "E_ID")
    public Long id;

    @Column(name = "E_USER_Name")
    public String userName;

    @Column(name = "E_FIRST_NAME")
    public String firstName;

    @Column(name = "E_LAST_NAME")
    public String lastName;

    @Column(name = "E_IS_ADMIN")
    public boolean isAdmin;

    public Examiner() {
    }


    public Examiner(String userName, String firstName, String lastName, boolean isAdmin) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isAdmin = isAdmin;
    }
}
