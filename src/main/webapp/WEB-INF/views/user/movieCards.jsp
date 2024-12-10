<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Каталог фильмов</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
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
                    <a class="nav-link active" aria-current="page" href="${pageContext.request.contextPath}/user/movies">
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
    <h1 class="text-center mb-4">Каталог фильмов</h1>
    <div class="card p-4 shadow-sm mb-4">
        <form id="filterForm" method="POST" action="${pageContext.request.contextPath}/user/movies">
            <div class="row g-3">
                <div class="col-md-6">
                    <label class="form-label">Поиск по названию</label>
                    <input type="text" id="searchQueryInput" name="searchQuery" class="form-control"
                           placeholder="Введите название фильма">
                </div>
                <div class="col-md-3">
                    <label class="form-label">Жанры</label>
                    <select id="genreFilterInput" name="genre" class="form-select">
                        <option value="">Все жанры</option>
                        <c:forEach var="genre" items="${genres}">
                            <option value="${genre.id}">${genre.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-3">
                    <label class="form-label">Язык</label>
                    <select id="languageFilterInput" name="language" class="form-select">
                        <option value="">Все языки</option>
                        <option value="Русский">Русский</option>
                        <option value="Английский">Английский</option>
                        <option value="Французский">Французский</option>
                        <option value="Испанский">Испанский</option>
                        <option value="Белорусский">Белорусский</option>
                    </select>
                </div>
            </div>
            <div class="row g-3 mt-3">
                <div class="col-md-3">
                    <label class="form-label">Год выпуска</label>
                    <input type="number" id="yearFilterInput" name="year" class="form-control"
                           placeholder="Например, 2023">
                </div>
                <div class="col-md-3">
                    <label class="form-label">Продолжительность</label>
                    <input type="number" id="durationFilterInput" name="duration" class="form-control"
                           placeholder="Минуты">
                </div>
                <div class="col-md-3 align-self-end">
                    <button type="button" class="btn btn-primary w-100" onclick="applyFilters()">Применить фильтры
                    </button>
                </div>
                <div class="col-md-3 align-self-end">
                    <button type="reset" class="btn btn-secondary w-100" onclick="resetFilters()">Сбросить</button>
                </div>
            </div>
        </form>
    </div>
    <div class="row">
        <div class="col-md-12">
            <h3>Рекомендации для вас</h3>
            <div class="row row-cols-1 row-cols-md-3 g-4">
                <c:forEach var="recommendation" items="${recommendations}">
                    <div class="col">
                        <div class="card h-100">
                            <img src="data:image/jpeg;base64,${recommendation.posterBase64}" class="card-img-top"
                                 alt="${recommendation.title}">
                            <div class="card-body">
                                <h5 class="card-title">${recommendation.title}</h5>
                                <a href="${pageContext.request.contextPath}/user/movies/${recommendation.id}"
                                   class="btn btn-outline-primary">Подробнее</a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
    <h3>Фильмы</h3>
    <div class="row row-cols-1 row-cols-md-3 g-4" id="moviesCards">
        <c:forEach var="movie" items="${movies}">
            <div class="col">
                <div class="card h-100">
                    <img src="data:image/jpeg;base64,${movie.posterBase64}" class="card-img-top" alt="${movie.title}">
                    <div class="card-body">
                        <h5 class="card-title">${movie.title}</h5>
                        <p class="card-text">
                            <strong>Жанры:</strong>
                            <c:forEach var="genre" items="${movie.genres}">
                                <span class="badge bg-primary">${genre}</span>
                            </c:forEach>
                        </p>
                        <p class="card-text"><strong>Язык:</strong> ${movie.language}</p>
                        <p class="card-text"><strong>Год:</strong> ${movie.releaseYear}</p>
                        <p class="card-text"><strong>Продолжительность:</strong> ${movie.duration} мин</p>
                        <a href="${pageContext.request.contextPath}/user/movies/${movie.id}" class="btn btn-primary">Подробнее</a>


                        <form method="POST" action="${pageContext.request.contextPath}/user/movies/rate">
                            <input type="hidden" name="movieId" value="${movie.id}">
                            <div class="rating">
                                <c:forEach begin="1" end="5" var="star">
                                    <input
                                            type="radio"
                                            id="star-${star}-${movie.id}"
                                            name="rating"
                                            value="${star}"
                                            <c:if test="${userRatings[movie.id] == star}">checked</c:if> />
                                    <label for="star-${star}-${movie.id}" class="star-label">
                                        <i class="bi bi-star-fill"></i>
                                    </label>
                                </c:forEach>
                            </div>
                            <button type="submit" class="btn btn-success mt-3">Оценить</button>
                        </form>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>


    <script>
        function applyFilters() {
            const form = document.getElementById('filterForm');
            const searchQuery = document.getElementById('searchQueryInput').value;
            const genre = document.getElementById('genreFilterInput').value;
            const language = document.getElementById('languageFilterInput').value;
            const year = document.getElementById('yearFilterInput').value;
            const duration = document.getElementById('durationFilterInput').value;

            form.searchQuery.value = searchQuery;
            form.genre.value = genre;
            form.language.value = language;
            form.year.value = year;
            form.duration.value = duration;

            form.submit();
        }

        function resetFilters() {
            const form = document.getElementById('filterForm');
            form.reset();
            window.location.href = `${window.location.pathname}`;
        }
    </script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
