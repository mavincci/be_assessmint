package com.assessmint.be.global.controllers.dtos;

import java.util.List;

public class ListResponseDTO<T> {
   final public int length;
   final public List<T> values;

   public ListResponseDTO(List<T> values) {
      this.values = values;
      this.length = values.size();
   }
}
