package com.registrationlogindemo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    /**
     * Some boilerplate code from codeburps.com (see references for link), along with variables like balance and List of options
     * A user can have many options, => ManyToOne relationship
     * A user's email acts as their username
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column
    private BigDecimal balance;

    @OneToMany(mappedBy = "owner")
    private List<Option> options = new ArrayList<>();


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}
    )
    private List<Role> roles = new ArrayList<>();

    public User(String name, String email, String password, BigDecimal balance, List<Role> roles) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.balance = balance;
        this.roles = roles;
    }

    public User(User user) {
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", balance=" + balance +
                //", options=" + options +
                ", roles=" + roles +
                '}';
    }

    //Method to decouple the Users role and the User themselves. this is used for when deleting a user.
    public void removeRole(Role role){
        this.roles.remove(role);
        role.getUsers().remove(this);
    }


}
