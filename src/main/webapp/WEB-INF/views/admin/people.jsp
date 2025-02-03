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
    <title><fmt:message key="people.title"/></title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function editPerson(id, fullName, role) {
        document.getElementById('id').value = id;
        document.getElementById('fullName').value = fullName;
        document.getElementById('role').value = role;
        document.getElementById('method').value = 'PUT';

        const formTitle = document.getElementById('formTitle');
        formTitle.textContent = '<fmt:message key="form.editPerson" />';
    }
</script>

<fmt:setBundle basename="messages"/>
<fmt:setLocale value="${param.lang != null ? param.lang : 'ru'}"/>

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
                        <i class="bi bi-film"></i> <fmt:message key="navbar.admin.movies"/>
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/genres">
                        <i class="bi bi-tags"></i> <fmt:message key="navbar.admin.genres"/>
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link active" href="${pageContext.request.contextPath}/admin/people">
                        <i class="bi bi-person-stars"></i> <fmt:message key="navbar.admin.stars"/>
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/users">
                        <i class="bi bi-person"></i> <fmt:message key="navbar.admin.users"/>
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/reviews">
                        <i class="bi bi-chat-dots"></i> <fmt:message key="navbar.admin.reviews"/>
                    </a>
                </li>
            </ul>
            <form method="post" action="${pageContext.request.contextPath}/logout" class="d-flex">
                <button class="btn btn-danger btn-sm" type="submit">
                    <i class="bi bi-box-arrow-right"></i> <fmt:message key="navbar.logout"/>
                </button>
            </form>
        </div>
    </div>
    <div class="ml-auto">
        <a href="?lang=en" class="text-white">EN</a>
        <a href="?lang=ru" class="text-white">RU</a>
        <a href="?lang=by" class="text-white">BY</a>
    </div>
</nav>

<form method="get" class="d-flex mb-3">
    <input type="text" name="search" class="form-control me-2" placeholder="<fmt:message key='search.placeholder' />"
           value="${searchQuery}">
    <select name="role" class="form-select me-2">
        <option value=""><fmt:message key="filter.allRoles"/></option>
        <c:forEach var="role" items="${roles}">
            <option value="${role}" <c:if test="${roleFilter == role}">selected</c:if>>${role}</option>
        </c:forEach>
    </select>
    <button type="submit" class="btn btn-primary"><fmt:message key="search.submit"/></button>
    <a href="${pageContext.request.contextPath}/admin/people" class="btn btn-secondary ms-2">
        <i class="bi bi-x-circle"></i> <fmt:message key="admin.reset"/>
    </a>
</form>
<div class="container my-5">
    <h1 class="text-center mb-4" id="formTitle"><fmt:message key="stars.header"/></h1>
    <div class="table-responsive mb-5">
        <table class="table table-bordered table-striped">
            <thead class="table-dark">
            <tr class="text-center">
                <th><fmt:message key="table.id"/></th>
                <th><fmt:message key="table.name"/></th>
                <th><fmt:message key="table.role"/></th>
                <th><fmt:message key="table.actions"/></th>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${empty people}">
                    <tr>
                        <td colspan="11" class="text-center text-muted">
                            <fmt:message key="table.noData"/>
                        </td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="person" items="${people}">
                        <tr class="text-center align-middle">
                            <td>${person.id}</td>
                            <td>${person.fullName}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${person.role == 'PRODUCER'}"><fmt:message
                                            key="role.producer"/></c:when>
                                    <c:when test="${person.role == 'DIRECTOR'}"><fmt:message
                                            key="role.director"/></c:when>
                                    <c:when test="${person.role == 'ACTOR'}"><fmt:message key="role.actor"/></c:when>
                                    <c:otherwise>${person.role}</c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <button
                                        class="btn btn-sm btn-primary mb-1"
                                        onclick="editPerson(${person.id}, '${person.fullName}', '${person.role}')">
                                    <fmt:message key="button.edit"/>
                                </button>
                                <form method="post" action="${pageContext.request.contextPath}/admin/people"
                                      style="display:inline;">
                                    <input type="hidden" name="id" value="${person.id}">
                                    <input type="hidden" name="_method" value="DELETE">
                                    <button type="submit" class="btn btn-sm btn-danger">
                                        <fmt:message key="button.delete"/>
                                    </button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </div>
    <nav aria-label="Page navigation example">
        <ul class="pagination justify-content-center">
            <c:forEach var="i" begin="1" end="${totalPages}">
                <li class="page-item ${i == currentPage ? 'active' : ''}">
                    <a class="page-link"
                       href="?page=${i}&size=${size}&search=${searchQuery}&role=${roleFilter}">
                            ${i}
                    </a>
                </li>
            </c:forEach>
        </ul>
    </nav>
    <div class="card">
        <div class="card-header bg-dark text-white">
            <h2 class="text-center mb-0"><fmt:message key="form.header"/></h2>
        </div>
        <div class="card-body">
            <form method="post" action="${pageContext.request.contextPath}/admin/people" id="personForm">
                <input type="hidden" name="id" id="id">
                <input type="hidden" name="_method" id="method" value="POST">

                <div class="mb-3">
                    <label for="fullName" class="form-label"><fmt:message key="form.name"/></label>
                    <input type="text" id="fullName" name="fullName" class="form-control" required>
                </div>

                <div class="mb-3">
                    <label for="role" class="form-label"><fmt:message key="form.role"/></label>
                    <select id="role" name="role" class="form-select" required>
                        <option value="PRODUCER"><fmt:message key="role.producer"/></option>
                        <option value="DIRECTOR"><fmt:message key="role.director"/></option>
                        <option value="ACTOR"><fmt:message key="role.actor"/></option>
                    </select>
                </div>
                <div class="text-center">
                    <button type="submit" class="btn btn-success"><fmt:message key="button.save"/></button>
                </div>
            </form>
        </div>
    </div>
</div>

</body>
</html>
