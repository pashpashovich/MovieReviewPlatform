<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Управление фильмами</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/js/movieForm.js" defer></script>
<div class="container my-5">
    <h1 class="text-center mb-4">Управление фильмами</h1>
    <div class="table-responsive">
        <table class="table table-bordered table-striped">
            <thead class="table-dark">
            <tr class="text-center">
                <th>Постер</th>
                <th>ID</th>
                <th>Название</th>
                <th>Жанры</th>
                <th>Актеры</th>
                <th>Режиссеры</th>
                <th>Продюсеры</th>
                <th>Год выпуска</th>
                <th>Продолжительность</th>
                <th>Язык</th>
                <th>Действия</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="movie" items="${movies}">
                <tr class="text-center align-middle">
                    <td>
                        <img src="data:image/jpeg;base64,${movie.posterBase64}" alt="Постер ${movie.title}"
                             class="img-thumbnail" style="max-width: 100px; max-height: 150px;">
                    </td>
                    <td>${movie.id}</td>
                    <td>${movie.title}</td>
                    <td>
                        <c:forEach var="genre" items="${movie.genres}">
                            <span class="badge bg-primary">${genre}</span>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="actor" items="${movie.actors}">
                            <span class="badge bg-info">${actor}</span>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="director" items="${movie.directors}">
                            <span class="badge bg-warning text-dark">${director}</span>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="producer" items="${movie.producers}">
                            <span class="badge bg-success">${producer}</span>
                        </c:forEach>
                    </td>
                    <td>${movie.releaseYear}</td>
                    <td>${movie.duration} мин</td>
                    <td>${movie.language}</td>
                    <td>
                        <button class="btn btn-sm btn-primary mb-1"
                                onclick="editMovie(${movie.id}, '${movie.title}', '${movie.description}', ${movie.releaseYear}, ${movie.duration}, '${movie.language}')">
                            Редактировать
                        </button>
                        <form method="post" action="${pageContext.request.contextPath}/admin/movies" style="display:inline;">
                            <input type="hidden" name="id" value="${movie.id}">
                            <input type="hidden" name="_method" value="DELETE">
                            <button type="submit" class="btn btn-sm btn-danger">Удалить</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

    <hr class="my-5">

    <!-- Форма добавления/редактирования -->
    <div class="card">
        <div class="card-header bg-dark text-white">
            <h2 class="text-center mb-0">Добавить/Редактировать фильм</h2>
        </div>
        <div class="card-body">
            <form method="post" action="${pageContext.request.contextPath}/admin/movies" enctype="multipart/form-data" id="movieForm">
                <input type="hidden" id="id" name="id">
                <input type="hidden" name="_method" id="method" value="POST">

                <div class="mb-3">
                    <label for="title" class="form-label">Название</label>
                    <input type="text" id="title" name="title" class="form-control" required>
                </div>

                <div class="mb-3">
                    <label for="description" class="form-label">Описание</label>
                    <textarea id="description" name="description" class="form-control" rows="3"></textarea>
                </div>

                <div class="row">
                    <div class="col-md-4 mb-3">
                        <label for="releaseYear" class="form-label">Год выпуска</label>
                        <input type="number" id="releaseYear" name="releaseYear" class="form-control" required>
                    </div>
                    <div class="col-md-4 mb-3">
                        <label for="duration" class="form-label">Продолжительность (мин)</label>
                        <input type="number" id="duration" name="duration" class="form-control" required>
                    </div>
                    <div class="col-md-4 mb-3">
                        <label for="language" class="form-label">Язык</label>
                        <input type="text" id="language" name="language" class="form-control" required>
                    </div>
                </div>

                <div class="mb-3">
                    <label for="posterFile" class="form-label">Постер</label>
                    <input type="file" id="posterFile" name="posterFile" class="form-control" accept="image/*">
                </div>

                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="genres" class="form-label">Жанры</label>
                        <select name="genreIds" id="genres" class="form-select" multiple>
                            <c:forEach var="genre" items="${genres}">
                                <option value="${genre.id}">${genre.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="actors" class="form-label">Актеры</label>
                        <select name="actorIds" id="actors" class="form-select" multiple>
                            <c:forEach var="actor" items="${actors}">
                                <option value="${actor.id}">${actor.fullName}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="directors" class="form-label">Режиссеры</label>
                        <select name="directorIds" id="directors" class="form-select" multiple>
                            <c:forEach var="director" items="${directors}">
                                <option value="${director.id}">${director.fullName}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="producers" class="form-label">Продюсеры</label>
                        <select name="producerIds" id="producers" class="form-select" multiple>
                            <c:forEach var="producer" items="${producers}">
                                <option value="${producer.id}">${producer.fullName}</option>
                            </c:forEach>
                        </select>
                    </div>
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
