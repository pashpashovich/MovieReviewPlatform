<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${lang}"/>
<fmt:setBundle basename="messages" />
<!DOCTYPE html>
<html lang="${lang}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><fmt:message key="user.profile.heading" /></title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body class="bg-light">
<nav class="navbar navbar-expand-lg navbar-dark bg-dark mb-4">
    <div class="container">
        <a class="navbar-brand mx-auto" href="${pageContext.request.contextPath}/user/profile">
            <strong><fmt:message key="app.name" /></strong>
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
                aria-controls="navbarNav" aria-expanded="false" aria-label="<fmt:message key='navbar.toggle' />">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse justify-content-center" id="navbarNav">
            <ul class="navbar-nav">
                <li class="nav-item">
                    <a class="nav-link active" aria-current="page" href="${pageContext.request.contextPath}/user/profile">
                        <i class="bi bi-film"></i> <fmt:message key="navbar.profile" />
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/user/movies">
                        <i class="bi bi-film"></i> <fmt:message key="navbar.search" />
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/user/watchlist">
                        <i class="bi bi-tags"></i> <fmt:message key="navbar.watchlist" />
                    </a>
                </li>
            </ul>
            <form method="post" action="${pageContext.request.contextPath}/logout" class="d-flex">
                <button class="btn btn-danger btn-sm" type="submit">
                    <i class="bi bi-box-arrow-right"></i> <fmt:message key="navbar.logout" />
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
    <div class="card shadow-sm p-4">
        <h1 class="text-center mb-4"><fmt:message key="user.profile.heading" /></h1>
        <div class="row">
            <div class="col-md-4 text-center">
                <i class="bi bi-person-circle" style="font-size: 6rem;"></i>
            </div>
            <div class="col-md-8">
                <h2><fmt:message key="user.profile.welcome" />, ${user.username}!</h2>
                <p><strong><fmt:message key="user.profile.email" />:</strong> ${user.email}</p>
                <p><strong><fmt:message key="user.profile.id" />:</strong> ${user.id}</p>
            </div>
        </div>
    </div>

    <div class="card shadow-sm p-4 mt-4">
        <h2 class="text-center mb-4"><fmt:message key="user.profile.activity.title" /></h2>
        <c:if test="${not empty recentReviews}">
            <ul class="list-group">
                <c:forEach var="review" items="${recentReviews}">
                    <li class="list-group-item">
                        <div class="d-flex justify-content-between">
                            <span><strong><fmt:message key="user.profile.activity.movie" />:</strong> ${review.movie.title}</span>
                            <span class="badge
                                <c:choose>
                                    <c:when test="${review.status == 'APPROVED'}">bg-success</c:when>
                                    <c:when test="${review.status == 'REJECTED'}">bg-danger</c:when>
                                    <c:when test="${review.status == 'PENDING'}">bg-warning</c:when>
                                    <c:otherwise>bg-secondary</c:otherwise>
                                </c:choose>">
                                <c:choose>
                                    <c:when test="${review.status == 'APPROVED'}"><fmt:message key="review.status.approved" /></c:when>
                                    <c:when test="${review.status == 'REJECTED'}"><fmt:message key="review.status.rejected" /></c:when>
                                    <c:when test="${review.status == 'PENDING'}"><fmt:message key="review.status.pending" /></c:when>
                                    <c:otherwise><fmt:message key="review.status.unknown" /></c:otherwise>
                                </c:choose>
                            </span>
                        </div>
                        <p><strong><fmt:message key="user.profile.activity.review" />:</strong> ${review.content}</p>
                        <p><strong><fmt:message key="user.profile.activity.rating" />:</strong> ${review.rating} из 5</p>
                        <p class="text-muted">
                            <strong><fmt:message key="user.profile.activity.date" />:</strong>
                            <span class="createdAt">${review.createdAt}</span>
                        </p>
                    </li>
                </c:forEach>
            </ul>
        </c:if>
        <c:if test="${empty recentReviews}">
            <p class="text-center"><fmt:message key="user.profile.activity.empty" /></p>
        </c:if>
    </div>
</div>
<footer class="text-center mt-5">
    <p>&copy; 2024 <fmt:message key="footer.brand" /></p>
</footer>
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

    document.querySelectorAll('.createdAt').forEach((element) => {
        const rawDate = element.textContent;
        element.textContent = formatDateTime(rawDate);
    });
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
