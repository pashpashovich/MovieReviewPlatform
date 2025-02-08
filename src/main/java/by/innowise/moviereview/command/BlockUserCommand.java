package by.innowise.moviereview.command;

import by.innowise.moviereview.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BlockUserCommand implements UserCommand {

    private final AdminUserService adminUserService;

    @Override
    public void execute(Long userId) {
        adminUserService.blockUser(userId);
    }
}

