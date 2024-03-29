package com.unknown.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public record CreateUserRequest(
        @NotEmpty(message = "firstName cannot be blank")
        String firstName,
        @NotEmpty(message = "lastName cannot be blank")
        String lastName,
        @NotEmpty(message = "email cannot be blank")
        @Email(message = "Invalid Email Address")
        String email,
        @NotEmpty(message = "password cannot be blank")
        @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,32}$",
                message = "Password must have least 8 characters," +
                        "at least one uppercase letter, one lowercase letter,one special character(@,*) and one number")
        String password,
        @NotEmpty(message = "phoneNumber cannot be blank")
        String phoneNumber) {

}
