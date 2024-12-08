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
                <li class="list-group-item"><strong>Средний рейтинг:</strong>
                    <span class="text-warning rating">
                        <c:forEach begin="1" end="5" var="star">
                            <i class="bi ${star <= averageRating ? 'bi-star-fill' : 'bi-star'}" style="color: gold;"></i>
                        </c:forEach>
                        (${averageRating})
                    </span>
                </li>
                <li class="list-group-item"><strong>Описание:</strong> ${movie.description}</li>
                <li class="list-group-item"><strong>Жанры:</strong>
                    <c:forEach var="genre" items="${movie.genres}">
                        <span class="badge bg-primary">${genre}</span>
                    </c:forEach>
                </li>
                <li class="list-group-item"><strong>Режиссеры:</strong>
                    <c:forEach var="director" items="${movie.directors}">
                        <span class="badge bg-warning text-dark">${director}</span>
                    </c:forEach>
                </li>
            </ul>
            <form method="POST" action="${pageContext.request.contextPath}/user/watchlist/add">
                <input type="hidden" name="movieId" value="${movie.id}">
                <button type="submit" class="btn btn-outline-success">
                    <i class="bi bi-plus-circle"></i> Хочу посмотреть
                </button>
                <c:if test="${not empty param.error}">
                    <div class="alert alert-warning">
                            ${param.error}
                    </div>
                </c:if>
            </form>
        </div>
    </div>

    <div class="card mt-4">
        <div class="card-body">
            <h3>Отзывы</h3>
            <c:if test="${not empty reviews}">
                <ul class="list-group">
                    <c:forEach var="review" items="${reviews}">
                        <li class="list-group-item">
                            <strong>${review.user.username}</strong> (оценка: ${review.rating}/5)
                            <p>${review.content}</p>
                            <small class="text-muted">Опубликовано: ${review.createdAt}</small>
                        </li>
                    </c:forEach>
                </ul>
            </c:if>
            <c:if test="${empty reviews}">
                <p>Пока нет отзывов на этот фильм.</p>
            </c:if>
        </div>
    </div>

    <div class="card mt-4">
        <div class="card-body">
            <h3>Написать рецензию</h3>
            <form method="POST" action="${pageContext.request.contextPath}/user/movies/review">
                <input type="hidden" name="movieId" value="${movie.id}">
                <div class="mb-3">
                    <label for="reviewContent" class="form-label">Текст рецензии</label>
                    <textarea id="reviewContent" name="content" class="form-control" rows="5" required></textarea>
                </div>
                <div class="mb-3">
                    <label for="reviewRating" class="form-label">Ваша оценка</label>
                    <select id="reviewRating" name="rating" class="form-select" required>
                        <c:forEach begin="1" end="5" var="star">
                            <option value="${star}">${star}</option>
                        </c:forEach>
                    </select>
                </div>
                <button type="submit" class="btn btn-success">Отправить рецензию</button>
            </form>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
