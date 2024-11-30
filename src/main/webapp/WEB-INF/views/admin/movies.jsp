<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Управление фильмами</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<h1>Управление фильмами</h1>

<table border="1">
    <thead>
    <tr>
        <th>ID</th>
        <th>Название</th>
        <th>Год выпуска</th>
        <th>Продолжительность (мин)</th>
        <th>Язык</th>
        <th>Рейтинг</th>
        <th>Действия</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="movie" items="${movies}">
        <tr>
            <td>${movie.id}</td>
            <td>${movie.title}</td>
            <td>${movie.releaseYear}</td>
            <td>${movie.duration}</td>
            <td>${movie.language}</td>
            <td>${movie.avgRating}</td>
            <td>
                <a href="${pageContext.request.contextPath}/movies/${movie.id}">Просмотр</a>
                <a href="${pageContext.request.contextPath}/movies/edit?id=${movie.id}">Редактировать</a>
                <form method="post" action="${pageContext.request.contextPath}/movies/${movie.id}"
                      style="display:inline;">
                    <input type="hidden" name="_method" value="DELETE">
                    <button type="submit">Удалить</button>
                </form>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<h2>Добавить новый фильм</h2>
<form method="post" action="${pageContext.request.contextPath}/movies">
    <label>Название: <input type="text" name="title" required></label><br>
    <label>Описание: <textarea name="description"></textarea></label><br>
    <label>Год выпуска: <input type="number" name="releaseYear" required></label><br>
    <label>Продолжительность (мин): <input type="number" name="duration" required></label><br>
    <label>Язык: <input type="text" name="language" required></label><br>
    <label>URL постера: <input type="text" name="posterUrl"></label><br>
    <button type="submit">Добавить</button>
</form>
</body>
</html>
