package com.javajags.blogapp.service;

import com.javajags.blogapp.entity.User;
import com.javajags.blogapp.payload.UserDto;

import java.util.List;

public interface UserServiceI  {

    //create_user ServiceI
    UserDto createUser(UserDto userDto);

    UserDto updateUser(UserDto userDto, Integer userId);

    UserDto getUserById(Integer userId);

    List<UserDto> getAllUsers();

    void deleteUser(Integer userId);

}
