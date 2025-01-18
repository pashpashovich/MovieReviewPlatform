package by.innowise.moviereview.servlet;

import by.innowise.moviereview.entity.Person;
import by.innowise.moviereview.mapper.PersonMapperImpl;
import by.innowise.moviereview.dao.PersonDao;
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
        List<Person> people = personService.getAllPeople();
        req.setAttribute("people", people);
        req.setAttribute("roles", MovieRole.values());
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
