<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Registration Completed</title>
    <link rel="stylesheet" href="/guestbook/css/general.css">
</head>
<body>
    <div>
        <h1>Ввод нового пароля</h1>
            <div class="form_container">
                <form id="repair" action="/guestbook/reset_password" method="POST">
                    <div class="form_element" hidden=true>
                        <label for="token">Токен из письма электронной почты:</label>
                        <br/>
                        <input type="text" name="token" id="token" value=${token} />
                    </div>
                    <div class="form_element" hidden=true>
                        <label for="email">Адрес электронной почты:</label>
                        <br/>
                        <input type="email" name="email" id="email" value=${email} />
                    </div>
                    <div class="form_element">
                        <label for="password1">Введите новый пароль:</label>
                        <br/>
                        <input type="password" name="password1" id="password1" placeholder="Пароль"/>
                    </div>
                    <div class="form_element">
                        <label for="password2">Повторите новый пароль:</label>
                        <br/>
                        <input type="password" name="password2" id="password2" placeholder="Введите ещё раз"/>
                        <br/>
                    </div>
                    <input type="submit" value="Восстановить пароль" id="button">
                </form>
            </div>
    </div>
</body>
</html>