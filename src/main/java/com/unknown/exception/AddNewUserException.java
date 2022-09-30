package com.unknown.exception;

public class AddNewUserException extends RuntimeException
{
    public AddNewUserException(String message)
    {
        super(message);
    }
}