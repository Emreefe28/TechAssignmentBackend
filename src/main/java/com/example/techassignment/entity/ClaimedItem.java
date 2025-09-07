package com.example.techassignment.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Positive;


import java.time.LocalDateTime;

@Entity
@Table(name = "claimed_item")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ClaimedItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lost_item_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})  // Add this line
    private LostItem lostItemId;

    @NotNull
    @Positive
    @Column(nullable = false)
    private Integer claimedQuantity;

    @Column(nullable = false)
    private LocalDateTime claimDate;

    @PrePersist
    protected void onCreate() {
        claimDate = LocalDateTime.now();
    }

}
