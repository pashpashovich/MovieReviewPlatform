package by.innowise.moviereview.servlet;

import by.innowise.moviereview.dto.EntityDto;
import by.innowise.moviereview.mapper.GenreMapperImpl;
import by.innowise.moviereview.repository.GenreRepositoryImpl;
import by.innowise.moviereview.service.GenreService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/genres")
public class GenreServlet extends HttpServlet {
    private final GenreService genreService = new GenreService(new GenreRepositoryImpl(), new GenreMapperImpl());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<EntityDto> genres = genreService.findAll();
        req.setAttribute("entities", genres);
        req.setAttribute("entityType", "Жанры");
        req.setAttribute("entityPath", "/admin/genres");
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
