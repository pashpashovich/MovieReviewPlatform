package by.innowise.moviereview.servlet;

import by.innowise.moviereview.dto.UserDto;
import by.innowise.moviereview.service.AdminUserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/users")
public class AdminUserServlet extends HttpServlet {

    private final AdminUserService adminUserService = AdminUserService.getInstance();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<UserDto> users = adminUserService.getAllUsers();
        req.setAttribute("users", users);
        req.getRequestDispatcher("/WEB-INF/views/admin/users.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getParameter("action");
        Long userId = Long.valueOf(req.getParameter("userId"));

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
        }
        resp.sendRedirect(req.getContextPath() + "/admin/users");
    }
}
