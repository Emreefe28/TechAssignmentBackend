package com.example.techassignment.repository;

import com.example.techassignment.entity.ClaimedItem;
import com.example.techassignment.entity.LostItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ClaimedItemRepository extends CrudRepository<ClaimedItem, Long> {
    List<ClaimedItem> findByUserId(Long userId);
    List<ClaimedItem> findByLostItemId_Id(Long lostItemId);
    List<ClaimedItem> findByUserIdAndLostItemId_Id(Long userId, Long lostItemId);

}
