package at.htl.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "F_USER_GROUP_ALLOCATION")
public class UserGroupAllocation extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UGA_ID")
    Long id;

    @OneToMany
    @JoinColumn(name = "UGA_USER_ID")
    List<User> users;

    @OneToMany
    @JoinColumn(name = "UGA_USER_GROUP_ID")
    List<UserGroup> userGroups;

    public UserGroupAllocation() {
    }
}
