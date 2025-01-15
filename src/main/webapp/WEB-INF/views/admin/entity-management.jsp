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
    <title><fmt:message key="admin.manage"/> ${entityType}</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body>
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
                        <i class="bi bi-film"></i> <fmt:message key="navbar.admin.movies"/>
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link active" href="${pageContext.request.contextPath}/admin/genres">
                        <i class="bi bi-tags"></i> <fmt:message key="navbar.admin.genres"/>
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/people">
                        <i class="bi bi-person-stars"></i> <fmt:message key="navbar.admin.stars"/>
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/users">
                        <i class="bi bi-people"></i> <fmt:message key="navbar.admin.users"/>
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
        <a href="?lang=en" class="text-white">EN</a> |
        <a href="?lang=ru" class="text-white">RU</a>
    </div>
</nav>
<div class="container my-5">
    <h1 class="text-center"><fmt:message key="admin.manage"/> ${entityType}</h1>
    <div class="table-responsive my-4">
        <table class="table table-bordered table-striped">
            <thead class="table-dark">
            <tr>
                <th><fmt:message key="admin.id"/></th>
                <th><fmt:message key="admin.name"/></th>
                <th><fmt:message key="admin.actions"/></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="entity" items="${entities}">
                <tr>
                    <td>${entity.id}</td>
                    <td>${entity.name}</td>
                    <td>
                        <button class="btn btn-sm btn-primary" onclick="editEntity(${entity.id}, '${entity.name}')">
                            <fmt:message key="admin.edit"/>
                        </button>
                        <form method="post" action="${entityPath}" style="display:inline;">
                            <input type="hidden" name="id" value="${entity.id}">
                            <input type="hidden" name="_method" value="DELETE">
                            <button type="submit" class="btn btn-sm btn-danger">
                                <fmt:message key="admin.delete"/>
                            </button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

    <div class="card">
        <div class="card-header bg-dark text-white">
            <h2 id="formTitle" class="mb-0"><fmt:message key="admin.add"/> ${entityType}</h2>
        </div>
        <div class="card-body">
            <form method="post" action="${entityPath}" id="entityForm">
                <input type="hidden" id="id" name="id">
                <input type="hidden" id="method" name="_method" value="POST">
                <div class="mb-3">
                    <label for="name" class="form-label"><fmt:message key="admin.name"/></label>
                    <input type="text" id="name" name="name" class="form-control" required>
                </div>
                <div class="text-center">
                    <button type="submit" class="btn btn-success"><fmt:message key="admin.save"/></button>
                </div>
            </form>
        </div>
    </div>
</div>
<script>
    function editEntity(id, name) {
        document.getElementById('id').value = id;
        document.getElementById('name').value = name;
        document.getElementById('method').value = 'PUT';
        document.getElementById('formTitle').textContent = '<fmt:message key="admin.edit" /> ${entityType}';
    }
</script>
</body>
</html>
