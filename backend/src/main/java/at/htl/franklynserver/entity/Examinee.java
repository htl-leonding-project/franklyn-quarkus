package at.htl.franklynserver.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;

import javax.persistence.*;

@Entity
@Table(name = "F_EXAMINEE")
public class Examinee extends PanacheEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "E_ID")
    public Long id;

    @Column(name = "E_FIRST_NAME")
    public String firstName;

    @Column(name = "E_LAST_NAME")
    public String lastName;
}
