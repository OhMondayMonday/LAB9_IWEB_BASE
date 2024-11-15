package com.example.lab9_base.Dao;

import com.example.lab9_base.Bean.Arbitro;
import java.sql.*;
import java.util.ArrayList;

public class DaoArbitros extends DaoBase {

    public ArrayList<Arbitro> listarArbitros() {
        ArrayList<Arbitro> arbitros = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM arbitro");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Arbitro arbitro = new Arbitro();
                arbitro.setIdArbitro(rs.getInt("idArbitro"));
                arbitro.setNombre(rs.getString("nombre"));
                arbitro.setPais(rs.getString("pais"));
                arbitros.add(arbitro);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return arbitros;
    }

    public void crearArbitro(Arbitro arbitro) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO arbitro (nombre, pais) VALUES (?, ?)")) {
            stmt.setString(1, arbitro.getNombre());
            stmt.setString(2, arbitro.getPais());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Arbitro> busquedaNombreOPais(String termino) {
        ArrayList<Arbitro> arbitros = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM arbitro WHERE nombre LIKE ? OR pais LIKE ?")) {
            String terminoBusqueda = "%" + termino + "%";
            stmt.setString(1, terminoBusqueda);
            stmt.setString(2, terminoBusqueda);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Arbitro arbitro = new Arbitro();
                arbitro.setIdArbitro(rs.getInt("idArbitro"));
                arbitro.setNombre(rs.getString("nombre"));
                arbitro.setPais(rs.getString("pais"));
                arbitros.add(arbitro);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return arbitros;
    }

    public boolean existeArbitroPorNombre(String nombre) {
        boolean existe = false;
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM arbitro WHERE nombre = ?")) {
            stmt.setString(1, nombre);
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


    public void borrarArbitro(int id) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM arbitro WHERE idArbitro = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

