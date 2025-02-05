package by.innowise.moviereview.controller;

import by.innowise.moviereview.dto.UserDto;
import by.innowise.moviereview.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = adminUserService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    @PostMapping("/{userId}")
    public ResponseEntity<String> handleUserActions(@PathVariable Long userId,
                                                    @RequestParam String action) {
        return switch (action.toLowerCase()) {
            case "delete" -> {
                adminUserService.deleteUser(userId);
                yield ResponseEntity.ok("User deleted successfully");
            }
            case "block" -> {
                adminUserService.blockUser(userId);
                yield ResponseEntity.ok("User blocked successfully");
            }
            case "unblock" -> {
                adminUserService.unblockUser(userId);
                yield ResponseEntity.ok("User unblocked successfully");
            }
            case "promote" -> {
                adminUserService.promoteToAdmin(userId);
                yield ResponseEntity.ok("User promoted to admin successfully");
            }
            default -> ResponseEntity.badRequest().body("Unknown action: " + action);
        };
    }

}


