<%@ page import="model.Person" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%
    String nom = request.getParameter("nom"); // récupérer le nom du formulaire
    Person p = new Person(nom); // créer l'objet Java
%>
<!DOCTYPE html>
<html>
<head>
    <title>Résultat</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
    <h1>Résultat :</h1>
    <p>Nom : <%= p.getNom() %></p>
    <p>Message : <%= p.saluer() %></p>

    <a href="index.jsp">Retour</a>
</body>
</html>
