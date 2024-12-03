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
<script src="${pageContext.request.contextPath}/js/personForm.js" defer></script>

<div class="container my-5">
    <h1 class="text-center mb-4" id="formTitle">Управление людьми</h1>

    <!-- Таблица людей -->
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
                    <td>${person.role}</td>
                    <td>
                        <!-- Кнопка редактирования -->
                        <button
                                class="btn btn-sm btn-primary mb-1"
                                onclick="editPerson(${person.id}, '${person.fullName}', '${person.role}')">
                            Редактировать
                        </button>
                        <!-- Удаление -->
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

    <!-- Форма добавления/редактирования -->
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
                        <c:forEach var="role" items="${roles}">
                            <option value="${role}">${role}</option>
                        </c:forEach>
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