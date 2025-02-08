package by.innowise.moviereview.controller;

import by.innowise.moviereview.command.UserCommandFactory;
import by.innowise.moviereview.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserCommandFactory userCommandFactory;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userCommandFactory.getAllUsers());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> handleUserAction(@PathVariable Long userId) {
        userCommandFactory.getCommand("delete").execute(userId);
        return ResponseEntity.ok(String.format("User with id %s deleted successfully", userId));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<String> handleUserAction(@PathVariable Long userId,
                                                   @RequestParam String action) {
        var command = userCommandFactory.getCommand(action);
        if (command == null) {
            return ResponseEntity.badRequest().body("Unknown action: " + action);
        }
        command.execute(userId);
        return ResponseEntity.ok("User " + action + " successfully");
    }
}
