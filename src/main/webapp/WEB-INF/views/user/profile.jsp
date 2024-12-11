<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Профиль пользователя</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body class="bg-light">
<nav class="navbar navbar-expand-lg navbar-dark bg-dark mb-4">
    <div class="container">
        <a class="navbar-brand mx-auto" href="${pageContext.request.contextPath}/user/profile">
            <strong>КиноХелпер</strong>
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
                aria-controls="navbarNav" aria-expanded="false" aria-label="Переключить навигацию">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse justify-content-center" id="navbarNav">
            <ul class="navbar-nav">
                <li class="nav-item">
                    <a class="nav-link active" aria-current="page" href="${pageContext.request.contextPath}/user/profile">
                        <i class="bi bi-film"></i> Профиль
                    </a>
                </li>
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
    <div class="card shadow-sm p-4">
        <h1 class="text-center mb-4">Профиль пользователя</h1>
        <div class="row">
            <div class="col-md-4 text-center">
                <i class="bi bi-person-circle" style="font-size: 6rem;"></i>
            </div>
            <div class="col-md-8">
                <h2>Добро пожаловать, ${user.username}!</h2>
                <p><strong>Email:</strong> ${user.email}</p>
                <p><strong>Идентификатор пользователя:</strong> ${user.id}</p>
            </div>
        </div>
    </div>

    <div class="card shadow-sm p-4 mt-4">
        <h2 class="text-center mb-4">Последняя активность</h2>
        <c:if test="${not empty recentReviews}">
            <ul class="list-group">
                <c:forEach var="review" items="${recentReviews}">
                    <li class="list-group-item">
                        <div class="d-flex justify-content-between">
                            <span><strong>Фильм:</strong> ${review.movie.title}</span>
                            <span class="badge <c:choose>
                                <c:when test="${review.status == 'APPROVED'}">bg-success</c:when>
                                <c:when test="${review.status == 'REJECTED'}">bg-danger</c:when>
                                <c:otherwise>bg-warning</c:otherwise>
                            </c:choose>">${review.status}</span>
                        </div>
                        <p><strong>Рецензия:</strong> ${review.content}</p>
                        <p><strong>Рейтинг:</strong> ${review.rating} из 5</p>
                        <p class="text-muted"><strong>Дата:</strong> ${review.createdAt}</p>
                    </li>
                </c:forEach>
            </ul>
        </c:if>
        <c:if test="${empty recentReviews}">
            <p class="text-center">Нет активности за последние 5 дней.</p>
        </c:if>
    </div>
</div>
<footer class="text-center mt-5">
    <p>&copy; 2024 MovieReview</p>
</footer>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
