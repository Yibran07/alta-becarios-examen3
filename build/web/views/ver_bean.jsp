<%-- 
    Document   : ver_bean
    Created on : 13-nov-2024, 19:30:43
    Author     : Dell
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="beans.BecarioBean"%>

<jsp:useBean id="becarioBean" scope="page" class="beans.BecarioBean"/>
<%
    request.setCharacterEncoding("UTF-8");

    becarioBean.setCurp(request.getParameter("curp"));
    becarioBean.setNombre(request.getParameter("nombre"));
    becarioBean.setApellidoPaterno(request.getParameter("apellidoPaterno"));
    becarioBean.setApellidoMaterno(request.getParameter("apellidoMaterno"));
    becarioBean.setGenero(request.getParameter("genero"));
    becarioBean.setPassword(request.getParameter("password"));
    becarioBean.setFechaNacimiento(request.getParameter("fechaNacimiento"));
%>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Detalles del Becario</title>
</head>
<body>
    <h1>Detalles del Becario</h1>

    <%
        
        if (becarioBean != null) {
    %>

    <table border="1">
        <tr>
            <th>CURP</th>
            <td><%= becarioBean.getCurp() %></td>
        </tr>
        <tr>
            <th>Nombre</th>
            <td><%= becarioBean.getNombre() %></td>
        </tr>
        <tr>
            <th>Apellido Paterno</th>
            <td><%= becarioBean.getApellidoPaterno() %></td>
        </tr>
        <tr>
            <th>Apellido Materno</th>
            <td><%= becarioBean.getApellidoMaterno() %></td>
        </tr>
        <tr>
            <th>Género</th>
            <td><%= becarioBean.getGenero() %></td>
        </tr>
        <tr>
            <th>Password</th>
            <td><%= becarioBean.getPassword() %></td>
        </tr>
        <tr>
            <th>Fecha de Nacimiento</th>
            <td><%= becarioBean.getFechaNacimiento() %></td>
        </tr>
    </table>

    <%
        } else {
            out.println("<p>Becario no encontrado.</p>");
        }
    %>
</body>
</html>
