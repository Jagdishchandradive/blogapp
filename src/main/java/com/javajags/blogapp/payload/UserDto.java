package com.javajags.blogapp.payload;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {
//{Operational_Flow=> User -> UserDto -> UserServiceI}
    private int id;
    private String name;
    private String email;

    private String password;

    private String about;
    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", about='" + about + '\'' +
                '}';
    }
}
