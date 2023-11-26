package at.htl.entity;


import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@NamedQueries({
      /*  @NamedQuery(
                name = "UserGroup.getStats",
                query = "select new at.htl.entity.dto.UserGroupDto(sc.id, sc.title, sc.year) " +
                        "from UserGroup sc " +
                        "where sc.year = ?1 " +
                        "group by sc.id, sc.title"
        ),*/
        @NamedQuery(
                name = "UserGroup.getByTitleAndYear",
                query = "select sc from UserGroup sc where sc.title = ?1 and sc.year = ?2"
        )
})

@Entity
@Table(name = "F_USER_GROUP")
public class UserGroup {
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

    /*@ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinTable(name = "F_USER",
            joinColumns = { @JoinColumn(name = "U_ID") },
            inverseJoinColumns = { @JoinColumn(name = "UG_U_ID") })
    public List<User> users;*/

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
