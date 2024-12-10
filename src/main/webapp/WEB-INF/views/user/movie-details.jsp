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
                    <a class="nav-link" aria-current="page" href="${pageContext.request.contextPath}/user/movies">
                        <i class="bi bi-film"></i> Поиск фильмов
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/user/watchlist">
                        <i class="bi bi-tags"></i> Хочу посмотреть
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
    <h1 class="text-center mb-4">${movie.title}</h1>
    <div class="row">
        <div class="col-md-4">
            <img src="data:image/jpeg;base64,${movie.posterBase64}" class="img-fluid rounded shadow"
                 alt="${movie.title}">
        </div>
        <div class="col-md-8">
            <ul class="list-group mb-4">
                <li class="list-group-item"><strong>Средний рейтинг:</strong>
                    <span class="text-warning rating">
                        <c:forEach begin="1" end="5" var="star">
                            <i class="bi ${star <= averageRating ? 'bi-star-fill' : 'bi-star'}"
                               style="color: gold;"></i>
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
                <button
                        type="submit"
                        class="btn btn-outline-success"
                        <c:if test="${isInList}">disabled</c:if>
                >
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
            <h3>Рецензии</h3>
            <c:if test="${not empty reviews}">
                <ul class="list-group">
                    <c:forEach var="review" items="${reviews}">
                        <li class="list-group-item">
                            <strong>${review.user.username}</strong> (оценка: ${review.rating}/5)
                            <small class="text-muted">Опубликовано: ${review.createdAt}</small>
                            <p>${review.content}</p>
                        </li>
                    </c:forEach>
                </ul>
            </c:if>
            <c:if test="${empty reviews}">
                <p>Пока нет рецензий на этот фильм.</p>
            </c:if>
        </div>
    </div>

    <div class="card mt-4">
        <div class="card-body">
            <h3>Написать рецензию</h3>
            <c:if test="${not empty sessionScope.successMessage}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                        ${sessionScope.successMessage}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
                <c:remove var="successMessage" scope="session" />
            </c:if>
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
