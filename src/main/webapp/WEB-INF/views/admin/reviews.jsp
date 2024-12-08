<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Управление рецензиями</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">
<div class="container my-5">
  <h1 class="text-center mb-4">Управление рецензиями</h1>
  <table class="table table-striped">
    <thead>
    <tr>
      <th>Автор</th>
      <th>Фильм</th>
      <th>Рецензия</th>
      <th>Статус</th>
      <th>Действия</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="review" items="${reviews}">
      <tr>
        <td>${review.user.username}</td>
        <td>${review.movie.title}</td>
        <td>${review.content}</td>
        <td>${review.status}</td>
        <td>
          <form method="POST" action="${pageContext.request.contextPath}/admin/reviews">
            <input type="hidden" name="reviewId" value="${review.id}">
            <select name="status" class="form-select">
              <option value="APPROVED">Одобрить</option>
              <option value="REJECTED">Отклонить</option>
            </select>
            <button type="submit" class="btn btn-primary mt-2">Обновить</button>
          </form>
        </td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

