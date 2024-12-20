package com.javajags.blogapp.controller;

import com.javajags.blogapp.exception.ResourceNotFoundException;
import com.javajags.blogapp.payload.ApiResponse;
import com.javajags.blogapp.payload.UserDto;
import com.javajags.blogapp.service.UserServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private UserServiceI userServiceI;

    @PostMapping("/create-user")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        UserDto createUserDto = this.userServiceI.createUser(userDto);
        return new ResponseEntity<>(createUserDto, HttpStatus.CREATED);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto, @PathVariable Integer userId) {
        Optional<UserDto> updatedUser = this.userServiceI.updateUser(userDto, userId);

        return updatedUser.map(userDto1 -> new ResponseEntity<>(userDto1, HttpStatus.OK))
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "ID", userId)
                );
    }

    @GetMapping("/get-all-users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(this.userServiceI.getAllUsers());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable("userId") Integer uid) {
        try {
            this.userServiceI.deleteUser(uid);
            return new ResponseEntity<ApiResponse>(new ApiResponse("User deleted Successfully", true), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage(), false), HttpStatus.NOT_FOUND);
        }
    }

}
