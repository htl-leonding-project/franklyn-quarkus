package at.htl.entity;


import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "F_USER")

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "U_ID")
    public Long id;

    @Column(name = "U_FIRST_NAME")
    public String firstName;

    @Column(name = "U_LAST_NAME")
    public String lastName;

    @Column(unique = true, name = "U_EMAIL")
    public String email;

    @Column(name = "U_IS_ONLINE")
    public boolean isOnline;

    @Column(name = "U_IS_ADMIN")
    public boolean isAdmin;

    @Column(name = "U_LAST_ONLINE")
    public LocalDateTime lastOnline;

    public User(){}

    public User(String firstName, String lastName, boolean isOnline) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.isOnline = isOnline;
    }
    public User(String firstName, String lastName, boolean isOnline, LocalDateTime lastOnline) {
        this(firstName,lastName,isOnline);
        this.lastOnline = lastOnline;
    }

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(String firstName, String lastName, String email, boolean isAdmin) {
        this(firstName, lastName);
        this.email = email;
        this.isAdmin = isAdmin;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public LocalDateTime getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(LocalDateTime lastOnline) {
        this.lastOnline = lastOnline;
    }
}
