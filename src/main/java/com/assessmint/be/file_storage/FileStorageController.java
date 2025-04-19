package com.assessmint.be.file_storage;

import com.assessmint.be.file_storage.dtos.FileItemDTO;
import com.assessmint.be.file_storage.dtos.GetAllFilesResponseDTO;
import com.assessmint.be.global.controllers.dtos.APIResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/file_storage")
@RequiredArgsConstructor
@Tags(value = {
      @Tag(name = "File Storage", description = "File Storage API")
})
public class FileStorageController {

   private final FileStorageService fileStorageService;

   @PostMapping("/upload")
   public ResponseEntity<APIResponse<FileItemDTO>> uploadFile(
         @RequestParam("file") MultipartFile file) {

      return APIResponse.build(
            HttpStatus.CREATED.value(),
            "File is successfully uploaded",
            fileStorageService.uploadFile(file)
      );
   }

   @GetMapping("/get_file_info/{fileId}")
   public ResponseEntity<APIResponse<FileItemDTO>> getFileInfo(@PathVariable UUID fileId) {
      return APIResponse.build(
            HttpStatus.OK.value(),
            "File info",
            fileStorageService.getFileInfo(fileId)
      );
   }

   @GetMapping("/get_all_files")
   public ResponseEntity<APIResponse<GetAllFilesResponseDTO>> getAllFiles() {
      return APIResponse.build(
            HttpStatus.OK.value(),
            "All files",
            fileStorageService.getAllFiles()
      );
   }

   @GetMapping("/download/{fileId}")
   public ResponseEntity<Resource> downloadFile(@PathVariable UUID fileId) {
      final var resDTO = fileStorageService.downloadFile(fileId);

      final var contentType = "application/octet-stream";
      final var headerValue = "attachment; filename=\"" + resDTO.fileName() + "\"";

      return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
            .body(resDTO.resource());
   }
}
