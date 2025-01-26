package by.innowise.moviereview.filter;


import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

@WebFilter("/*")
public class LocaleFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        if (httpRequest.getContentType() != null && httpRequest.getContentType().startsWith("multipart/")) {
            chain.doFilter(request, response);
            return;
        }

        String lang = httpRequest.getParameter("lang");

        if (lang == null) {
            lang = (String) httpRequest.getSession().getAttribute("lang");
        }

        if (lang == null) {
            lang = "ru";
        }

        httpRequest.getSession().setAttribute("lang", lang);
        chain.doFilter(request, response);
    }
}

