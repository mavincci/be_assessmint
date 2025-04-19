package com.assessmint.be.file_storage.dtos;

import com.assessmint.be.file_storage.FileItem;

import java.util.Date;
import java.util.UUID;

public record FileItemDTO(
      UUID id,
      String fileName,
      String contentType,
      long sizeInBytes,
      Date createdAt
) {
   public static FileItemDTO fromEntity(FileItem fileItem) {
      return new FileItemDTO(
            fileItem.getId(),
            fileItem.getFileName(),
            fileItem.getContentType(),
            fileItem.getSizeInBytes(),
            fileItem.getCreatedOn()
      );
   }
}
