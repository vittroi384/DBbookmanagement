package com.libraryManage.Exception;

public class NotExistingException extends RuntimeException {
	public NotExistingException(String message) {
		super(message);
	}
}
