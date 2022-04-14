package at.htl.franklynserver.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "F_FORM")
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
