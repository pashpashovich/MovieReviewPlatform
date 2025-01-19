<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${lang}"/>
<fmt:setBundle basename="messages"/>
<!DOCTYPE html>
<html lang="${lang}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><fmt:message key="title"/></title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body class="bg-light">
<nav class="navbar navbar-expand-lg navbar-dark bg-dark mb-4">
    <div class="container">
        <a class="navbar-brand mx-auto" href="${pageContext.request.contextPath}/user/profile">
            <strong><fmt:message key="app.name"/></strong>
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
                aria-controls="navbarNav" aria-expanded="false" aria-label="Переключить навигацию">
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
                    <a class="nav-link active" aria-current="page"
                       href="${pageContext.request.contextPath}/user/movies">
                        <i class="bi bi-film"></i> <fmt:message key="navbar.search"/>
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
        <a href="?lang=en" class="text-white">EN</a>
        <a href="?lang=ru" class="text-white">RU</a>
        <a href="?lang=by" class="text-white">BY</a>
    </div>
</nav>
<div class="container my-5">
    <h1 class="text-center mb-4"><fmt:message key="title"/></h1>
    <div class="card p-4 shadow-sm mb-4">
        <form id="filterForm" method="POST" action="${pageContext.request.contextPath}/user/movies">
            <div class="row g-3">
                <div class="col-md-6">
                    <label class="form-label"><fmt:message key="filter.title"/></label>
                    <input type="text" id="searchQueryInput" name="searchQuery" class="form-control"
                           placeholder="<fmt:message key='filter.title' />" value="${param.searchQuery}">
                </div>
                <div class="col-md-3">
                    <label class="form-label"><fmt:message key="filter.genres"/></label>
                    <select id="genreFilterInput" name="genre" class="form-select">
                        <option value=""><fmt:message key="filter.allgenres"/></option>
                        <c:forEach var="genre" items="${genres}">
                            <option value="${genre.id}"
                                    <c:if test="${genre.id == param.genre}">selected</c:if>>${genre.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-3">
                    <label class="form-label"><fmt:message key="filter.language"/></label>
                    <select id="languageFilterInput" name="language" class="form-select">
                        <option value="" disabled ${empty param.language ? 'selected' : ''}><fmt:message
                                key="form.selectLanguage"/></option>
                        <option value="Русский" ${param.language == 'Русский' ? 'selected' : ''}><fmt:message
                                key="form.russian"/></option>
                        <option value="Английский" ${param.language == 'Английский' ? 'selected' : ''}><fmt:message
                                key="form.english"/></option>
                        <option value="Французский" ${param.language == 'Французский' ? 'selected' : ''}><fmt:message
                                key="form.french"/></option>
                        <option value="Испанский" ${param.language == 'Испанский' ? 'selected' : ''}><fmt:message
                                key="form.spanish"/></option>
                        <option value="Белорусский" ${param.language == 'Белорусский' ? 'selected' : ''}><fmt:message
                                key="form.belarusian"/></option>
                    </select>
                </div>
            </div>
            <div class="row g-3 mt-3">
                <div class="col-md-3">
                    <label class="form-label"><fmt:message key="filter.year"/></label>
                    <input type="number" id="yearFilterInput" name="year" class="form-control"
                           placeholder="2023" value="${param.year}">
                </div>
                <div class="col-md-3">
                    <label class="form-label"><fmt:message key="filter.duration"/></label>
                    <input type="number" id="durationFilterInput" name="duration" class="form-control"
                           placeholder="<fmt:message key="filter.minutes"/>" value="${param.duration}">
                </div>
                <div class="col-md-3 align-self-end">
                    <button type="button" class="btn btn-primary w-100" onclick="applyFilters()">
                        <fmt:message key="filter.apply"/>
                    </button>
                </div>
                <div class="col-md-3 align-self-end">
                    <button type="reset" class="btn btn-secondary w-100" onclick="resetFilters()">
                        <fmt:message key="filter.reset"/>
                    </button>
                </div>
            </div>
        </form>
    </div>
    <div class="row mb-4">
        <div class="col-md-12">
            <h3><fmt:message key="recommendations.title"/></h3>
            <c:choose>
                <c:when test="${empty recommendations}">
                    <div class="alert alert-info text-center" role="alert">
                        <fmt:message key="recommendations.empty"/>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="row row-cols-1 row-cols-md-3 g-4">
                        <c:forEach var="recommendation" items="${recommendations}">
                            <div class="col">
                                <div class="card h-100">
                                    <img src="data:image/jpeg;base64,${recommendation.posterBase64}"
                                         class="card-img-top"
                                         alt="${recommendation.title}">
                                    <div class="card-body">
                                        <h5 class="card-title">${recommendation.title}</h5>
                                        <a href="${pageContext.request.contextPath}/user/movies/${recommendation.id}"
                                           class="btn btn-outline-primary"><fmt:message key="movies.more"/></a>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12">
            <h3><fmt:message key="movies.title"/></h3>
            <c:choose>
                <c:when test="${empty movies}">
                    <div class="alert alert-warning text-center" role="alert">
                        <strong><fmt:message key="movies.empty"/></strong>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="row row-cols-1 row-cols-md-3 g-4" id="moviesCards">
                        <c:forEach var="movie" items="${movies}">
                            <div class="col">
                                <div class="card h-100">
                                    <img src="data:image/jpeg;base64,${movie.posterBase64}" class="card-img-top"
                                         alt="${movie.title}">
                                    <div class="card-body">
                                        <h5 class="card-title">${movie.title}</h5>
                                        <p class="card-text"><strong><fmt:message
                                                key="filter.genres"/></strong> ${movie.genres}</p>
                                        <p class="card-text"><strong><fmt:message
                                                key="filter.language"/></strong> ${movie.language}</p>
                                        <p class="card-text"><strong><fmt:message
                                                key="filter.year"/></strong> ${movie.releaseYear}</p>
                                        <p class="card-text"><strong><fmt:message
                                                key="filter.duration"/></strong> ${movie.duration} мин</p>
                                        <a href="${pageContext.request.contextPath}/user/movies/${movie.id}"
                                           class="btn btn-primary"><fmt:message key="movies.more"/></a>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    <nav aria-label="Page navigation example">
                        <ul class="pagination justify-content-center">
                            <c:forEach var="i" begin="1" end="${totalPages}">
                                <li class="page-item ${i == currentPage ? 'active' : ''}">
                                    <a class="page-link"
                                       href="?page=${i}&size=9&search=${param.search}&sort=${param.sort}">
                                            ${i}
                                    </a></li>
                            </c:forEach>
                        </ul>
                    </nav>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
<script>
    function applyFilters() {
        document.getElementById('filterForm').submit();
    }

    function resetFilters() {
        document.getElementById('filterForm').reset();
        window.location.href = `${window.location.pathname}`;
    }
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
