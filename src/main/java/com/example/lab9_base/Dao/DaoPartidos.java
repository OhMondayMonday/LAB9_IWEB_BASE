package com.example.lab9_base.Dao;

import com.example.lab9_base.Bean.*;
import java.sql.*;
import java.util.ArrayList;

public class DaoPartidos extends DaoBase {

    public ArrayList<Partido> listaDePartidos() {
        ArrayList<Partido> partidos = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT p.idPartido, p.fecha, p.numeroJornada, " +
                             "s1.idSeleccion AS idSeleccionLocal, s1.nombre AS nombreLocal, s1.tecnico AS tecnicoLocal, " +
                             "e1.idEstadio, e1.nombre AS nombreEstadio, e1.provincia AS provinciaEstadio, e1.club AS clubEstadio, " +
                             "s2.idSeleccion AS idSeleccionVisitante, s2.nombre AS nombreVisitante, s2.tecnico AS tecnicoVisitante, " +
                             "a.idArbitro, a.nombre AS nombreArbitro, a.pais AS paisArbitro " +
                             "FROM partido p " +
                             "JOIN seleccion s1 ON p.seleccionLocal = s1.idSeleccion " +
                             "JOIN estadio e1 ON s1.estadio_idEstadio = e1.idEstadio " +
                             "JOIN seleccion s2 ON p.seleccionVisitante = s2.idSeleccion " +
                             "JOIN arbitro a ON p.arbitro = a.idArbitro"
             );
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Partido partido = new Partido();
                partido.setIdPartido(rs.getInt("idPartido"));
                partido.setFecha(rs.getString("fecha"));
                partido.setNumeroJornada(rs.getInt("numeroJornada"));

                Seleccion seleccionLocal = new Seleccion();
                seleccionLocal.setIdSeleccion(rs.getInt("idSeleccionLocal"));
                seleccionLocal.setNombre(rs.getString("nombreLocal"));
                seleccionLocal.setTecnico(rs.getString("tecnicoLocal"));

                Estadio estadio = new Estadio();
                estadio.setIdEstadio(rs.getInt("idEstadio"));
                estadio.setNombre(rs.getString("nombreEstadio"));
                estadio.setProvincia(rs.getString("provinciaEstadio"));
                estadio.setClub(rs.getString("clubEstadio"));
                seleccionLocal.setEstadio(estadio);

                partido.setSeleccionLocal(seleccionLocal);

                Seleccion seleccionVisitante = new Seleccion();
                seleccionVisitante.setIdSeleccion(rs.getInt("idSeleccionVisitante"));
                seleccionVisitante.setNombre(rs.getString("nombreVisitante"));
                seleccionVisitante.setTecnico(rs.getString("tecnicoVisitante"));
                partido.setSeleccionVisitante(seleccionVisitante);

                Arbitro arbitro = new Arbitro();
                arbitro.setIdArbitro(rs.getInt("idArbitro"));
                arbitro.setNombre(rs.getString("nombreArbitro"));
                arbitro.setPais(rs.getString("paisArbitro"));
                partido.setArbitro(arbitro);

                partidos.add(partido);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return partidos;
    }



    public void crearPartido(Partido partido) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO partido (fecha, numeroJornada, seleccionLocal, seleccionVisitante, arbitro) VALUES (?, ?, ?, ?, ?)")) {

            stmt.setString(1, partido.getFecha());
            stmt.setInt(2, partido.getNumeroJornada());
            stmt.setInt(3, partido.getSeleccionLocal().getIdSeleccion());
            stmt.setInt(4, partido.getSeleccionVisitante().getIdSeleccion());
            stmt.setInt(5, partido.getArbitro().getIdArbitro());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean existePartido(String fecha, int numeroJornada, int seleccionLocalId, int seleccionVisitanteId) {
        boolean existe = false;
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT COUNT(*) FROM partido WHERE fecha = ? AND numeroJornada = ? AND seleccionLocal = ? AND seleccionVisitante = ?")) {
            stmt.setString(1, fecha);
            stmt.setInt(2, numeroJornada);
            stmt.setInt(3, seleccionLocalId);
            stmt.setInt(4, seleccionVisitanteId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    existe = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return existe;
    }

}

