package com.unknown.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public record LoginUserRequest(
        @NotEmpty(message = "Email cannot be blank")
        @Email(message = "Invalid Email Address")
        String email,
        @NotEmpty
        @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,32}$",
                message = "Password must have least 8 characters," +
                        "at least one uppercase letter, one lowercase letter,one special character(@,*) and one number")
        String password
) {
}
