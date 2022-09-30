package com.unknown.exception;

public class TransactionDeniedException
        extends RuntimeException
{
    public TransactionDeniedException(String message)
    {
        super(message);
    }
}

