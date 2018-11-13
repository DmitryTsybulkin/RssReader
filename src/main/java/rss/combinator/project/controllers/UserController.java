package rss.combinator.project.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import rss.combinator.project.dto.UserDTO;
import rss.combinator.project.representation.UserRepresentation;

@RestController
@Api(description = "Manage users")
public class UserController {

    private final UserRepresentation userRepresentation;

    @Autowired
    public UserController(UserRepresentation userRepresentation) {
        this.userRepresentation = userRepresentation;
    }

    @PostMapping(value = "/users/new", produces = "application/json")
    @ApiOperation(value = "Create new user", httpMethod = "POST", produces = "application/json")
    public UserDTO createUser(@RequestBody UserDTO dto) {
        return userRepresentation.createUser(dto);
    }

    @GetMapping("/users/{id}")
    @ApiOperation(value = "Get user by id", httpMethod = "GET", produces = "application/json")
    public UserDTO getUserById(@PathVariable(value = "id") Long id) {
        return userRepresentation.getUserById(id);
    }

    @GetMapping("/users")
    @ApiOperation(value = "Get all users", httpMethod = "GET", produces = "application/json")
    public Page<UserDTO> getAllActiveUsers(Pageable pageable) {
        return userRepresentation.getAllActiveUsers(pageable);
    }

    @DeleteMapping(value = "/users/{id}")
    @ApiOperation(value = "Delete user by id", httpMethod = "DELETE", produces = "application/json")
    public void deactivateUser(@PathVariable(value = "id") Long id) {
        userRepresentation.deactivateUser(id);
    }

}