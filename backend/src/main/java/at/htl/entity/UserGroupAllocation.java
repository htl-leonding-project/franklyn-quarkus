package at.htl.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "F_USER_GROUP_ALLOCATION")
public class UserGroupAllocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UGA_ID")
    Long id;

    @ManyToOne
    @JoinColumn(name = "UGA_USER_ID")
    User users;

    @ManyToOne
    @JoinColumn(name = "UGA_USER_GROUP_ID")
    UserGroup userGroups;

    public UserGroupAllocation() {
    }
}
