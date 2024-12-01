<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Управление фильмами</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<h1>Управление фильмами</h1>

<table border="1" style="width: 100%; text-align: center;">
    <thead>
    <tr>
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
        <tr>
            <td>
                <img src="data:image/jpeg;base64,${movie.posterBase64}"
                     alt="Постер ${movie.title}"
                     style="max-width: 100px; max-height: 150px;">
            </td>
            <td>${movie.id}</td>
            <td>${movie.title}</td>
            <td>
                <c:forEach var="genre" items="${movie.genres}">
                    <span>${genre}</span><br>
                </c:forEach>
            </td>
            <td>
                <c:forEach var="actor" items="${movie.actors}">
                    <span>${actor}</span><br>
                </c:forEach>
            </td>
            <td>
                <c:forEach var="director" items="${movie.directors}">
                    <span>${director}</span><br>
                </c:forEach>
            </td>
            <td>
                <c:forEach var="producer" items="${movie.producers}">
                    <span>${producer}</span><br>
                </c:forEach>
            </td>
            <td>${movie.releaseYear}</td>
            <td>${movie.duration}</td>
            <td>${movie.language}</td>
            <td>
                <button onclick="editMovie(${movie.id}, '${movie.title}', '${movie.description}', ${movie.releaseYear}, ${movie.duration}, '${movie.language}')">
                    Редактировать
                </button>
                <form method="post" action="${pageContext.request.contextPath}/admin/movies" style="display:inline;">
                    <input type="hidden" name="id" value="${movie.id}">
                    <input type="hidden" name="_method" value="DELETE">
                    <button type="submit">Удалить</button>
                </form>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<hr>

<h2>Добавить/Редактировать фильм</h2>

<form method="post" action="${pageContext.request.contextPath}/admin/movies" enctype="multipart/form-data" id="movieForm">
    <input type="hidden" id="id" name="id">
    <input type="hidden" name="_method" id="method" value="POST">

    <label>Название: <input type="text" id="title" name="title" required></label><br><br>
    <label>Описание: <textarea id="description" name="description"></textarea></label><br><br>
    <label>Год выпуска: <input type="number" id="releaseYear" name="releaseYear" required></label><br><br>
    <label>Продолжительность: <input type="number" id="duration" name="duration" required></label><br><br>
    <label>Язык: <input type="text" id="language" name="language" required></label><br><br>
    <label>Постер: <input type="file" id="posterFile" name="posterFile" accept="image/*"></label><br><br>

    <label>Жанры:</label>
    <select name="genreIds" multiple>
        <c:forEach var="genre" items="${genres}">
            <option value="${genre.id}">${genre.name}</option>
        </c:forEach>
    </select><br><br>

    <label>Актеры:</label>
    <select name="actorIds" multiple>
        <c:forEach var="actor" items="${actors}">
            <option value="${actor.id}">${actor.fullName}</option>
        </c:forEach>
    </select><br><br>

    <label>Режиссеры:</label>
    <select name="directorIds" multiple>
        <c:forEach var="director" items="${directors}">
            <option value="${director.id}">${director.fullName}</option>
        </c:forEach>
    </select><br><br>

    <label>Продюсеры:</label>
    <select name="producerIds" multiple>
        <c:forEach var="producer" items="${producers}">
            <option value="${producer.id}">${producer.fullName}</option>
        </c:forEach>
    </select><br><br>

    <button type="submit">Сохранить</button>
</form>

<script>
    function editMovie(id, title, description, releaseYear, duration, language) {
        document.getElementById('id').value = id;
        document.getElementById('title').value = title;
        document.getElementById('description').value = description;
        document.getElementById('releaseYear').value = releaseYear;
        document.getElementById('duration').value = duration;
        document.getElementById('language').value = language;

        document.getElementById('method').value = 'PUT';
    }
</script>

</body>
</html>
