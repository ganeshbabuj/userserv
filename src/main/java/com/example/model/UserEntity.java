package com.example.model;

import lombok.Data;
import lombok.ToString;

import java.util.List;

import javax.persistence.*;

@Entity
// SQLServer doesn't allow "user" table as its a keyword.
// Square brackets not required for other table names
@Table(name = "[user]")
@Data
@ToString
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String firstName;

    private String lastName;

    @Column(unique = true, nullable = false)
    private String username;

    private String email;

    private String mobile;

    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"))
    List<Role> roles;

}
