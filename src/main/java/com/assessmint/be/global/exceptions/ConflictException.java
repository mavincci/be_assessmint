package com.assessmint.be.global.exceptions;

public class ConflictException extends RuntimeException {
   public ConflictException(String message) {
      super(message);
   }
}
