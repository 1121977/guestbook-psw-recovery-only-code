<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Please Log In</title>
        <link rel="stylesheet" href="/css/general.css">
</head>
<body display="flex" justify-content="center" align-items="center" flex-direction="column">

    <h1>Необходимо ввести login/password</h1>
    <div class="form_container">
        <form id="login" action="/login" method="POST">
            <div class="form_element">
                <label for="username">Имя пользователя:</label>
                <br/>
                <input type="text" name="username" id="username" placeholder="Username"/>
            </div>
            <div class="form_element">
                <label for="password">Пароль:</label>
                <br/>
                <input type="password" name="password" id="password" placeholder="Password"/>
            </div>
            <br/>
            <input type="submit" value="Войти">
        </form>
    </div>
    <br/>
    <a href="/static/regform.html">Зарегистрироваться</>
</body>
</html>