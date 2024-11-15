package com.example.lab9_base.Dao;

import com.example.lab9_base.Bean.Seleccion;
import com.example.lab9_base.Bean.Estadio;
import java.sql.*;
import java.util.ArrayList;

public class DaoSelecciones extends DaoBase {

    public ArrayList<Seleccion> listarSelecciones() {
        ArrayList<Seleccion> selecciones = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM seleccion");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Seleccion seleccion = new Seleccion();
                seleccion.setIdSeleccion(rs.getInt("idSeleccion"));
                seleccion.setNombre(rs.getString("nombre"));
                seleccion.setTecnico(rs.getString("tecnico"));

                // Asignar el estadio, que se obtiene con su ID
                int estadioId = rs.getInt("estadio_idEstadio");
                seleccion.setEstadio(obtenerEstadioPorId(estadioId));

                selecciones.add(seleccion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return selecciones;
    }

    private Estadio obtenerEstadioPorId(int id) {
        Estadio estadio = null;
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM estadio WHERE idEstadio = ?")) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                estadio = new Estadio();
                estadio.setIdEstadio(rs.getInt("idEstadio"));
                estadio.setNombre(rs.getString("nombre"));
                estadio.setProvincia(rs.getString("provincia"));
                estadio.setClub(rs.getString("club"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return estadio;
    }

    public Seleccion obtenerSeleccionPorId(int id) {
        Seleccion seleccion = null;
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM seleccion WHERE idSeleccion = ?")) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                seleccion = new Seleccion();
                seleccion.setIdSeleccion(rs.getInt("idSeleccion"));
                seleccion.setNombre(rs.getString("nombre"));
                seleccion.setTecnico(rs.getString("tecnico"));

                // Asignar el estadio con su ID
                int estadioId = rs.getInt("estadio_idEstadio");
                seleccion.setEstadio(obtenerEstadioPorId(estadioId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seleccion;
    }
}

