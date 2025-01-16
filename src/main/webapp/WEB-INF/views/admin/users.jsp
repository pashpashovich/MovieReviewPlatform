<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${lang}"/>
<fmt:setBundle basename="messages"/>
<!DOCTYPE html>
<html lang="${lang}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><fmt:message key="users.manage"/></title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark mb-4">
    <div class="container">
        <a class="navbar-brand mx-auto" href="${pageContext.request.contextPath}/admin/movies">
            <strong><fmt:message key="nav.title.default"/></strong>
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
                aria-controls="navbarNav" aria-expanded="false" aria-label="<fmt:message key='navbar.toggle' />">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse justify-content-center" id="navbarNav">
            <ul class="navbar-nav">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/movies">
                        <i class="bi bi-film"></i> <fmt:message key="movies.manage"/>
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/genres">
                        <i class="bi bi-tags"></i> <fmt:message key="genres"/>
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/people">
                        <i class="bi bi-person-stars"></i> <fmt:message key="stars"/>
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link active" href="${pageContext.request.contextPath}/admin/users">
                        <i class="bi bi-person-stars"></i> <fmt:message key="users"/>
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/reviews">
                        <i class="bi bi-person-stars"></i> <fmt:message key="reviews"/>
                    </a>
                </li>
            </ul>
            <form method="post" action="${pageContext.request.contextPath}/logout" class="d-flex">
                <button class="btn btn-danger btn-sm" type="submit">
                    <i class="bi bi-box-arrow-right"></i> <fmt:message key="logout"/>
                </button>
            </form>
        </div>
    </div>
    <div class="ml-auto">
        <a href="?lang=en" class="text-white">EN</a> |
        <a href="?lang=ru" class="text-white">RU</a>
    </div>
</nav>
<div class="container my-5">
    <h1 class="text-center mb-4"><fmt:message key="users.manage"/></h1>
    <div class="table-responsive mb-5">
        <table class="table table-bordered table-striped">
            <thead class="table-dark">
            <tr class="text-center">
                <th><fmt:message key="id"/></th>
                <th><fmt:message key="username"/></th>
                <th><fmt:message key="email"/></th>
                <th><fmt:message key="role"/></th>
                <th><fmt:message key="status"/></th>
                <th><fmt:message key="actions"/></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="user" items="${users}">
                <tr class="text-center align-middle">
                    <td>${user.id}</td>
                    <td>${user.username}</td>
                    <td>${user.email}</td>
                    <td>${user.role}</td>
                    <td>
                        <c:choose>
                            <c:when test="${user.isBlocked}">
                                <fmt:message key="status.blocked"/>
                            </c:when>
                            <c:otherwise>
                                <fmt:message key="status.active"/>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <form method="post" style="display:inline;">
                            <input type="hidden" name="userId" value="${user.id}">
                            <input type="hidden" name="action" value="block">
                            <button class="btn btn-warning btn-sm" ${user.isBlocked ? "disabled" : ""}><fmt:message
                                    key="block"/></button>
                        </form>
                        <form method="post" style="display:inline;">
                            <input type="hidden" name="userId" value="${user.id}">
                            <input type="hidden" name="action" value="unblock">
                            <button class="btn btn-success btn-sm" ${!user.isBlocked ? "disabled" : ""}><fmt:message
                                    key="unblock"/></button>
                        </form>
                        <form method="post" style="display:inline;">
                            <input type="hidden" name="userId" value="${user.id}">
                            <input type="hidden" name="action" value="promote">
                            <button class="btn btn-primary btn-sm"><fmt:message key="promote"/></button>
                        </form>
                        <form method="post" style="display:inline;">
                            <input type="hidden" name="userId" value="${user.id}">
                            <input type="hidden" name="action" value="delete">
                            <button class="btn btn-danger btn-sm"><fmt:message key="delete"/></button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
