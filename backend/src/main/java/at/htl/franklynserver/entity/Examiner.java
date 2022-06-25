package at.htl.franklynserver.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "F_EXAMINER")
public class Examiner extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ER_ID")
    public Long id;

    @Column(name = "ER_USER_Name")
    public String userName;

    @Column(name = "ER_FIRST_NAME")
    public String firstName;

    @Column(name = "ER_LAST_NAME")
    public String lastName;

    @Column(name = "ER_IS_ADMIN")
    public boolean isAdmin;

    @ManyToMany
    @JoinTable(name = "ER_E_Examiner_exam")
    public List<Exam> exams;

    public Examiner() {
    }


    public Examiner(String userName, String firstName, String lastName, boolean isAdmin) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isAdmin = isAdmin;
    }
}
