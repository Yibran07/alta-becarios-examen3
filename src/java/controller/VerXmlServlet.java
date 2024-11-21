/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.File;
import configuration.ConnectionBD;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import model.BecarioModel;

/**
 *
 * @author Dell
 */
@WebServlet(name = "VerXmlServlet", urlPatterns = {"/verXmlServlet"})
public class VerXmlServlet extends HttpServlet {
    
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
            out.println("<title>Servlet VerXmlServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet VerXmlServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
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
         String curp = request.getParameter("curp");
         String download = request.getParameter("download");


        // Validación de CURP
        if (curp == null || curp.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "CURP no proporcionado");
            return;
        }

        ConnectionBD conexion = new ConnectionBD();
        BecarioModel becario = null;

        String sql = "SELECT curp, apellido_paterno, apellido_materno, nombre, genero, password, fecha_nacimiento " +
                     "FROM becario WHERE curp = ?";
        
        try (Connection conn = conexion.getConnectionBD();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, curp);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    becario = new BecarioModel();
                    becario.setCurp(rs.getString("curp"));
                    becario.setApellido_paterno(rs.getString("apellido_paterno"));
                    becario.setApellido_materno(rs.getString("apellido_materno"));
                    becario.setNombre(rs.getString("nombre"));
                    becario.setGenero(rs.getString("genero"));
                    becario.setPassword(rs.getString("password"));
                    becario.setFecha_nacimiento(rs.getString("fecha_nacimiento"));
                }
            }

            if (becario == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Becario no encontrado");
                return;
            }
            
            if ("true".equals(download)) {
                generateAndSendXML(response, becario);
            } else {
                request.setAttribute("becario", becario);
                request.getRequestDispatcher("/views/ver_xml.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al obtener los detalles del becario");
        }
    }
    
    private void generateAndSendXML(HttpServletResponse response, BecarioModel becario) throws ParserConfigurationException, TransformerException, IOException {
        // Configuración para generar XML en memoria
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        // Creación del documento XML
        Element root = document.createElement("Becario");
        document.appendChild(root);

        Element curp = document.createElement("CURP");
        curp.appendChild(document.createTextNode(becario.getCurp()));
        root.appendChild(curp);

        Element nombre = document.createElement("Nombre");
        nombre.appendChild(document.createTextNode(becario.getNombre()));
        root.appendChild(nombre);

        Element apellidoPaterno = document.createElement("ApellidoPaterno");
        apellidoPaterno.appendChild(document.createTextNode(becario.getApellido_paterno()));
        root.appendChild(apellidoPaterno);

        Element apellidoMaterno = document.createElement("ApellidoMaterno");
        apellidoMaterno.appendChild(document.createTextNode(becario.getApellido_materno()));
        root.appendChild(apellidoMaterno);

        Element genero = document.createElement("Genero");
        genero.appendChild(document.createTextNode(becario.getGenero()));
        root.appendChild(genero);

        Element password = document.createElement("Password");
        password.appendChild(document.createTextNode(becario.getPassword()));
        root.appendChild(password);

        Element fechaNacimiento = document.createElement("FechaNacimiento");
        fechaNacimiento.appendChild(document.createTextNode(becario.getFecha_nacimiento()));
        root.appendChild(fechaNacimiento);

        // Guardar el archivo XML en disco
        File xmlFile = new File("C://Users/Dell/" + becario.getCurp() + ".xml");
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult fileResult = new StreamResult(xmlFile);
        transformer.transform(source, fileResult);

        // Enviar el XML como respuesta HTTP
        response.setContentType("application/xml; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + becario.getCurp() + ".xml");
        StreamResult httpResult = new StreamResult(response.getWriter());
        transformer.transform(source, httpResult);
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
        processRequest(request, response);
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
