<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${movie.title}</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link href="${pageContext.request.contextPath}/css/movie-details.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container my-5">
    <h1 class="text-center mb-4">${movie.title}</h1>
    <div class="row">
        <div class="col-md-4">
            <img src="data:image/jpeg;base64,${movie.posterBase64}" class="img-fluid rounded shadow" alt="${movie.title}">
        </div>
        <div class="col-md-8">
            <ul class="list-group mb-4">
                <li class="list-group-item"><strong>Описание:</strong> ${movie.description}</li>
                <li class="list-group-item"><strong>Жанры:</strong>
                    <c:forEach var="genre" items="${movie.genres}">
                        <span class="badge bg-primary">${genre}</span>
                    </c:forEach>
                </li>
                <li class="list-group-item"><strong>Актеры:</strong>
                    <c:forEach var="actor" items="${movie.actors}">
                        <span class="badge bg-info">${actor}</span>
                    </c:forEach>
                </li>
                <li class="list-group-item"><strong>Режиссеры:</strong>
                    <c:forEach var="director" items="${movie.directors}">
                        <span class="badge bg-warning text-dark">${director}</span>
                    </c:forEach>
                </li>
                <li class="list-group-item"><strong>Продюсеры:</strong>
                    <c:forEach var="producer" items="${movie.producers}">
                        <span class="badge bg-success">${producer}</span>
                    </c:forEach>
                </li>
                <li class="list-group-item"><strong>Год выпуска:</strong> ${movie.releaseYear}</li>
                <li class="list-group-item"><strong>Продолжительность:</strong> ${movie.duration} мин</li>
                <li class="list-group-item"><strong>Язык:</strong> ${movie.language}</li>
            </ul>
            <a href="${pageContext.request.contextPath}/movies" class="btn btn-secondary">Назад к списку фильмов</a>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
