package com.javajags.blogapp.service;

import com.javajags.blogapp.entity.User;
import com.javajags.blogapp.exception.ResourceNotFoundException;
import com.javajags.blogapp.payload.UserDto;
import com.javajags.blogapp.repository.UserRepo;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserServiceI {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    UserRepo userRepo;
    @Autowired
    ModelMapper modelMapper;

    //Method dtoToUser,userToDto used for mapping between DTO class and Entity class.
    public User dtoToUser(UserDto userDto) {
        logger.debug("Mapping UserDto to User Entity: {}", userDto);
        //user object created manually.
        User user = new User();
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
        logger.debug("Mapping User Entity to UserDto: {}", user);
        UserDto userDto = new UserDto();
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
        logger.info("Request received to create a new user: {}", userDto);
        User user = dtoToUser(userDto);
        User savedUser = userRepo.save(user);
        logger.info("User created successfully with ID: {}", savedUser.getId());
        return userToDto(savedUser);
    }

    // Update the user field
    // Save the updatedUser to the Db
    // Convert the updatedUser to Dto
    @Override
    public Optional<UserDto> updateUser(UserDto userDto, Integer userId) {
        logger.info("Request received to update user with ID: {}", userId);
        return this.userRepo.findById(userId).map(user -> {
            logger.debug("Updating user details for ID: {}", userId);
            user.setName(userDto.getName());
            user.setEmail(userDto.getEmail());
            user.setPassword(userDto.getPassword());
            user.setAbout(userDto.getAbout());
            User updatedUser = this.userRepo.save(user);
            logger.info("User updated successfully for ID: {}", userId);
            return this.modelMapper.map(updatedUser, UserDto.class);
        });
    }

    @Override
    public UserDto getUserById(Integer userId) {
        logger.info("Fetching user with ID: {}", userId);
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> {
                    logger.error("User not found with ID: {}", userId);
                    return new ResourceNotFoundException("User", " Id ", userId);
                });
        logger.info("User fetched successfully with ID: {}", userId);
        return this.userToDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        logger.info("Fetching all users from the database");
        List<User> users = this.userRepo.findAll();
        List<UserDto> userDtos = users.stream().map(this::userToDto).collect(Collectors.toList());
        logger.info("Total users fetched: {}", userDtos.size());
        return userDtos;
    }

    @Override
    public void deleteUser(Integer userId) {
        logger.info("Request received to delete user with ID: {}", userId);
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> {
                    logger.error("User not found with ID: {}", userId);
                    return new ResourceNotFoundException("User", "Id", userId);
                });
        this.userRepo.delete(user);
        logger.info("User deleted successfully with ID: {}", userId);
    }
}
