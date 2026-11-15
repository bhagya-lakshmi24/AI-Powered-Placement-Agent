
package com.placementagent.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/resumes")
public class ResumeController {

    private final Path uploadDir = Paths.get("uploads");

    @PostMapping("/upload")
    public ResponseEntity<?> uploadResume(
            @RequestParam("file") MultipartFile file) {

        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Please select a file"));
            }

            if (!"application/pdf".equalsIgnoreCase(
                    file.getContentType())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Only PDF files are allowed"));
            }

            Files.createDirectories(uploadDir);

            String filename = UUID.randomUUID() + ".pdf";
            Path destination = uploadDir.resolve(filename);

            Files.copy(file.getInputStream(), destination,
                    StandardCopyOption.REPLACE_EXISTING);

            return ResponseEntity.ok(Map.of(
                    "message", "Resume uploaded successfully",
                    "filename", filename));

        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Could not upload resume"));
        }
    }
}
