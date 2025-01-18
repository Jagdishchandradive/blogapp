package com.javajags.blogapp.controller;

import com.javajags.blogapp.exception.ResourceNotFoundException;
import com.javajags.blogapp.payload.ApiResponse;
import com.javajags.blogapp.payload.UserDto;
import com.javajags.blogapp.service.UserServiceI;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserServiceI userServiceI;

    @PostMapping("/create-user")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        logger.info("Request received to create user: {}", userDto);
        UserDto createUserDto = this.userServiceI.createUser(userDto);
        logger.info("User created successfully with ID: {}", createUserDto.getId());
        return new ResponseEntity<>(createUserDto, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getSingleUser(@Valid @PathVariable Integer userId) {
        logger.info("Request received to update user with ID: {}", userId);
        return ResponseEntity.ok(this.userServiceI.getUserById(userId));

    }

    @GetMapping("/get-all-users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        logger.info("Request received to fetch all users");
        return ResponseEntity.ok(this.userServiceI.getAllUsers());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto, @PathVariable Integer userId) {
        logger.info("Request received to update user with ID: {}, payload: {}", userId, userDto);
        Optional<UserDto> updatedUser = this.userServiceI.updateUser(userDto, userId);

        return updatedUser.map(userDto1 -> {
            logger.info("User updated successfully for userID: {}, Updated Data: {}", userId, userDto1);
            return new ResponseEntity<>(userDto1, HttpStatus.OK);
        }).orElseThrow(() -> {
            String errorMessage = String.format("User with ID %d not found in the database.", userId);
            logger.error(errorMessage);
            return new ResourceNotFoundException("User", "ID", userId);
        });
    }


    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable("userId") Integer uid) {
        logger.info("Request received to delete user with ID: {}", uid);
        try {
            this.userServiceI.deleteUser(uid);
            logger.info("User deleted successfully with  the ID: {}", uid);
            return new ResponseEntity<ApiResponse>(new ApiResponse("User deleted Successfully", true), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            logger.error("Error deleting user with ID: {}", uid, e);
            return new ResponseEntity<>(new ApiResponse(e.getMessage(), false), HttpStatus.NOT_FOUND);
        }
    }

}
