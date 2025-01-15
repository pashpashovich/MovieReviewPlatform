<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${lang}" />
<fmt:setBundle basename="messages" />
<!DOCTYPE html>
<html lang="${lang}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${movie.title}</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/movie-details.css" rel="stylesheet">
</head>
<body class="bg-light">
<nav class="navbar navbar-expand-lg navbar-dark bg-dark mb-4">
    <div class="container">
        <a class="navbar-brand mx-auto" href="${pageContext.request.contextPath}/user/profile">
            <strong><fmt:message key="app.name"/></strong>
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
                aria-controls="navbarNav" aria-expanded="false" aria-label="<fmt:message key='navbar.toggle'/>">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse justify-content-center" id="navbarNav">
            <ul class="navbar-nav">
                <li class="nav-item">
                    <a class="nav-link" aria-current="page" href="${pageContext.request.contextPath}/user/profile">
                        <i class="bi bi-film"></i> <fmt:message key="navbar.profile"/>
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" aria-current="page" href="${pageContext.request.contextPath}/user/movies">
                        <i class="bi bi-film"></i> <fmt:message key="navbar.movies"/>
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/user/watchlist">
                        <i class="bi bi-tags"></i> <fmt:message key="navbar.watchlist"/>
                    </a>
                </li>
            </ul>
            <form method="post" action="${pageContext.request.contextPath}/logout" class="d-flex">
                <button class="btn btn-danger btn-sm" type="submit">
                    <i class="bi bi-box-arrow-right"></i> <fmt:message key="navbar.logout"/>
                </button>
            </form>
        </div>
    </div>
    <div class="ml-auto">
        <a href="?lang=en" class="text-white">EN</a> |
        <a href="?lang=ru" class="text-white">RU</a>
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
                <li class="list-group-item">
                    <strong><fmt:message key="movie.rating"/></strong>
                    <span class="text-warning rating">
                        <c:forEach begin="1" end="5" var="star">
                            <i class="bi ${star <= averageRating ? 'bi-star-fill' : 'bi-star'}"
                               style="color: gold;"></i>
                        </c:forEach>
                        (${averageRating})
                    </span>
                </li>
                <li class="list-group-item">
                    <strong><fmt:message key="movie.description"/></strong> ${movie.description}
                </li>
                <li class="list-group-item">
                    <strong><fmt:message key="movie.genres"/></strong>
                    <c:forEach var="genre" items="${movie.genres}">
                        <span class="badge bg-primary">${genre}</span>
                    </c:forEach>
                </li>
                <li class="list-group-item">
                    <strong><fmt:message key="movie.directors"/></strong>
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
                    <i class="bi bi-plus-circle"></i> <fmt:message key="movie.addToWatchlist"/>
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
            <h3><fmt:message key="movie.reviews"/></h3>
            <c:if test="${not empty reviews}">
                <ul class="list-group">
                    <c:forEach var="review" items="${reviews}">
                        <li class="list-group-item">
                            <strong>${review.user.username}</strong> (<fmt:message
                                key="movie.ratingValue"/>: ${review.rating})
                            <small class="text-muted createdAt">${review.createdAt}</small>
                            <p>${review.content}</p>
                        </li>
                    </c:forEach>
                </ul>
            </c:if>
            <c:if test="${empty reviews}">
                <p><fmt:message key="movie.noReviews"/></p>
            </c:if>
        </div>
    </div>

    <div class="card mt-4">
        <div class="card-body">
            <h3><fmt:message key="movie.addReview"/></h3>
            <c:if test="${not empty sessionScope.successMessage}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                        ${sessionScope.successMessage}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
                <c:remove var="successMessage" scope="session"/>
            </c:if>
            <form method="POST" action="${pageContext.request.contextPath}/user/movies/review">
                <input type="hidden" name="movieId" value="${movie.id}">
                <div class="mb-3">
                    <label for="reviewContent" class="form-label"><fmt:message key="movie.reviewContent"/></label>
                    <textarea id="reviewContent" name="content" class="form-control" rows="5" required></textarea>
                </div>
                <div class="mb-3">
                    <label for="reviewRating" class="form-label"><fmt:message key="movie.reviewRating"/></label>
                    <select id="reviewRating" name="rating" class="form-select" required>
                        <c:forEach begin="1" end="5" var="star">
                            <option value="${star}">${star}</option>
                        </c:forEach>
                    </select>
                </div>
                <button type="submit" class="btn btn-success"><fmt:message key="movie.submitReview"/></button>
            </form>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
