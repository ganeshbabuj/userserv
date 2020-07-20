package com.example.controller;

import com.example.service.UserService;
import com.example.vo.*;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collections;

@RestController
@RequestMapping("/v1/account")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/users")
    public ResponseEntity create(@RequestBody User user) {
        //TODO: implement post with required permission
        throw new UnsupportedOperationException();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity read(@PathVariable("id") Integer id) {
        //TODO: implement get with required permission
        throw new UnsupportedOperationException();
    }


    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Integer id, @RequestBody User user) {

        //TODO: implement put with required permission
        throw new UnsupportedOperationException();

    }

    @PatchMapping("/users/{id}")
    public ResponseEntity patch(@PathVariable("id") Integer id, @RequestBody JsonNode patchJson) {
        //TODO: implement patch with required permission
        // Refer: http://jsonpatch.com/
        // https://github.com/java-json-tools/json-patch
        throw new UnsupportedOperationException();

    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity delete(@PathVariable("id") Integer id) {
        //TODO: implement delete with required permission
        throw new UnsupportedOperationException();
    }


    @GetMapping(value = "/users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserCollection> search(@RequestParam String username, Principal principal) {

        System.out.println("Search done by: " + principal.getName());
        System.out.println("Looking for: " + username);
        UserCollection userCollection = new UserCollection();
        User user = userService.search(username);
        userCollection.setItems(Collections.singletonList(user));
        return new ResponseEntity<>(userCollection, HttpStatus.OK);

    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }

    @PostMapping("/register")
    public RegistrationResponse register(@RequestBody User user) {
        return userService.register(user);

    }

    @GetMapping("/refresh")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public String refresh(HttpServletRequest req) {
        return userService.refresh(req.getRemoteUser());
    }

}
