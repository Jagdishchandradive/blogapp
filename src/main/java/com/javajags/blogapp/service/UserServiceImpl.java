package com.javajags.blogapp.service;

import com.javajags.blogapp.entity.User;
import com.javajags.blogapp.payload.UserDto;
import com.javajags.blogapp.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserServiceImpl implements UserServiceI{
    @Autowired
    UserRepo userRepo;


    //Method dtoToUser,userToDto used for mapping between DTO class and Entity class.
    public User dtoToUser(UserDto userDto){
        //user object created manually.
        User user= new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setAbout(userDto.getAbout());
        user.setPassword(userDto.getPassword());
        //Manually mapping the data and returning to user.
        return user;
    }
    //reponse sequense=> Db -> Entity -> DTO -> Service -> Controller
    public UserDto userToDto(User user) {
        UserDto userDto=new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setAbout(user.getAbout());
        userDto.setPassword(user.getPassword());
        return userDto;
    }
    //create user method working:-
    // Convert the incoming UserDto to a User entity
    // Save the User entity into the database
    // Convert the saved User entity back to a UserDto to return as a response
    @Override
    public UserDto createUser(UserDto userDto) {
        User user = dtoToUser(userDto);
        User savedUser=userRepo.save(user);
        return userToDto(savedUser);
    }

    @Override
    public UserDto updateUser(UserDto userDto, Integer userId) {
        return null;
    }

    @Override
    public UserDto getUserById(Integer userId) {
        return null;
    }

    @Override
    public List<UserDto> getAllUsers() {
        return null;
    }

    @Override
    public void deleteUser(Integer userId) {

    }
}
