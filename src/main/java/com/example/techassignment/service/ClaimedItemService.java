package com.example.techassignment.service;

import com.example.techassignment.entity.ClaimedItem;
import com.example.techassignment.entity.LostItem;
import com.example.techassignment.repository.ClaimedItemRepository;
import com.example.techassignment.repository.LostItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClaimedItemService {

    @Autowired
    private ClaimedItemRepository claimedItemRepository;

    @Autowired
    private LostItemRepository lostItemRepository;

    @Transactional
    public ClaimedItem claimItem(Long userId, Long lostItemId, Integer claimedQuantity) {
        // Find the lost item
        LostItem lostItem = lostItemRepository.findById(lostItemId)
                .orElseThrow(() -> new RuntimeException("Lost item not found with ID: " + lostItemId));

        // Check if there's enough quantity available
        Integer totalClaimedQuantity = getTotalClaimedQuantity(lostItemId);
        Integer availableQuantity = lostItem.getQuantity() - totalClaimedQuantity;

        if (claimedQuantity > availableQuantity) {
            throw new RuntimeException("Not enough quantity available. Available: " +
                    availableQuantity + ", Requested: " + claimedQuantity);
        }

        // Create and save claimed item
        ClaimedItem claimedItem = new ClaimedItem();
        claimedItem.setUserId(userId);
        claimedItem.setLostItemId(lostItem);
        claimedItem.setClaimedQuantity(claimedQuantity);

        // Update the lost item quantity (subtract claimed quantity)
        int newAvailableQuantity = availableQuantity - claimedQuantity;
        lostItem.setQuantity(newAvailableQuantity);

        if (newAvailableQuantity == 0) {
            lostItem.setStatus(LostItem.ItemStatus.FULLY_CLAIMED);
        }

        lostItemRepository.save(lostItem);

        return claimedItemRepository.save(claimedItem);
    }
    public Integer getTotalClaimedQuantity(Long lostItemId) {
        List<ClaimedItem> claimedItems = claimedItemRepository.findByLostItemId_Id(lostItemId);
        return claimedItems.stream()
                .mapToInt(ClaimedItem::getClaimedQuantity)
                .sum();
    }
    public List<ClaimedItem> findClaimedItemByUserId(Long userId) {
        return claimedItemRepository.findByUserId(userId);
    }

    public List<ClaimedItem> findClaimedItemByLostItemId(Long lostItemId) {
        return claimedItemRepository.findByLostItemId_Id(lostItemId);
    }

    @Transactional()
    public List<ClaimedItem> findAll() {
        return (List<ClaimedItem>) claimedItemRepository.findAll();
    }
}
