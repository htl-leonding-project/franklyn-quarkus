package at.htl.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@NamedQueries({
        @NamedQuery(
                name = "SchoolClass.getStats",
                query = "select new at.htl.entity.dto.SchoolClassDto(sc.id, sc.title, count(sc.title)) " +
                        "from SchoolClass sc " +
                        "where sc.year = ?1 " +
                        "group by sc.id, sc.title"
        ),
        @NamedQuery(
                name = "SchoolClass.getByTitleAndYear",
                query = "select sc from SchoolClass sc where sc.title = ?1 and sc.year = ?2"
        )
})

@Entity
@Table(name = "F_SCHOOL_CLASS")
public class SchoolClass extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SC_ID")
    public Long id;

    @Column(name = "SC_TITLE")
    public String title;

    @Column(name = "SC_YEAR")
    public String year;

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinTable
    @JsonIgnore
    public List<Exam> exams;

    public SchoolClass() {
    }

    public SchoolClass(String title, String year) {
        this.title = title;
        this.year = year;
    }

    @Override
    public String toString() {
        return String.format(this.title + " " + this.year);
    }
}
