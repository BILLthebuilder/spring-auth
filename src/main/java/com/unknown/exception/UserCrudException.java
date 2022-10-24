package com.unknown.exception;

public class UserCrudException extends RuntimeException
{
    public UserCrudException(String message)
    {
        super(message);
    }
}