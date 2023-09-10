package com.unknown.dto;

import com.unknown.model.User;

import java.util.List;

public record GetUsersResponse(
        Status status,
        List<User>users
) {
}
