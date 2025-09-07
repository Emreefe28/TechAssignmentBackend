package com.example.techassignment.repository;

import com.example.techassignment.entity.LostItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LostItemRepository extends CrudRepository<LostItem, Long> {
    List<LostItem> findLostItemByItemName(String itemName);

    LostItem findByItemNameAndPlace(String itemName, String place);

}
