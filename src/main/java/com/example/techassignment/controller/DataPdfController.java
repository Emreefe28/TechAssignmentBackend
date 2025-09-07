package com.example.techassignment.controller;

import com.example.techassignment.entity.LostItem;
import com.example.techassignment.service.DataPdfReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pdf")
public class DataPdfController {

    // Doing a static way of an auth bearer token, since short on time. But can explain in person how I would tackle an
    // Authorization option normally.
    private static final String VALID_TOKEN = "Test";

    @Autowired
    private DataPdfReaderService dataPdfReaderService;

    @PostMapping()
    public ResponseEntity<Object> readPdf(
            @RequestParam("file") MultipartFile file,
            @RequestHeader("Authorization") String authHeader) {
        try {
            // Check Bearer token
            if (!isValidBearerToken(authHeader)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Error: Invalid or missing Bearer token");
            }

            // Validate PDF file
            if (!isValidPdfFile(file)) {
                return ResponseEntity.badRequest().body("Error: Please upload a valid PDF file");
            }

            // Process PDF and save items (all business logic in service)
            List<LostItem> savedItems = dataPdfReaderService.processLostItemsFromPdf(file);

            // Create response
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Successfully processed PDF");
            response.put("itemsProcessed", savedItems.size());
            response.put("pageCount", dataPdfReaderService.getPageCount(file));
            response.put("items", savedItems);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    private boolean isValidBearerToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return false;
        }
        return authHeader.equals("Bearer " + VALID_TOKEN);
    }

    private boolean isValidPdfFile(MultipartFile file) {
        return file != null &&
                !file.isEmpty() &&
                "application/pdf".equals(file.getContentType()) &&
                dataPdfReaderService.isPdfValid(file);
    }
}