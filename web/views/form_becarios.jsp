<%-- 
    Document   : form_becarios
    Created on : 13-nov-2024, 18:53:28
    Author     : Dell
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Alta de Becario</title>
    </head>
    <body>
        <h1>Alta de Becario!</h1>
    <form action="${pageContext.request.contextPath}/becarioServlet" method="post">
        <label for="curp">CURP:</label><br>
        <input type="text" id="curp" name="curp" required><br><br>

        <label for="apellido_paterno">Apellido Paterno:</label><br>
        <input type="text" id="apellido_paterno" name="apellido_paterno" required><br><br>

        <label for="apellido_materno">Apellido Materno:</label><br>
        <input type="text" id="apellido_materno" name="apellido_materno" required><br><br>

        <label for="nombre">Nombre:</label><br>
        <input type="text" id="nombre" name="nombre" required><br><br>

        <label for="genero">Género:</label><br>
        <select id="genero" name="genero" required>
            <option value="Masculino">Masculino</option>
            <option value="Femenino">Femenino</option>
        </select><br><br>

        <label for="password">Contraseña:</label><br>
        <input type="password" id="password" name="password" required><br><br>

        <input type="submit" value="Registrar">
    </form>
    </body>
</html>
