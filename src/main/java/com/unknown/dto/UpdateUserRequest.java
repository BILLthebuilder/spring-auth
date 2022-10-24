package com.unknown.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public record UpdateUserRequest(
        String firstName,
        String lastName,
        @Email(message = "Invalid Email Address")
        String email,
        String phoneNumber
) {
}
