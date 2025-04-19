package com.assessmint.be.file_storage;

import com.assessmint.be.global.Auditable;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity(name = "file_storage_file_item")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileItem extends Auditable {
   @Id
   @GeneratedValue(strategy = GenerationType.UUID)
   private UUID id;

   @Column
   private String fileName;

   @Column
   private String contentType;

   @Column
   private Long sizeInBytes;

   public FileItem(String fileName, String contentType, Long sizeInBytes) {
      this.fileName = fileName;
      this.contentType = contentType;
      this.sizeInBytes = sizeInBytes;
   }
}
