package at.htl.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "F_USER_GROUP_ALLOCATION")
public class UserGroupAllocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UGA_ID")
    Long id;

    @ManyToOne
    @JoinColumn(name = "U_ID")
    @MapsId("UGA_U_ID")
    @NotNull
    User users;

    @ManyToOne
    @JoinColumn(name = "UG_ID")
    @MapsId("UGA_UG_ID")
    @NotNull
    UserGroup userGroups;

    public UserGroupAllocation() {
    }
}
