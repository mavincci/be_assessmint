package com.assessmint.be.global.exceptions;

public class NotFoundException extends RuntimeException {
   public NotFoundException(String message) {
      super(message);
   }
}
