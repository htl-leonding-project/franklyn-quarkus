package at.htl.franklynserver.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NamedQueries({
        @NamedQuery(
                name = "SchoolClass.getAllSchoolClasses",
                query = "select sc from SchoolClass sc order by sc.year,sc.title"
        ),
        @NamedQuery(
                name="SchoolClass.getCurrentSchoolClass",
                query = "select sc from SchoolClass sc where sc.year = ?1 order by sc.title"
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
public class SchoolClass extends PanacheEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "E_ID")
//    public Long id;

    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "F_TITLE")
    public String title;

    @NotNull
    @Size(min = 4, max = 4)
    @Column(name = "F_YEAR")
    public String year;

    public SchoolClass() {
    }

    public SchoolClass(String title, String year) {
        this.title = title;
        this.year = year;
    }
}
