<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Регистрация</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body class="bg-light">
<nav class="navbar navbar-expand-lg navbar-dark bg-dark mb-4">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/">
            <strong>КиноАдмин</strong>
        </a>
    </div>
</nav>
<div class="container my-5">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <div class="card shadow">
                <div class="card-header text-center bg-primary text-white">
                    <h3>Регистрация</h3>
                </div>
                <div class="card-body">
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger text-center">${error}</div>
                    </c:if>
                    <form method="post" action="${pageContext.request.contextPath}/register" onsubmit="return validatePassword()">
                        <div class="mb-3">
                            <label for="username" class="form-label">Имя пользователя</label>
                            <input type="text" id="username" name="username" class="form-control" required>
                        </div>
                        <div class="mb-3">
                            <label for="email" class="form-label">Электронная почта</label>
                            <input type="email" id="email" name="email" class="form-control" required>
                        </div>
                        <div class="mb-3">
                            <label for="password" class="form-label">Пароль</label>
                            <input type="password" id="password" name="password" class="form-control" required>
                            <small id="passwordHelp" class="form-text text-muted">
                                Пароль должен содержать минимум 8 символов, включать заглавные буквы, цифры и специальные символы.
                            </small>
                        </div>
                        <div id="passwordError" class="alert alert-danger d-none" role="alert">
                            Пароль не соответствует требованиям безопасности.
                        </div>
                        <button type="submit" class="btn btn-primary w-100">Зарегистрироваться</button>
                    </form>
                </div>
                <div class="card-footer text-center">
                    <a href="${pageContext.request.contextPath}/login" class="text-muted">Уже есть аккаунт? Войти</a>
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
