package com.example.vo;

import java.util.List;

import com.example.model.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class User {

  private Integer id;
  private String firstName;
  private String lastName;
  private String username;
  private String email;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;

  List<Role> roles;

}
