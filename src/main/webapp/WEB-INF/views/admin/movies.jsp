<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${lang}"/>
<fmt:setBundle basename="messages"/>
<!DOCTYPE html>
<html lang="${lang}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><fmt:message key="page.title"/></title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function editMovie(id, title, description, releaseYear, duration, language, selectedGenres, selectedActors, selectedDirectors, selectedProducers) {
        document.getElementById('id').value = id;
        document.getElementById('title').value = title;
        document.getElementById('description').value = description;
        document.getElementById('releaseYear').value = releaseYear;
        document.getElementById('duration').value = duration;
        document.getElementById('language').value = language;
        document.getElementById('method').value = 'PUT';

        document.getElementById('formTitle').innerText = document.getElementById('editMovieMessage').textContent;

        updateSelect(document.getElementById('genres'), selectedGenres);
        updateSelect(document.getElementById('actors'), selectedActors);
        updateSelect(document.getElementById('directors'), selectedDirectors);
        updateSelect(document.getElementById('producers'), selectedProducers);
    }

    function resetForm() {
        document.getElementById('id').value = '';
        document.getElementById('title').value = '';
        document.getElementById('description').value = '';
        document.getElementById('releaseYear').value = '';
        document.getElementById('duration').value = '';
        document.getElementById('language').value = '';
        document.getElementById('method').value = 'POST';

        document.getElementById('formTitle').innerText = document.getElementById('editMovieMessage').textContent;

        resetSelect(document.getElementById('genres'));
        resetSelect(document.getElementById('actors'));
        resetSelect(document.getElementById('directors'));
        resetSelect(document.getElementById('producers'));
    }

    function updateSelect(selectElement, selectedValues) {
        if (selectedValues.length === 1 && typeof selectedValues[0] === 'string') {
            selectedValues = selectedValues[0].split(',').map(value => value.trim());
        }
        const selectedSet = new Set(selectedValues);
        Array.from(selectElement.options).forEach(option => {
            option.selected = selectedSet.has(option.value);
        });
    }

    function resetSelect(selectElement) {
        Array.from(selectElement.options).forEach(option => {
            option.selected = false;
        });
    }

    function validateForm() {
        const language = document.getElementById("language").value;
        if (!language) {
            alert(document.getElementById('alert').textContent);
            return false;
        }
        return true;
    }
