package com.homework.userservice.service;

import com.homework.userservice.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> findAll();
    UserDto findById(Long id);
    UserDto create(UserDto dto);
    UserDto update(Long id, UserDto dto);
    void delete(Long id);
}
