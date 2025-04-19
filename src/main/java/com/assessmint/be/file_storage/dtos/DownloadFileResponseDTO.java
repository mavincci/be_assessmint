package com.assessmint.be.file_storage.dtos;

import org.springframework.core.io.Resource;

public record DownloadFileResponseDTO(
      String fileName,
      String contentType,
      Resource resource
) {
}
