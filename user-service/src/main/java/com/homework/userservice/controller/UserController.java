package com.homework.userservice.controller;

import com.homework.userservice.dto.UserDto;
import com.homework.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "User management API")
public class UserController {
    private final UserService svc;

    public UserController(UserService svc) {
        this.svc = svc;
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve a list of all users")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of users")
    public CollectionModel<EntityModel<UserDto>> getAll() {
        List<EntityModel<UserDto>> users = svc.findAll().stream()
                .map(this::toEntityModel)
                .collect(Collectors.toList());

        return CollectionModel.of(users)
                .add(linkTo(methodOn(UserController.class).getAll()).withSelfRel())
                .add(linkTo(methodOn(UserController.class).create(null)).withRel("create"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieve a specific user by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    public EntityModel<UserDto> getOne(
            @Parameter(description = "ID of the user to retrieve")
            @PathVariable Long id) {
        UserDto user = svc.findById(id);
        return toEntityModel(user);
    }

    @PostMapping
    @Operation(summary = "Create a new user", description = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<EntityModel<UserDto>> create(
            @Parameter(description = "User data to create")
            @Valid @RequestBody UserDto dto) {
        UserDto created = svc.create(dto);
        EntityModel<UserDto> userModel = toEntityModel(created);

        return ResponseEntity
                .created(URI.create("/api/users/" + created.getId()))
                .body(userModel);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Update an existing user with new information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    public EntityModel<UserDto> update(
            @Parameter(description = "ID of the user to update")
            @PathVariable Long id,
            @Parameter(description = "Updated user data")
            @Valid @RequestBody UserDto dto) {
        UserDto updated = svc.update(id, dto);
        return toEntityModel(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Delete a user by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID of the user to delete")
            @PathVariable Long id) {
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }

    private EntityModel<UserDto> toEntityModel(UserDto user) {
        return EntityModel.of(user)
                .add(linkTo(methodOn(UserController.class).getOne(user.getId())).withSelfRel())
                .add(linkTo(methodOn(UserController.class).getAll()).withRel("users"))
                .add(linkTo(methodOn(UserController.class).update(user.getId(), null)).withRel("update"))
                .add(linkTo(methodOn(UserController.class).delete(user.getId())).withRel("delete"));
    }
}