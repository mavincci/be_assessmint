package com.assessmint.be.auth.entities.helpers;

import com.assessmint.be.global.exceptions.EnumIllegalArgumentException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum AuthRole {
   ADMIN("ADMIN"),
   USER("USER");

   @JsonValue
   public final String value;

   AuthRole(String value) {
      this.value = value;
   }

   @JsonCreator
   public static AuthRole fromValue(String value) {
      for (var role : values())
         if (role.value.equals(value))
            return role;

      throw new EnumIllegalArgumentException(
            "AUTH_ROLE",
            value,
            Arrays.toString(AuthRole.values())
      );
   }
}
