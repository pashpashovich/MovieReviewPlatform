package by.innowise.moviereview.servlet;

import by.innowise.moviereview.dto.EntityDto;
import by.innowise.moviereview.service.GenreService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

@WebServlet("/admin/genres")
public class GenreServlet extends HttpServlet {
    private final GenreService genreService = GenreService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String searchQuery = req.getParameter("search");
        String sortField = req.getParameter("sort") != null ? req.getParameter("sort") : "id";
        int page = req.getParameter("page") != null ? Integer.parseInt(req.getParameter("page")) : 1;
        int pageSize = req.getParameter("size") != null ? Integer.parseInt(req.getParameter("size")) : 10;
        Map<String, Object> result = genreService.getGenresWithFilters(searchQuery, sortField, page, pageSize);
        req.setAttribute("entities", result.get("genres"));
        req.setAttribute("totalPages", result.get("totalPages"));
        req.setAttribute("currentPage", result.get("currentPage"));
        req.getRequestDispatcher("/WEB-INF/views/admin/entity-management.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");
        EntityDto entityDto = EntityDto.builder()
                .name(name)
                .build();
        genreService.save(entityDto);
        resp.sendRedirect(req.getContextPath() + "/admin/genres");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = Long.parseLong(req.getParameter("id"));
        String name = req.getParameter("name");
        genreService.update(new EntityDto(id, name));
        resp.sendRedirect(req.getContextPath() + "/admin/genres");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = Long.parseLong(req.getParameter("id"));
        genreService.delete(id);
        resp.sendRedirect(req.getContextPath() + "/admin/genres");
    }
}
