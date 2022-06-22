package at.htl.franklynserver.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@NamedQueries({
        @NamedQuery(
                name = "SchoolClass.getStats",
                query = "select new at.htl.franklynserver.entity.SchoolClassDTO(sc.id, sc.title, count(sc.title)) " +
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

    public SchoolClass() {
    }

    public SchoolClass(String title, String year) {
        this.title = title;
        this.year = year;
    }
}
