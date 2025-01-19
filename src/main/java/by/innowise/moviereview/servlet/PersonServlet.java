package by.innowise.moviereview.servlet;

import by.innowise.moviereview.entity.Person;
import by.innowise.moviereview.service.PersonService;
import by.innowise.moviereview.util.enums.MovieRole;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/people")
public class PersonServlet extends HttpServlet {
    private final PersonService personService = PersonService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int page = Integer.parseInt(req.getParameter("page") != null ? req.getParameter("page") : "1");
        int size = Integer.parseInt(req.getParameter("size") != null ? req.getParameter("size") : "10");
        String searchQuery = req.getParameter("search");
        String roleFilter = req.getParameter("role");
        List<Person> people = personService.getAllPeople(page, size, searchQuery, roleFilter);
        long totalRecords = personService.countPeople(searchQuery, roleFilter);
        int totalPages = (int) Math.ceil((double) totalRecords / size);
        req.setAttribute("people", people);
        req.setAttribute("roles", MovieRole.values());
        req.setAttribute("currentPage", page);
        req.setAttribute("size", size);
        req.setAttribute("searchQuery", searchQuery);
        req.setAttribute("roleFilter", roleFilter);
        req.setAttribute("totalPages", totalPages);
        req.getRequestDispatcher("/WEB-INF/views/admin/people.jsp").forward(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fullName = req.getParameter("fullName");
        String role = req.getParameter("role");

        Person person = new Person();
        person.setFullName(fullName);
        person.setRole(MovieRole.valueOf(role));

        personService.addPerson(person);

        resp.sendRedirect(req.getContextPath() + "/admin/people");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.parseLong(req.getParameter("id"));
        String fullName = req.getParameter("fullName");
        String role = req.getParameter("role");
        Person person = personService.getPersonById(id);
        if (person != null) {
            person.setFullName(fullName);
            person.setRole(MovieRole.valueOf(role));
            personService.update(person);
        }
        resp.sendRedirect(req.getContextPath() + "/admin/people");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.parseLong(req.getParameter("id"));
        personService.deletePersonById(id);
        resp.sendRedirect(req.getContextPath() + "/admin/people");
    }
}
