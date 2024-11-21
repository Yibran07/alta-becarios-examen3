<%-- 
    Document   : tabla_becarios
    Created on : 13-nov-2024, 19:25:25
    Author     : Dell
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.BecarioModel" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Lista de Becarios</title>
</head>
<body>
    <h1>Lista de Becarios</h1>

    <%
        // Obtener la lista de becarios desde el atributo de la solicitud
        List<BecarioModel> becarios = (List<BecarioModel>) request.getAttribute("becarios");

        // Verificar si hay datos
        if (becarios != null && !becarios.isEmpty()) {
    %>
        <table border="1">
            <tr>
                <th>CURP</th>
                <th>Nombre</th>
                <th>Acciones</th>
            </tr>
            <% 
                // Iterar sobre la lista de becarios
                for (BecarioModel becario : becarios) {
            %>
                <tr>
                    <td><%= becario.getCurp() %></td>
                    <td><%= becario.getNombre() %></td>
                    <td>
                        <!-- Botón para ver XML -->
                        <form action="verXmlServlet" method="get" style="display:inline;">
                            <input type="hidden" name="curp" value="<%= becario.getCurp() %>" />
                            <input type="submit" value="Ver XML" />
                        </form>

                        <!-- Botón para ver Beans -->
                        <form action="views/ver_bean.jsp" method="post" style="display:inline;">
                            <input type="hidden" name="curp" value="<%= becario.getCurp() %>" />
                            <input type="hidden" name="nombre" value="<%= becario.getNombre() %>" />
                            <input type="hidden" name="apellidoPaterno" value="<%=  becario.getApellido_paterno() %>" />
                            <input type="hidden" name="apellidoMaterno" value="<%= becario.getApellido_materno() %>" />
                            <input type="hidden" name="genero" value="<%= becario.getGenero() %>" />
                            <input type="hidden" name="password" value="<%= becario.getPassword() %>" />
                            <input type="hidden" name="fechaNacimiento" value="<%= becario.getFecha_nacimiento() %>" />
                            <input type="submit" value="Ver Beans" />
                        </form>
                    </td>
                </tr>
            <% 
                }
            %>
        </table>
    <%
        } else {
            out.println("<p>No hay registros disponibles.</p>");
        }
    %>
</body>
</html>
