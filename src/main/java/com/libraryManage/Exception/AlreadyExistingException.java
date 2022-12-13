package com.libraryManage.Exception;

public class AlreadyExistingException extends RuntimeException {
	public AlreadyExistingException(String message) {
		super(message);
	}
}
