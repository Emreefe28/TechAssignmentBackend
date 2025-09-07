package com.example.techassignment.controller;

import com.example.techassignment.entity.ClaimedItem;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
class UserController {

    // Mock user data
    private static final Map<Long, Map<String, String>> MOCK_USERS = new HashMap<>();

    // Since this is just a mock and since there is already auth on other parts of the code, I decided to not put
    // here the same logic

    static {
        Map<String, String> user1 = new HashMap<>();
        user1.put("name", "Karelse Wilco");
        user1.put("email", "karelse.wilco@email.com");
        MOCK_USERS.put(1001L, user1);

        Map<String, String> user2 = new HashMap<>();
        user2.put("name", "Akhil Dixit");
        user2.put("email", "akhil.dixit@email.com");
        MOCK_USERS.put(1002L, user2);

        Map<String, String> user3 = new HashMap<>();
        user3.put("name", "Emre Efe");
        user3.put("email", "emre.efe@email.com");
        MOCK_USERS.put(1003L, user3);

        Map<String, String> user4 = new HashMap<>();
        user4.put("name", "Bilal El Kassas");
        user4.put("email", "bilal.elkassas@email.com");
        MOCK_USERS.put(1004L, user4);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Long userId) {
        Map<String, String> user = MOCK_USERS.get(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("id", userId);
        response.put("name", user.get("name"));
        response.put("email", user.get("email"));

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllUsers() {
        List<Map<String, Object>> users = new ArrayList<>();

        for (Map.Entry<Long, Map<String, String>> entry : MOCK_USERS.entrySet()) {
            Map<String, Object> user = new HashMap<>();
            user.put("id", entry.getKey());
            user.put("name", entry.getValue().get("name"));
            user.put("email", entry.getValue().get("email"));
            users.add(user);
        }

        return ResponseEntity.ok(users);
    }
}
