/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import configuration.ConnectionBD;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.BecarioModel;

/**
 *
 * @author Dell
 */
@WebServlet(name = "BecarioServlet", urlPatterns = {"/becarioServlet"})
public class BecarioServlet extends HttpServlet {
    
    Connection conn;
    PreparedStatement ps;
    Statement statement;
    ResultSet rs;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet BecarioServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet BecarioServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    private Date extractFechaNacimientoFromCURP(String curp) throws ParseException {
        // Los primeros 6 caracteres de la CURP corresponden a la fecha de nacimiento en formato AAMMDD
        String fechaCURP = curp.substring(4, 10);

        // Formato de la CURP: AAMMDD -> Año, Mes, Día
        String anio = fechaCURP.substring(0, 2);
        String mes = fechaCURP.substring(2, 4);
        String dia = fechaCURP.substring(4, 6);

        // Determinamos si el año es del siglo 1900 o 2000
        int anioInt = Integer.parseInt(anio);
        if (anioInt >= 0 && anioInt <= 23) { // Ejemplo para años 2000 a 2023
            anio = "20" + anio;
        } else {
            anio = "19" + anio; // Para años 1900 a 1999
        }

        // Formato final: yyyy-MM-dd
        String fechaNacimientoString = anio + "-" + mes + "-" + dia;

        // Convertimos la cadena de fecha a tipo java.sql.Date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date fechaNacimientoUtil = sdf.parse(fechaNacimientoString);
        return new Date(fechaNacimientoUtil.getTime());
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        ConnectionBD conexion = new ConnectionBD();
        List<BecarioModel> listaBecarios = new ArrayList<>();
        String sql = "SELECT curp, apellido_paterno, apellido_materno, nombre, genero, password, fecha_nacimiento FROM becario";

        try {
            conn = conexion.getConnectionBD();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            // Itera sobre los resultados y crea objetos UsuarioModel
            while (rs.next()) {
                BecarioModel becario = new BecarioModel();
                becario.setCurp(rs.getString("curp"));
                becario.setApellido_paterno(rs.getString("apellido_paterno"));
                becario.setApellido_materno(rs.getString("apellido_materno"));
                becario.setNombre(rs.getString("nombre"));
                becario.setGenero(rs.getString("genero"));
                becario.setPassword(rs.getString("password"));
                becario.setFecha_nacimiento(rs.getString("fecha_nacimiento"));
                listaBecarios.add(becario);
                
                System.out.println("APELLIDO PATERNO "+ rs.getString("apellido_paterno"));
                System.out.println("APELLIDO MATERNO "+ rs.getString("apellido_materno"));
            }

            // Pasa la lista de usuarios al JSP
            request.setAttribute("becarios", listaBecarios);

            request.getRequestDispatcher("/views/tabla_becarios.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al obtener los becarios" + e);
        } finally {
            // Close resources
            // Close resources
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String curp = request.getParameter("curp");
        String apellidoPaterno = request.getParameter("apellido_paterno");
        String apellidoMaterno = request.getParameter("apellido_materno");
        String nombre = request.getParameter("nombre");
        String genero = request.getParameter("genero");
        String password = request.getParameter("password");
        
        Date fechaNacimiento = null;
        try {
            fechaNacimiento = extractFechaNacimientoFromCURP(curp);
        } catch (ParseException e) {
            e.printStackTrace();
            response.getWriter().println("Error al extraer la fecha de nacimiento de la CURP.");
            return;
        }
       
        ConnectionBD conexionBD = new ConnectionBD();
        Connection conn = null;

        try {
            conn = conexionBD.getConnectionBD();  // Obtener la conexión a la base de datos

            // Consulta SQL para insertar los datos
            String sql = "INSERT INTO becario (curp, apellido_paterno, apellido_materno, nombre, genero, password, fecha_nacimiento) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, curp);
            statement.setString(2, apellidoPaterno);
            statement.setString(3, apellidoMaterno);
            statement.setString(4, nombre);
            statement.setString(5, genero);
            statement.setString(6, password);
            statement.setDate(7, fechaNacimiento);

            // Ejecutar la consulta
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                response.sendRedirect(request.getContextPath() + "/becarioServlet");
            } else {
                response.getWriter().println("Hubo un error en el registro.");
            }

        } catch (SQLException e) {
            throw new ServletException("Error al conectar a la base de datos", e);
        } finally {
            try {
                if (conn != null) {
                    conn.close(); // Cerrar la conexión a la base de datos
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
