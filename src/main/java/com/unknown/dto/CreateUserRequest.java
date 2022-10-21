package com.unknown.dto;

import com.unknown.exception.PasswordsException;
import lombok.SneakyThrows;

import javax.validation.ValidationException;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public record CreateUserRequest(
        @NotEmpty(message = "First Name cannot be blank")
        String firstName,
        @NotEmpty(message = "Last Name cannot be blank")
        String lastName,
        @NotEmpty(message = "Email cannot be blank")
        @Email(message = "Invalid Email Address")
        String email,
        @NotEmpty
        String password,
        @NotEmpty
        String repeatPassword,
        @NotEmpty
        //@Pattern(regexp = "")
        String phoneNumber) {

//    @SneakyThrows
//    public CreateUserRequest {
//      if(!password.equalsIgnoreCase(repeatPassword)){
//         throw new ValidationException("Passwords do not match");
//      }
//    }

}
