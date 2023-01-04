package com.mgg.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import io.jsonwebtoken.SignatureException;

@ControllerAdvice
public class ExceptionResolver {

	@ExceptionHandler
	public String handleSignatureException(SignatureException e, HttpServletRequest request) {
		return "SignatureException";
	}
}