</script>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark mb-4">
    <div class="container">
        <a class="navbar-brand mx-auto" href="${pageContext.request.contextPath}/admin/movies">
            <strong><fmt:message key="nav.title.default"/></strong>
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
                aria-controls="navbarNav" aria-expanded="false" aria-label="Переключить навигацию">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse justify-content-center" id="navbarNav">
            <ul class="navbar-nav">
                <li class="nav-item">
                    <a class="nav-link active" aria-current="page"
                       href="${pageContext.request.contextPath}/admin/movies">
                        <i class="bi bi-film"></i> <fmt:message key="navbar.admin.movies"/>
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/genres">
                        <i class="bi bi-tags"></i> <fmt:message key="navbar.admin.genres"/>
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/people">
                        <i class="bi bi-person-stars"></i> <fmt:message key="navbar.admin.stars"/>
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/users">
                        <i class="bi bi-person-stars"></i> <fmt:message key="navbar.admin.users"/>
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/reviews">
                        <i class="bi bi-person-stars"></i> <fmt:message key="navbar.admin.reviews"/>
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
    <form method="get" action="" class="mb-4">
        <div class="input-group">
            <input type="text" name="title" class="form-control"
                   placeholder="<fmt:message key='form.searchPlaceholder'/>" value="${param.title}">
            <button class="btn btn-primary" type="submit"><fmt:message key="button.search"/></button>
            <a href="${pageContext.request.contextPath}/admin/movies" class="btn btn-secondary ms-2">
                <i class="bi bi-x-circle"></i> <fmt:message key="admin.reset"/>
            </a>
        </div>
    </form>
    <h1 class="text-center mb-4"><fmt:message key="page.header"/></h1>
    <div class="table-responsive">
        <table class="table table-bordered table-striped">
            <thead class="table-dark">
            <tr class="text-center">
                <th><fmt:message key="table.poster"/></th>
                <th><fmt:message key="table.id"/></th>
                <th><fmt:message key="table.title"/></th>
                <th><fmt:message key="table.genres"/></th>
                <th><fmt:message key="table.actors"/></th>
                <th><fmt:message key="table.directors"/></th>
                <th><fmt:message key="table.producers"/></th>
                <th><fmt:message key="table.year"/></th>
                <th><fmt:message key="table.duration"/></th>
                <th><fmt:message key="table.language"/></th>
                <th><fmt:message key="table.actions"/></th>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${empty movies}">
                    <tr>
                        <td colspan="11" class="text-center text-muted">
                            <fmt:message key="table.noData"/>
                        </td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="movie" items="${movies}">
                        <tr class="text-center align-middle">
                            <td>
                                <img src="data:image/jpeg;base64,${movie.posterBase64}"
                                     alt="<fmt:message key="table.poster"/> ${movie.title}"
                                     class="img-thumbnail"
                                     style="max-width: 100px; max-height: 150px;"
                                >
                            </td>
                            <td>${movie.id}</td>
                            <td>${movie.title}</td>
                            <td>
                                <c:forEach var="genre" items="${movie.genres}">
                                    <span class="badge bg-primary">${genre}</span>
                                </c:forEach>
                            </td>
                            <td>
                                <c:forEach var="actor" items="${movie.actors}">
                                    <span class="badge bg-info">${actor}</span>
                                </c:forEach>
                            </td>
                            <td>
                                <c:forEach var="director" items="${movie.directors}">
                                    <span class="badge bg-warning text-dark">${director}</span>
                                </c:forEach>
                            </td>
                            <td>
                                <c:forEach var="producer" items="${movie.producers}">
                                    <span class="badge bg-success">${producer}</span>
                                </c:forEach>
                            </td>
                            <td>${movie.releaseYear}</td>
                            <td>${movie.duration} мин</td>
                            <td>${movie.language}</td>
                            <td>
                                <button
                                        class="btn btn-sm btn-primary mb-1"
                                        onclick="editMovie(
                                            ${movie.id},
                                                '${movie.title}',
                                                '${movie.description}',
                                            ${movie.releaseYear},
                                            ${movie.duration},
                                                '${movie.language}',
                                                ['<c:forEach var="genre"
                                                             items="${movie.genres}">${genre},</c:forEach>'.slice(0, -1)],
                                                ['<c:forEach var="actor"
                                                             items="${movie.actors}">${actor},</c:forEach>'.slice(0, -1)],
                                                ['<c:forEach var="director"
                                                             items="${movie.directors}">${director},</c:forEach>'.slice(0, -1)],
                                                ['<c:forEach var="producer"
                                                             items="${movie.producers}">${producer},</c:forEach>'.slice(0, -1)]
                                                )">
                                    <fmt:message key="button.edit"/>
                                </button>
                                <form method="post" action="${pageContext.request.contextPath}/admin/movies"
                                      style="display:inline;">
                                    <input type="hidden" name="id" value="${movie.id}">
                                    <input type="hidden" name="_method" value="DELETE">
                                    <button type="submit" class="btn btn-sm btn-danger"><fmt:message
                                            key="button.delete"/></button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </div>
    <nav aria-label="Page navigation example">
        <ul class="pagination justify-content-center">
            <c:forEach var="i" begin="1" end="${totalPages}">
                <li class="page-item ${i == currentPage ? 'active' : ''}">
                    <a class="page-link" href="?page=${i}&pageSize=5">${i}</a>
                </li>
            </c:forEach>
        </ul>
    </nav>
    <hr class="my-5">
    <div class="card">
        <div class="card-header bg-dark text-white">
            <h2 class="text-center mb-0" id="formTitle"><fmt:message key="form.addMovie"/></h2>
            <span id="editMovieMessage" style="display:none;"><fmt:message key="form.editMovie"/></span>
            <span id="alert" style="display:none;"><fmt:message key="alert"/></span>
            <h6 class="text-center mb-0"><fmt:message key="hint"/></h6>
        </div>
        <div class="card-body">
            <form method="post" action="${pageContext.request.contextPath}/admin/movies" enctype="multipart/form-data"
                  id="movieForm" onsubmit="return validateForm()">
                <input type="hidden" id="id" name="id">
                <input type="hidden" name="_method" id="method" value="POST">
                <div class="mb-3">
                    <label for="title" class="form-label"><fmt:message key="form.title"/></label>
                    <input type="text" id="title" name="title" class="form-control" required>
                </div>
                <div class="mb-3">
                    <label for="description" class="form-label"><fmt:message key="form.description"/></label>
                    <textarea id="description" name="description" class="form-control" rows="3"></textarea>
                </div>
                <div class="row">
                    <div class="col-md-4 mb-3">
                        <label for="releaseYear" class="form-label"><fmt:message key="table.year"/></label>
                        <input type="number" id="releaseYear" name="releaseYear" min="1895"
                               max="<%= java.time.Year.now().getValue() %>" class="form-control" required>
                    </div>
                    <div class="col-md-4 mb-3">
                        <label for="duration" class="form-label"><fmt:message key="form.duration"/></label>
                        <input type="number" id="duration" name="duration" min="1" class="form-control" required>
                    </div>
                    <div class="col-md-4 mb-3">
                        <label for="language" class="form-label"><fmt:message key="form.language"/></label>
                        <select id="language" name="language" class="form-select" required>
                            <option value="" disabled selected><fmt:message key="form.selectLanguage"/></option>
                            <option value="Русский"><fmt:message key="form.russian"/></option>
                            <option value="Английский"><fmt:message key="form.english"/></option>
                            <option value="Французский"><fmt:message key="form.french"/></option>
                            <option value="Испанский"><fmt:message key="form.spanish"/></option>
                            <option value="Белорусский"><fmt:message key="form.belarusian"/></option>
                        </select>
                    </div>
                </div>
                <div class="mb-3">
                    <label for="posterFile" class="form-label"><fmt:message key="table.poster"/></label>
                    <input type="file" id="posterFile" name="posterFile" class="form-control" accept="image/*">
                </div>
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="genres" class="form-label"><fmt:message key="table.genres"/></label>
                        <select name="genres" id="genres" class="form-select" multiple required>
                            <c:forEach var="genre" items="${genres}">
                                <option value="${genre.name}">${genre.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="actors" class="form-label"><fmt:message key="table.actors"/></label>
                        <select name="actors" id="actors" class="form-select" multiple required>
                            <c:forEach var="actor" items="${actors}">
                                <option value="${actor.fullName}">${actor.fullName}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="directors" class="form-label"><fmt:message key="table.directors"/></label>
                        <select name="directors" id="directors" class="form-select" multiple required>
                            <c:forEach var="director" items="${directors}">
                                <option value="${director.fullName}">${director.fullName}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="producers" class="form-label"><fmt:message key="table.producers"/></label>
                        <select name="producers" id="producers" class="form-select" multiple required>
                            <c:forEach var="producer" items="${producers}">
                                <option value="${producer.fullName}">${producer.fullName}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="text-center">
                    <button type="submit" class="btn btn-success"><fmt:message key="button.save"/></button>
                    <button type="button" class="btn btn-secondary" onclick="resetForm()"><fmt:message
                            key="button.reset"/></button>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>