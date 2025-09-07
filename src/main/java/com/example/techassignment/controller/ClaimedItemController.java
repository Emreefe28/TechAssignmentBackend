package com.example.techassignment.controller;

import com.example.techassignment.entity.ClaimedItem;
import com.example.techassignment.service.ClaimedItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/claim")
class ClaimedItemController {

    // Doing a static way of an auth bearer token, since short on time. But can explain in person how I would tackle an
    // Authorization option normally.
    private static final String VALID_TOKEN = "Test";

    @Autowired
    private ClaimedItemService claimedItemService;

    @PostMapping
    public ResponseEntity<Object> claimItem(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            Long lostItemId = Long.valueOf(request.get("lostItemId").toString());
            Integer claimedQuantity = Integer.valueOf(request.get("claimedQuantity").toString());

            ClaimedItem claimedItem = claimedItemService.claimItem(userId, lostItemId, claimedQuantity);
            return ResponseEntity.ok(claimedItem);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error claiming item: " + e.getMessage());
        }
    }

    // Not needed from assignment, but added just in case (No auth because will be unused)
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ClaimedItem>> getClaimsByUser(@PathVariable Long userId) {
        List<ClaimedItem> claims = claimedItemService.findClaimedItemByUserId(userId);
        return ResponseEntity.ok(claims);
    }

    // Not needed from assignment, but added just in case (No auth because will be unused)
    @GetMapping("/item/{lostItemId}")
    public ResponseEntity<List<ClaimedItem>> getClaimsByLostItem(@PathVariable Long lostItemId) {
        List<ClaimedItem> claims = claimedItemService.findClaimedItemByLostItemId(lostItemId);
        return ResponseEntity.ok(claims);
    }

    @GetMapping
    public ResponseEntity<Object> getAllClaims(@RequestHeader("Authorization") String authHeader) {
        // Check Bearer token
        if (!isValidBearerToken(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: Invalid or missing Bearer token");
        }

        List<ClaimedItem> claims = claimedItemService.findAll();
        return ResponseEntity.ok(claims);
    }

    private boolean isValidBearerToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return false;
        }
        return authHeader.equals("Bearer " + VALID_TOKEN);
    }

}
