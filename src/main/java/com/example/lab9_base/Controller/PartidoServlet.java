package com.example.lab9_base.Controller;

import com.example.lab9_base.Dao.*;
import com.example.lab9_base.Bean.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "PartidoServlet", urlPatterns = {"/PartidoServlet", ""})
public class PartidoServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "guardar" : request.getParameter("action");
        DaoPartidos daoPartidos = new DaoPartidos();
        DaoSelecciones daoSelecciones = new DaoSelecciones(); // Instancia para obtener selecciones
        RequestDispatcher view;

        switch (action) {
            case "guardar":
                try {
                    String fecha = request.getParameter("fecha").trim();
                    String numeroJornadaStr = request.getParameter("numeroJornada").trim();
                    int seleccionLocalId = Integer.parseInt(request.getParameter("seleccionLocal").trim());
                    int seleccionVisitanteId = Integer.parseInt(request.getParameter("seleccionVisitante").trim());

                    if (fecha.isEmpty() || numeroJornadaStr.isEmpty()) {
                        request.setAttribute("error", "Ningún campo debe estar vacío.");
                        view = request.getRequestDispatcher("/partidos/form.jsp");
                        view.forward(request, response);
                        return;
                    }

                    int numeroJornada = Integer.parseInt(numeroJornadaStr);

                    if (seleccionLocalId == seleccionVisitanteId) {
                        request.setAttribute("error", "La selección visitante no puede ser igual que la selección local.");
                        view = request.getRequestDispatcher("/partidos/form.jsp");
                        view.forward(request, response);
                        return;
                    }

                    Seleccion seleccionLocal = daoSelecciones.obtenerSeleccionPorId(seleccionLocalId);
                    Seleccion seleccionVisitante = daoSelecciones.obtenerSeleccionPorId(seleccionVisitanteId);

                    if (seleccionLocal == null || seleccionVisitante == null) {
                        request.setAttribute("error", "Una o ambas selecciones no existen.");
                        view = request.getRequestDispatcher("/partidos/form.jsp");
                        view.forward(request, response);
                        return;
                    }

                    if (daoPartidos.existePartido(fecha, numeroJornada, seleccionLocalId, seleccionVisitanteId)) {
                        request.setAttribute("error", "El partido ya existe en la base de datos.");
                        view = request.getRequestDispatcher("/partidos/form.jsp");
                        view.forward(request, response);
                        return;
                    }

                    Partido partido = new Partido();
                    partido.setFecha(fecha);
                    partido.setNumeroJornada(numeroJornada);
                    partido.setSeleccionLocal(seleccionLocal);
                    partido.setSeleccionVisitante(seleccionVisitante);

                    daoPartidos.crearPartido(partido);
                    response.sendRedirect("PartidoServlet?action=lista");

                } catch (NumberFormatException e) {
                    request.setAttribute("error", "Formato de número incorrecto. Por favor, verifica el campo de jornada.");
                    view = request.getRequestDispatcher("/partidos/form.jsp");
                    view.forward(request, response);
                } catch (Exception e) {
                    e.printStackTrace();
                    request.setAttribute("error", "Ocurrió un error al guardar el partido. Inténtalo de nuevo.");
                    view = request.getRequestDispatcher("/partidos/form.jsp");
                    view.forward(request, response);
                }
                break;
            default:
                response.sendRedirect("PartidoServlet?action=lista");
                break;
        }
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");
        DaoPartidos daoPartidos = new DaoPartidos();
        DaoSelecciones daoSelecciones = new DaoSelecciones();
        DaoArbitros daoArbitros = new DaoArbitros();
        RequestDispatcher view;

        switch (action) {
            case "lista":
                ArrayList<Partido> partidos = daoPartidos.listaDePartidos();
                request.setAttribute("partidos", partidos);

                view = request.getRequestDispatcher("/index.jsp");
                view.forward(request, response);
                break;
            case "crear":
                ArrayList<Seleccion> selecciones = daoSelecciones.listarSelecciones();
                ArrayList<Arbitro> arbitros = daoArbitros.listarArbitros();

                request.setAttribute("selecciones", selecciones);
                request.setAttribute("arbitros", arbitros);

                view = request.getRequestDispatcher("/partidos/form.jsp");
                view.forward(request, response);
                break;
            default:
                response.sendRedirect("PartidoServlet?action=lista");
                break;
        }
    }

}

