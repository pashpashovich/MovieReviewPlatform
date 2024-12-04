<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
                    <a class="nav-link active" aria-current="page" href="${pageContext.request.contextPath}/admin/movies">
                        <i class="bi bi-film"></i> Управление фильмами
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/genres">
                        <i class="bi bi-tags"></i> Жанры
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/people">
                        <i class="bi bi-person-stars"></i> Звезды
                    </a>
                </li>
            </ul>
        </div>
    </div>
</nav>
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
                        <button
                                class="btn btn-sm btn-primary mb-1"
                                onclick="editMovie(
                                    ${movie.id},
                                        '${movie.title}',
                                        '${movie.description}',
                                    ${movie.releaseYear},
                                    ${movie.duration},
                                        '${movie.language}',
                                        ['<c:forEach var="genre"
                                                     items="${movie.genres}">${genre},</c:forEach>'.slice(0, -1)],
                                        ['<c:forEach var="actor"
                                                     items="${movie.actors}">${actor},</c:forEach>'.slice(0, -1)],
                                        ['<c:forEach var="director"
                                                     items="${movie.directors}">${director},</c:forEach>'.slice(0, -1)],
                                        ['<c:forEach var="producer"
                                                     items="${movie.producers}">${producer},</c:forEach>'.slice(0, -1)]
                                        )">
                            Редактировать
                        </button>
                        <form method="post" action="${pageContext.request.contextPath}/admin/movies"
                              style="display:inline;">
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
    <div class="card">
        <div class="card-header bg-dark text-white">
            <h2 class="text-center mb-0" id="formTitle">Добавление фильма</h2>
        </div>
        <div class="card-body">
            <form method="post" action="${pageContext.request.contextPath}/admin/movies" enctype="multipart/form-data"
                  id="movieForm" onsubmit="return validateForm()">
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
                        <input type="number" id="releaseYear" name="releaseYear" min="1895"
                               max="<%= java.time.Year.now().getValue() %>" class="form-control" required>
                    </div>
                    <div class="col-md-4 mb-3">
                        <label for="duration" class="form-label">Продолжительность (мин)</label>
                        <input type="number" id="duration" name="duration" min="1" class="form-control" required>
                    </div>
                    <div class="col-md-4 mb-3">
                        <label for="language" class="form-label">Язык</label>
                        <select id="language" name="language" class="form-select" required>
                            <option value="" disabled selected>Выберите язык</option>
                            <option value="Русский">Русский</option>
                            <option value="Английский">Английский</option>
                            <option value="Французский">Французский</option>
                            <option value="Испанский">Испанский</option>
                            <option value="Белорусский">Белорусский</option>
                        </select>
                    </div>
                </div>
                <div class="mb-3">
                    <label for="posterFile" class="form-label">Постер</label>
                    <input type="file" id="posterFile" name="posterFile" class="form-control" accept="image/*">
                </div>
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="genres" class="form-label">Жанры</label>
                        <select name="genres" id="genres" class="form-select" multiple required>
                            <c:forEach var="genre" items="${genres}">
                                <option value="${genre.name}">${genre.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="actors" class="form-label">Актеры</label>
                        <select name="actors" id="actors" class="form-select" multiple required>
                            <c:forEach var="actor" items="${actors}">
                                <option value="${actor.fullName}">${actor.fullName}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="directors" class="form-label">Режиссеры</label>
                        <select name="directors" id="directors" class="form-select" multiple required>
                            <c:forEach var="director" items="${directors}">
                                <option value="${director.fullName}">${director.fullName}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="producers" class="form-label">Продюсеры</label>
                        <select name="producers" id="producers" class="form-select" multiple required>
                            <c:forEach var="producer" items="${producers}">
                                <option value="${producer.fullName}">${producer.fullName}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="text-center">
                    <button type="submit" class="btn btn-success">Сохранить</button>
                    <button type="button" class="btn btn-secondary" onclick="resetForm()">Сброс</button>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>
