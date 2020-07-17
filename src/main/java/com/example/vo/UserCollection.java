package com.example.vo;

import lombok.Data;

import java.util.List;

@Data
public class UserCollection {

    int pageNumber;
    int pageSize;

    List<User> items;

}
