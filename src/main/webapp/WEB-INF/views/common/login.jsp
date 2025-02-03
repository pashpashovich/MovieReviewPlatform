<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page isELIgnored="false" %>
<fmt:setLocale value="${lang}" />
<fmt:setBundle basename="messages" />
<!DOCTYPE html>
<html lang="${lang}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><fmt:message key="login.title"/></title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body class="bg-light">
<nav class="navbar navbar-expand-lg navbar-dark bg-dark mb-4">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/">
            <strong><fmt:message key="nav.title.default"/></strong>
        </a>
        <div class="ml-auto">
            <a href="?lang=en" class="text-white">EN</a>
            <a href="?lang=ru" class="text-white">RU</a>
            <a href="?lang=by" class="text-white">BY</a>
        </div>
    </div>
</nav>
<div class="container my-5">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <div class="card shadow">
                <div class="card-header text-center bg-primary text-white">
                    <h3><fmt:message key="login.title"/></h3>
                </div>
                <div class="card-body">
                    <c:if test="${error != null}">
                        <div class="alert alert-danger text-center">${error}</div>
                    </c:if>
                    <form:form method="post" action="${pageContext.request.contextPath}/login">
                        <div class="mb-3">
                            <label for="email" class="form-label"><fmt:message key="login.email"/></label>
                            <input type="email" value="${username}" id="email" name="username" class="form-control" required>
                        </div>
                        <div class="mb-3">
                            <label for="password" class="form-label"><fmt:message key="login.password"/></label>
                            <input type="password" value="${password}" id="password" name="password"
                                   class="form-control" required>
                        </div>
                        <button type="submit" class="btn btn-primary w-100"><fmt:message key="login.submit"/></button>
                    </form:form>
                </div>
                <div class="card-footer text-center">
                    <a href="${pageContext.request.contextPath}/register" class="text-muted"><fmt:message
                            key="login.register"/></a>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
