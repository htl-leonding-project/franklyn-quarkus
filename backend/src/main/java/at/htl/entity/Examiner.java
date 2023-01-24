package at.htl.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinTable(name = "ER_E_Examiner_exam")
    @JsonIgnore
    public List<Exam> exams;

    public Examiner() {
    }


    public Examiner(String userName, String firstName, String lastName, boolean isAdmin) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isAdmin = isAdmin;
    }

    @Override
    public String toString() {
        return String.format(this.firstName + " " + this.lastName + " " + this.isAdmin);
    }
}
