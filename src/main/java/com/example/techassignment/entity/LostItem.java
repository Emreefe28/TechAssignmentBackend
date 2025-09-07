package com.example.techassignment.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "lost_item")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LostItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Item name is required")
    @Column(nullable = false)
    private String itemName;

    @NotNull(message = "Quantity is required")
    @PositiveOrZero(message = "Quantity must be a positive number")
    @Column(nullable = false)
    private Integer quantity;

    @NotBlank(message = "Place is required")
    @Column(nullable = false)
    private String place;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ItemStatus status = ItemStatus.AVAILABLE;

    public enum ItemStatus {
        AVAILABLE, FULLY_CLAIMED, INACTIVE
    }

}
