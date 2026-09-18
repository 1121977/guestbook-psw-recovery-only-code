<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Guest Book</title>
    <link rel="stylesheet" href="/guestbook/css/index.css">
    <#setting locale="en_US">
</head>
<body>
    <header>
        <div></div>
        <h1>Гостевая книга УЭК</h1>
        <form action="/guestbook/logout" method="POST">
            <button type="submit">Выйти</button>
        </form>
   </header>
    <h2 id="hello_string">Привет, ${firstname}!</h2>
    <table border=2 class="gb_table">
        <#list notes as note>
            <tr <#if note.recipient??>class="private_note"</#if>>
                <td>
                    ${note.message}
                </td>
                <td>
                    <table>
                        <tr>
                            <td>
                                ${note.userName}
                            </td>
                            <td>
                                <a href="/guestbook/msg?to=${note.userName}">Отправить личное сообщение</a>
                            </td>
                        </tr>
                         <tr>
                            <td>
                                ${note.noteDateTime.format('MMM dd, yyyy, HH:mm:ss')}
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </#list>
    </table>
    <div class="form_container">
        <form id=sendMessageForm action="/guestbook/save" method="POST" modelAttribute="note">
            <div class="form_element">
                <label for="message">Сообщение:</label>
                <textarea id="message" name="message"></textarea>
            </div>
            <input type="submit" value="Отправить">
        </form>
    </div>
</body>
</html>