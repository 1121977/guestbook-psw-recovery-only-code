<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Please Log In</title>
        <link rel="stylesheet" href="/guestbook/css/msg.css">
</head>
<body display="flex" justify-content="center" align-items="center" flex-direction="column">

    <h1>Необходимо ввести login/password</h1>
    <div class="form_container">
        <form id=login action="/guestbook/login" method="POST">
            <div class="form_element">
                <label for="username">Имя пользователя:</label>
                <br/>
                <input type="text" name="username" id="username" placeholder="Username"/>
            </div>
            <div class="form_element">
                <label for="password">Имя пользователя:</label>
                <br/>
                <input type="password" name="password" id="password" placeholder="Password"/>
            </div>
            <input type="submit" value="Submit">
        </form>
    </div>
</body>
</html>