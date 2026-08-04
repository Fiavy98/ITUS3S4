<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Formulaire de salutation</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
    <h1>Entrez votre nom</h1>
    <form action="result.jsp" method="post">
        <input type="text" name="nom" placeholder="Votre nom" required>
        <button type="submit">Envoyer</button>
    </form>
</body>
</html>
