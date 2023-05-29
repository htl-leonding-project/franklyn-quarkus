package at.htl.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "F_USER")

public class User extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "U_ID")
    public Long id;

    @Column(name = "U_FIRST_NAME")
    public String firstName;

    @Column(name = "U_LAST_NAME")
    public String lastName;

    //@JoinColumn(name = "U_EXAM")
    //public Exam exam;

    @Column(name = "U_IS_ONLINE")
    public boolean isOnline;

    @Column(name = "U_IS_ADMIN")
    public boolean isAdmin;

    @Column(name = "U_LAST_ONLINE")
    public LocalDateTime lastOnline;

    public User(){}

    public User(String firstName, String lastName, boolean isOnline) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.isOnline = isOnline;
    }
}
