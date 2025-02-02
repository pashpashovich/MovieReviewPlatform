<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${lang}"/>
<fmt:setBundle basename="messages"/>
<!DOCTYPE html>
<html lang="${lang}">
<head>
    <meta charset="UTF-8">
    <title><fmt:message key="watchlist.title"/></title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">
</head>
<body class="bg-light">
<nav class="navbar navbar-expand-lg navbar-dark bg-dark mb-4">
    <div class="container">
        <a class="navbar-brand mx-auto" href="${pageContext.request.contextPath}/user/profile">
            <strong><fmt:message key="app.name"/></strong>
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
                aria-controls="navbarNav" aria-expanded="false" aria-label="<fmt:message key='navbar.toggle' />">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse justify-content-center" id="navbarNav">
            <ul class="navbar-nav">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/user/profile/${userId}">
                        <i class="bi bi-film"></i> <fmt:message key="navbar.profile"/>
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/user/movies/${userId}">
                        <i class="bi bi-film"></i> <fmt:message key="navbar.search"/>
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link active" href="${pageContext.request.contextPath}/user/watchlist/${userId}">
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
    <h1 class="text-center mb-4"><fmt:message key="watchlist.title"/></h1>
    <c:choose>
        <c:when test="${not empty watchlist}">
            <div class="row row-cols-1 row-cols-md-3 g-4">
                <c:forEach var="watchlistItem" items="${watchlist}">
                    <div class="col">
                        <div class="card h-100">
                            <img src="data:image/jpeg;base64,${watchlistItem.posterBase64}" class="card-img-top"
                                 alt="${watchlistItem.movieTitle}">
                            <div class="card-body">
                                <h5 class="card-title">${watchlistItem.movieTitle}</h5>
                                <p class="card-text">
                                    <strong><fmt:message key="watchlist.added"/>:</strong>
                                    <span class="addedAt">${watchlistItem.addedAt}</span>
                                </p>
                                <form method="post" action="${pageContext.request.contextPath}/user/watchlist">
                                    <input type="hidden" name="_method" value="DELETE">
                                    <input type="hidden" name="userId" value="${userId}">
                                    <input type="hidden" name="movieId" value="${watchlistItem.movieId}">
                                    <button type="submit" class="btn btn-danger">
                                        <fmt:message key="watchlist.delete"/>
                                    </button>
                                </form>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:when>
        <c:otherwise>
            <div class="alert alert-info text-center" role="alert">
                <fmt:message key="watchlist.empty"/>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<script>
    function formatDateTime(localDateTime) {
        const date = new Date(localDateTime);
        return date.toLocaleString('ru-RU', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit',
        });
    }

    document.querySelectorAll('.addedAt').forEach((element) => {
        const rawDate = element.textContent;
        element.textContent = formatDateTime(rawDate);
    });
</script>
</body>
</html>
