package at.htl.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@NamedQueries({
        @NamedQuery(
                name = "UserGroup.getStats",
                query = "select new at.htl.entity.dto.SchoolClassDto(sc.id, sc.title, count(sc.title)) " +
                        "from UserGroup sc " +
                        "where sc.year = ?1 " +
                        "group by sc.id, sc.title"
        ),
        @NamedQuery(
                name = "UserGroup.getByTitleAndYear",
                query = "select sc from UserGroup sc where sc.title = ?1 and sc.year = ?2"
        )
})

@Entity
@Table(name = "F_USER_GROUP")
public class UserGroup extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UG_ID")
    public Long id;

    @Column(name = "UG_TITLE")
    public String title;

    @Column(name = "UG_YEAR")
    public String year;

    @Column(name = "UG_STATUS")
    @Enumerated(EnumType.STRING)
    public UserGroupType userGroupType;

//    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
//    @Fetch(value = FetchMode.SUBSELECT)
//    @JoinTable
//    @JsonIgnore
//    public List<Exam> exams;

    public UserGroup() {
    }

    public UserGroup(String title, String year) {
        this.title = title;
        this.year = year;
    }

    @Override
    public String toString() {
        return String.format(this.title + " " + this.year);
    }
}
