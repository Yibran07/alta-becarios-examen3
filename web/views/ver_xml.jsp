<%-- 
    Document   : ver_xml
    Created on : 13-nov-2024, 19:30:55
    Author     : Dell
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.BecarioModel"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Ver XML - Becario</title>
</head>
<body>
    <h1>Detalles del Becario</h1>

    <% 
        // Obtener el objeto BecarioModel desde la solicitud
        BecarioModel becario = (BecarioModel) request.getAttribute("becario");
    %>

    <table border="1">
        <tr>
            <th>CURP</th>
            <td><%= becario.getCurp() %></td>
        </tr>
        <tr>
            <th>Nombre</th>
            <td><%= becario.getNombre() %></td>
        </tr>
        <tr>
            <th>Apellido Paterno</th>
            <td><%= becario.getApellido_paterno() %></td>
        </tr>
        <tr>
            <th>Apellido Materno</th>
            <td><%= becario.getApellido_materno() %></td>
        </tr>
        <tr>
            <th>Género</th>
            <td><%= becario.getGenero() %></td>
        </tr>
        <tr>
            <th>Password</th>
            <td><%= becario.getPassword() %></td>
        </tr>
        <tr>
            <th>Fecha de Nacimiento</th>
            <td><%= becario.getFecha_nacimiento() %></td>
        </tr>
    </table>

    <!-- Botón para descargar el XML -->
    <form action="${pageContext.request.contextPath}/verXmlServlet" method="get">
        <input type="hidden" name="curp" value="<%= becario.getCurp() %>">
        <input type="hidden" name="download" value="true">
        <input type="submit" value="Descargar XML">
    </form>
        
    <button onclick="window.history.back();">Regresar</button>

</body>
</html>

