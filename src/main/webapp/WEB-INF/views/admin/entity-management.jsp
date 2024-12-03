<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Управление ${entityType}</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<div class="container my-5">
    <h1 class="text-center">Управление ${entityType}</h1>

    <div class="table-responsive my-4">
        <table class="table table-bordered table-striped">
            <thead class="table-dark">
            <tr>
                <th>ID</th>
                <th>Название</th>
                <th>Действия</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="entity" items="${entities}">
                <tr>
                    <td>${entity.id}</td>
                    <td>${entity.name}</td>
                    <td>
                        <button
                                class="btn btn-sm btn-primary"
                                onclick="editEntity(${entity.id}, '${entity.name}')">
                            Редактировать
                        </button>
                        <form method="post" action="${entityPath}" style="display:inline;">
                            <input type="hidden" name="id" value="${entity.id}">
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
            <h2 id="formTitle" class="mb-0">Добавить ${entityType}</h2>
        </div>
        <div class="card-body">
            <form method="post" action="${entityPath}" id="entityForm">
                <input type="hidden" id="id" name="id">
                <input type="hidden" id="method" name="_method" value="POST">
                <div class="mb-3">
                    <label for="name" class="form-label">Название</label>
                    <input type="text" id="name" name="name" class="form-control" required>
                </div>
                <div class="text-center">
                    <button type="submit" class="btn btn-success">Сохранить</button>
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
        document.getElementById('formTitle').textContent = 'Редактировать ${entityType}';
    }
</script>
</body>
</html>