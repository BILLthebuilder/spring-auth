package com.unknown.dto;

import javax.validation.constraints.Email;
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
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,32}$",
                message = "Password must have least 8 characters,at least one uppercase letter, one lowercase letter and one number")
        String password,
        @NotEmpty
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,32}$",
                message = "Password must have least 8 characters,at least one uppercase letter, one lowercase letter and one number")
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
