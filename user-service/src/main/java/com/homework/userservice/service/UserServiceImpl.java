package com.homework.userservice.service;

import com.homework.userservice.dto.UserDto;
import com.homework.userservice.entity.UserEntity;
import com.homework.userservice.mapper.UserMapper;
import com.homework.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository repo;

    public UserServiceImpl(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<UserDto> findAll() {
        return repo.findAll().stream()
                .map(UserMapper::toDto)
                .toList();
    }

    @Override
    public UserDto findById(Long id) {
        return repo.findById(id)
                .map(UserMapper::toDto)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public UserDto create(UserDto dto) {
        UserEntity e = UserMapper.toEntity(dto);
        return UserMapper.toDto(repo.save(e));
    }

    @Override
    public UserDto update(Long id, UserDto dto) {
        UserEntity e = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        UserMapper.updateEntity(e, dto);
        return UserMapper.toDto(repo.save(e));
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }
}
