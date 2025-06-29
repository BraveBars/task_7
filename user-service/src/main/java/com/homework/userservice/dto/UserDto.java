package com.homework.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Schema(description = "User data transfer object")
public class UserDto {
    @Schema(description = "Unique id of the user", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    @Size(min=2, max=50, message = "Name must be between 2 and 50 characters")
    @Schema(description = "User's name", example = "Ivan Sidorov", minLength = 2, maxLength = 50)
    private String name;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email cannot be blank")
    @Schema(description = "User's email address", example = "ivan@mail.com")
    private String email;

    @Min(value = 1, message = "Age must be at least 1")
    @Max(value = 150, message = "Age must not exceed 150")
    @Schema(description = "User's age", example = "25", minimum = "1", maximum = "150")
    private Integer age;

    @Schema(description = "Timestamp when the user was created",example = "2025-01-01T12:00:00",
            accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    public UserDto(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}