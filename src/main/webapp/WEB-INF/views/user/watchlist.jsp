<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Хочу посмотреть</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">
<div class="container my-5">
    <h1 class="text-center mb-4">Хочу посмотреть</h1>
    <div class="row row-cols-1 row-cols-md-3 g-4">
        <c:forEach var="watchlistItem" items="${watchlist}">
            <div class="col">
                <div class="card h-100">
                    <img src="data:image/jpeg;base64,${watchlistItem.posterBase64}" class="card-img-top"
                         alt="${watchlistItem.movieTitle}">
                    <div class="card-body">
                        <h5 class="card-title">${watchlistItem.movieTitle}</h5>
                        <p class="card-text"><strong>Добавлено:</strong> ${watchlistItem.addedAt}</p>
                        <form method="POST" action="${pageContext.request.contextPath}/user/watchlist">
                            <input type="hidden" name="_method" value="DELETE">
                            <input type="hidden" name="movieId" value="${watchlistItem.movieId}">
                            <button type="submit" class="btn btn-danger">
                                Удалить
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>
</body>
</html>
