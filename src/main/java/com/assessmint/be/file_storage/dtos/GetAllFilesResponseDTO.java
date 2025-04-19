package com.assessmint.be.file_storage.dtos;

import java.util.List;

public record GetAllFilesResponseDTO(
      int length,
      List<FileItemDTO> fileItems
) {
}
