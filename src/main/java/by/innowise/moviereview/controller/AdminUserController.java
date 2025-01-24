package by.innowise.moviereview.controller;

import by.innowise.moviereview.dto.UserDto;
import by.innowise.moviereview.service.AdminUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @GetMapping
    public String getAllUsers(Model model) {
        List<UserDto> users = adminUserService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/users";
    }

    @PostMapping
    public String handleUserActions(@RequestParam("action") String action,
                                    @RequestParam("userId") Long userId) {
        switch (action) {
            case "delete":
                adminUserService.deleteUser(userId);
                break;
            case "block":
                adminUserService.blockUser(userId);
                break;
            case "unblock":
                adminUserService.unblockUser(userId);
                break;
            case "promote":
                adminUserService.promoteToAdmin(userId);
                break;
            default:
                throw new IllegalArgumentException("Unknown action: " + action);
        }
        return "redirect:/admin/users";
    }
}


