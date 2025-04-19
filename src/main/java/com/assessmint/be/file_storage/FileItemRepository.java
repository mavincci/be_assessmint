package com.assessmint.be.file_storage;

import com.assessmint.be.file_storage.FileItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FileItemRepository extends JpaRepository<FileItem, UUID> {
}
