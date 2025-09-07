package com.example.techassignment.controller;

import com.example.techassignment.entity.LostItem;
import com.example.techassignment.service.LostItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
class LostItemController {

    @Autowired
    private LostItemService LostItemService;

    @GetMapping("/{itemName}")
    public ResponseEntity<List<LostItem>> findLostItemByItemName(@PathVariable String itemName) {
        List<LostItem> item = LostItemService.findLostItemByItemName(itemName);
        return ResponseEntity.ok(item);
    }
    @GetMapping
    public ResponseEntity<List<LostItem>> findAllLostItem() {
        List<LostItem> items = LostItemService.findAll();
        return ResponseEntity.ok(items);
    }
    @PostMapping
    public ResponseEntity<Object> saveLostItem(@RequestBody LostItem item) {
        try {
            LostItem savedLostItem = LostItemService.saveLostItem(item);
            return ResponseEntity.ok(savedLostItem);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e);
        }
    }
}
