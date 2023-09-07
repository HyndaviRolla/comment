package com.example.demo.controller.exception;
 

public class Exception extends RuntimeException{
    private static final long serialVersionUID = 1L;

	public Exception(String message) {
        super(message);
    }
}