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

  @Column(unique = true, nullable = false)
  private String email;

  private String password;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name="user_roles",
          joinColumns=@JoinColumn(name="user_id"))
  List<Role> roles;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public List<Role> getRoles() {
    return roles;
  }

  public void setRoles(List<Role> roles) {
    this.roles = roles;
  }

}
