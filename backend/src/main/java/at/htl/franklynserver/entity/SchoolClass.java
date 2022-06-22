package at.htl.franklynserver.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@NamedQueries({
        @NamedQuery(
                name = "SchoolClass.getAllSchoolClasses",
                query = "select sc from SchoolClass sc order by sc.year,sc.title"
        ),
        @NamedQuery(
                name = "SchoolClass.getStats",
                query = "select new at.htl.franklynserver.entity.SchoolClassDTO(sc.id, sc.title, count(sc.year)) " +
                        "from SchoolClass sc " +
                        "where sc.year = ?1"
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

    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "SC_TITLE")
    public String title;

    @ManyToMany
    @JoinColumn(name = "SC_EXAMS")
    public List<Exam> exams;

    @NotNull
    @Size(min = 4, max = 4)
    @Column(name = "SC_YEAR")
    public String year;

    public SchoolClass() {
    }

    public SchoolClass(String title, String year) {
        this.title = title;
        this.year = year;
    }
}
