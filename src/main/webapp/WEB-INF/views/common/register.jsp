<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<fmt:setLocale value="${lang}" />
<fmt:setBundle basename="messages"/>
<!DOCTYPE html>
<html lang="${lang}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><fmt:message key="register.title"/></title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body class="bg-light">
<nav class="navbar navbar-expand-lg navbar-dark bg-dark mb-4">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/">
            <strong><fmt:message key="nav.title.default"/></strong>
        </a>
    </div>
    <div class="ml-auto">
        <a href="?lang=en" class="text-white">EN</a>
        <a href="?lang=ru" class="text-white">RU</a>
        <a href="?lang=by" class="text-white">BY</a>
    </div>
</nav>
<div class="container my-5">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <div class="card shadow">
                <div class="card-header text-center bg-primary text-white">
                    <h3><fmt:message key="register.title"/></h3>
                </div>
                <div class="card-body">
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger text-center"><fmt:message key="${error}"/></div>
                    </c:if>
                    <form method="post" action="${pageContext.request.contextPath}/register" onsubmit="return validatePassword()">
                        <div class="mb-3">
                            <label for="username" class="form-label"><fmt:message key="register.username"/></label>
                            <input type="text" id="username" name="username" class="form-control" required>
                        </div>
                        <div class="mb-3">
                            <label for="email" class="form-label"><fmt:message key="register.email"/></label>
                            <input type="email" id="email" name="email" class="form-control" required>
                        </div>
                        <div class="mb-3">
                            <label for="password" class="form-label"><fmt:message key="register.password"/></label>
                            <input type="password" id="password" name="password" class="form-control" required>
                            <small id="passwordHelp" class="form-text text-muted">
                                <fmt:message key="register.passwordHelp"/>
                            </small>
                        </div>
                        <div id="passwordError" class="alert alert-danger d-none" role="alert">
                            <fmt:message key="register.passwordError"/>
                        </div>
                        <button type="submit" class="btn btn-primary w-100"><fmt:message key="register.submit"/></button>
                    </form>
                </div>
                <div class="card-footer text-center">
                    <a href="${pageContext.request.contextPath}/" class="text-muted"><fmt:message key="register.loginLink"/></a>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function validatePassword() {
        const password = document.getElementById('password').value;
        const passwordError = document.getElementById('passwordError');

        const passwordRegex = /^(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,}$/;
        if (!passwordRegex.test(password)) {
            passwordError.classList.remove('d-none');
            return false;
        }
        passwordError.classList.add('d-none');
        return true;
    }
</script>
</body>
</html>
