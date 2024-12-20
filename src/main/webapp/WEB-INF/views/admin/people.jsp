<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Управление людьми</title>
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
        formTitle.textContent = 'Редактирование человека';
    }
</script>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark mb-4">
    <div class="container">
        <a class="navbar-brand mx-auto" href="${pageContext.request.contextPath}/admin/movies">
            <strong>КиноАдмин</strong>
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
                aria-controls="navbarNav" aria-expanded="false" aria-label="Переключить навигацию">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse justify-content-center" id="navbarNav">
            <ul class="navbar-nav">
                <li class="nav-item">
                    <a class="nav-link" aria-current="page" href="${pageContext.request.contextPath}/admin/movies">
                        <i class="bi bi-film"></i> Управление фильмами
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/genres">
                        <i class="bi bi-tags"></i> Жанры
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link active" href="${pageContext.request.contextPath}/admin/people">
                        <i class="bi bi-person-stars"></i> Звезды
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/users">
                        <i class="bi bi-person-stars"></i> Пользователи
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/reviews">
                        <i class="bi bi-person-stars"></i> Рецензии
                    </a>
                </li>
            </ul>
            <form method="post" action="${pageContext.request.contextPath}/logout" class="d-flex">
                <button class="btn btn-danger btn-sm" type="submit">
                    <i class="bi bi-box-arrow-right"></i> Выйти
                </button>
            </form>
        </div>
    </div>
</nav>
<div class="container my-5">
    <h1 class="text-center mb-4" id="formTitle">Управление звездами</h1>
    <div class="table-responsive mb-5">
        <table class="table table-bordered table-striped">
            <thead class="table-dark">
            <tr class="text-center">
                <th>ID</th>
                <th>Имя</th>
                <th>Роль</th>
                <th>Действия</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="person" items="${people}">
                <tr class="text-center align-middle">
                    <td>${person.id}</td>
                    <td>${person.fullName}</td>
                    <td>
                        <c:choose>
                            <c:when test="${person.role == 'PRODUCER'}">Продюсер</c:when>
                            <c:when test="${person.role == 'DIRECTOR'}">Режиссер</c:when>
                            <c:when test="${person.role == 'ACTOR'}">Актер</c:when>
                            <c:otherwise>${person.role}</c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <button
                                class="btn btn-sm btn-primary mb-1"
                                onclick="editPerson(${person.id}, '${person.fullName}', '${person.role}')">
                            Редактировать
                        </button>
                        <form method="post" action="${pageContext.request.contextPath}/admin/people"
                              style="display:inline;">
                            <input type="hidden" name="id" value="${person.id}">
                            <input type="hidden" name="_method" value="DELETE">
                            <button type="submit" class="btn btn-sm btn-danger">Удалить</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

    <div class="card">
        <div class="card-header bg-dark text-white">
            <h2 class="text-center mb-0">Добавить/Редактировать человека</h2>
        </div>
        <div class="card-body">
            <form method="post" action="${pageContext.request.contextPath}/admin/people" id="personForm">
                <input type="hidden" name="id" id="id">
                <input type="hidden" name="_method" id="method" value="POST">

                <div class="mb-3">
                    <label for="fullName" class="form-label">Имя</label>
                    <input type="text" id="fullName" name="fullName" class="form-control" required>
                </div>

                <div class="mb-3">
                    <label for="role" class="form-label">Роль</label>
                    <select id="role" name="role" class="form-select" required>
                        <option value="PRODUCER">Продюсер</option>
                        <option value="DIRECTOR">Режиссер</option>
                        <option value="ACTOR">Актер</option>
                    </select>
                </div>

                <div class="text-center">
                    <button type="submit" class="btn btn-success">Сохранить</button>
                </div>
            </form>
        </div>
    </div>
</div>

</body>
</html>
