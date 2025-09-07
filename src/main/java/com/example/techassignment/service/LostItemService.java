package com.example.techassignment.service;

import com.example.techassignment.entity.LostItem;
import com.example.techassignment.repository.LostItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class LostItemService {

    @Autowired
    private LostItemRepository lostItemRepository;

    public List<LostItem> findLostItemByItemName(String itemName) {
        return lostItemRepository.findLostItemByItemName(itemName);
    }

    public List<LostItem> findAll(){
        return (List<LostItem>) lostItemRepository.findAll();
    }

    public LostItem saveLostItem(@RequestBody LostItem item) {
        LostItem existingItem = lostItemRepository.findByItemNameAndPlace(
                item.getItemName(),
                item.getPlace()
        );
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
            return lostItemRepository.save(existingItem);
        }
        return lostItemRepository.save(item);
    }

}
