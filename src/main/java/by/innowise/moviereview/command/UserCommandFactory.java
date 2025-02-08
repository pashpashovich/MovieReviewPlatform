package by.innowise.moviereview.command;

import by.innowise.moviereview.dto.UserDto;
import by.innowise.moviereview.service.AdminUserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserCommandFactory {

    private final DeleteUserCommand deleteUserCommand;
    private final BlockUserCommand blockUserCommand;
    private final UnblockUserCommand unblockUserCommand;
    private final PromoteUserCommand promoteUserCommand;
    private final AdminUserService adminUserService;

    private Map<String, UserCommand> commands;

    @PostConstruct
    private void init() {
        commands = Map.of(
                "delete", deleteUserCommand,
                "block", blockUserCommand,
                "unblock", unblockUserCommand,
                "promote", promoteUserCommand
        );
    }

    public UserCommand getCommand(String action) {
        return commands.get(action.toLowerCase());
    }

    public List<UserDto> getAllUsers() {
        return adminUserService.getAllUsers();
    }
}
