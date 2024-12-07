package by.innowise.moviereview.servlet;

import by.innowise.moviereview.dto.UserDto;
import by.innowise.moviereview.mapper.UserMapperImpl;
import by.innowise.moviereview.repository.UserRepositoryImpl;
import by.innowise.moviereview.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/")
public class LoginServlet extends HttpServlet {
    private final UserRepositoryImpl userRepository = new UserRepositoryImpl();
    private UserMapperImpl userMapper = new UserMapperImpl();
    private final UserService userService = new UserService(userRepository, userMapper);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/common/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        try {
            UserDto userDto = userService.authenticate(email, password);

            HttpSession session = req.getSession();
            session.setAttribute("user", userDto);


            switch (userDto.getRole()) {
                case ADMIN:
                    resp.sendRedirect(req.getContextPath() + "/admin/movies");
                    break;
                case USER:
                    resp.sendRedirect(req.getContextPath() + "/user/movies");
                    break;
                default:
                    resp.sendRedirect(req.getContextPath() + "/");
            }
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/common/login.jsp").forward(req, resp);
        }
    }
}
