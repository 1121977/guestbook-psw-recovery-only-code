<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Guest Book</title>
    <link rel="stylesheet" href="/guestbook/css/index.css">
    <#setting locale="en_US">
</head>
<body>
    <h1>Гостевая книга УЭК</h1>
    <table border=2 class="gb_table">
        <#list notes as note>
            <tr>
                <td>
                    ${note.message}
                </td>
                <td>
                    <table>
                        <tr>
                            <td>
                                ${note.userName}
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
                <label for="message">Message:</label>
                <textarea id="message" name="message"></textarea>
                <!--input type="text" id="message" name="message"-->
            </div>
            <input type="submit" value="Submit">
        </form>
    </div>
</body>
</html>