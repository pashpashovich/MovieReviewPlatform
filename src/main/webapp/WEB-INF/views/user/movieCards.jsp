<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Поиск и фильтрация фильмов</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body class="bg-light">
<script src="${pageContext.request.contextPath}/js/movieCard.js" defer></script>
<div class="container my-5">
    <h1 class="text-center mb-4">Каталог фильмов</h1>
    <div class="card p-4 shadow-sm mb-4">
        <form id="filterForm">
            <div class="row g-3">
                <div class="col-md-6">
                    <label for="searchQuery" class="form-label">Поиск по названию</label>
                    <input type="text" id="searchQuery" name="searchQuery" class="form-control" placeholder="Введите название фильма">
                </div>
                <div class="col-md-3">
                    <label for="genreFilter" class="form-label">Жанры</label>
                    <select id="genreFilter" name="genre" class="form-select">
                        <option value="">Все жанры</option>
                        <c:forEach var="genre" items="${genres}">
                            <option value="${genre.id}">${genre.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-3">
                    <label for="languageFilter" class="form-label">Язык</label>
                    <select id="languageFilter" name="language" class="form-select">
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
                    <label for="yearFilter" class="form-label">Год выпуска</label>
                    <input type="number" id="yearFilter" name="year" class="form-control" placeholder="Например, 2023">
                </div>
                <div class="col-md-3">
                    <label for="durationFilter" class="form-label">Продолжительность</label>
                    <input type="number" id="durationFilter" name="duration" class="form-control" placeholder="Минуты">
                </div>
                <div class="col-md-3 align-self-end">
                    <button type="button" class="btn btn-primary w-100" onclick="applyFilters()">Применить фильтры</button>
                </div>
                <div class="col-md-3 align-self-end">
                    <button type="reset" class="btn btn-secondary w-100" onclick="resetFilters()">Сбросить</button>                </div>
            </div>
        </form>
    </div>

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
                        <p class="card-text">
                            <strong>Язык:</strong> ${movie.language}
                        </p>
                        <p class="card-text">
                            <strong>Год:</strong> ${movie.releaseYear}
                        </p>
                        <p class="card-text">
                            <strong>Продолжительность:</strong> ${movie.duration} мин
                        </p>
                        <a href="${pageContext.request.contextPath}/movies/${movie.id}" class="btn btn-primary">Подробнее</a>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>

<script>

</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

