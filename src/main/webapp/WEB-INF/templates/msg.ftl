<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Message To</title>
    <link rel="stylesheet" href="/guestbook/css/general.css">
</head>
<body>
    <h1>Сообщение для ${recipient}</h1>
    <div class="form_container">
        <form id=sendMessageForm action="/guestbook/sendto" method="POST" modelAttribute="note">
            <div class="form_element">
                <label for="message">Сообщение:</label>
                <br />
                <textarea id="message" name="message"></textarea>
                <input type="hidden" id="recipient" name="recipient" value="${recipient}" />
            </div>
            <input type="submit" value="Submit">
        </form>
    </div>
</body>
</html>