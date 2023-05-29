package at.htl.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;

@Entity
@Table(name = "F_USER_GROUP_ALLOCATION")
public class UserGroupAllocation extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UGA_ID")
    Long id;

    @ManyToOne
    @JoinColumn(name = "UGA_USER_ID")
    User user;

    @ManyToOne
    @JoinColumn(name = "UGA_USER_GROUP_ID")
    UserGroup userGroup;
}
