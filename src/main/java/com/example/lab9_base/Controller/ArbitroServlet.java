package com.example.lab9_base.Controller;

import com.example.lab9_base.Dao.DaoArbitros;
import com.example.lab9_base.Bean.Arbitro;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "ArbitroServlet", urlPatterns = {"/ArbitroServlet"})
public class ArbitroServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");
        DaoArbitros daoArbitros = new DaoArbitros();
        RequestDispatcher view;

        switch (action) {
            case "crear":
                view = request.getRequestDispatcher("/arbitros/form.jsp");
                view.forward(request, response);
                break;

            case "borrar":
                try {
                    int id = Integer.parseInt(request.getParameter("id"));
                    daoArbitros.borrarArbitro(id);
                    response.sendRedirect("ArbitroServlet?action=lista");
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    response.sendRedirect("ArbitroServlet?action=lista");
                }
                break;

            case "lista":
                ArrayList<Arbitro> arbitros;
                String terminoBusqueda = request.getParameter("buscar");

                if (terminoBusqueda != null && !terminoBusqueda.trim().isEmpty()) {
                    terminoBusqueda = terminoBusqueda.trim();
                    arbitros = daoArbitros.busquedaNombreOPais(terminoBusqueda);
                    if (arbitros.isEmpty()) {
                        request.setAttribute("mensaje", "No se encontraron árbitros con los criterios proporcionados.");
                    }
                } else {
                    arbitros = daoArbitros.listarArbitros();
                }

                request.setAttribute("arbitros", arbitros);
                view = request.getRequestDispatcher("/arbitros/list.jsp");
                view.forward(request, response);
                break;

            default:
                response.sendRedirect("ArbitroServlet?action=lista");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "guardar" : request.getParameter("action");
        DaoArbitros daoArbitros = new DaoArbitros();
        RequestDispatcher view;

        switch (action) {
            case "guardar":
                try {
                    String nombre = request.getParameter("nombre").trim();
                    String pais = request.getParameter("pais").trim();

                    if (nombre.isEmpty() || pais.isEmpty()) {
                        request.setAttribute("error", "Ningún campo debe estar vacío.");
                        view = request.getRequestDispatcher("/arbitros/form.jsp");
                        view.forward(request, response);
                        return;
                    }

                    if (daoArbitros.existeArbitroPorNombre(nombre)) {
                        request.setAttribute("error", "El nombre del árbitro ya existe en la base de datos.");
                        view = request.getRequestDispatcher("/arbitros/form.jsp");
                        view.forward(request, response);
                        return;
                    }

                    Arbitro arbitro = new Arbitro();
                    arbitro.setNombre(nombre);
                    arbitro.setPais(pais);

                    daoArbitros.crearArbitro(arbitro);
                    response.sendRedirect("ArbitroServlet?action=lista");

                } catch (Exception e) {
                    e.printStackTrace();
                    request.setAttribute("error", "Ocurrió un error al guardar el árbitro. Inténtalo de nuevo.");
                    view = request.getRequestDispatcher("/arbitros/form.jsp");
                    view.forward(request, response);
                }
                break;

            default:
                response.sendRedirect("ArbitroServlet?action=lista");
                break;
        }
    }
}
