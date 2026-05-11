package agricultura;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParcelaDAO {

    private static final String URL  = "jdbc:postgresql://localhost:5432/juadelavi";
    private static final String USER = "juadelavi";
    private static final String PASS = "2002";

    public ParcelaDAO() {
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public List<Parcela> getAll() {
        String sql = "SELECT * FROM parcelas";
        List<Parcela> resultado = new ArrayList<>();

        try {
            Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Parcela p = new Parcela();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setUbicacion(rs.getString("ubicacion"));
                resultado.add(p);
            }

            rs.close();
            ps.close();
            con.close();

        } catch (SQLException e) {
            System.out.println("Error consultando parcelas: " + e.getMessage());
        }

        return resultado;
    }

    public Parcela getById(int id) {
        String sql = "SELECT * FROM parcelas WHERE id = ?";
        Parcela parcela = null;

        try {
            Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                parcela = new Parcela();
                parcela.setId(rs.getInt("id"));
                parcela.setNombre(rs.getString("nombre"));
                parcela.setUbicacion(rs.getString("ubicacion"));
            }

            rs.close();
            ps.close();
            con.close();

        } catch (SQLException e) {
            System.out.println("Error consultando parcela: " + e.getMessage());
        }

        return parcela;
    }

    public boolean insert(Parcela p) {
        String sql = "INSERT INTO parcelas (nombre, ubicacion) VALUES (?, ?)";

        try {
            Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getUbicacion());

            int filas = ps.executeUpdate();
            ps.close();
            con.close();

            return filas > 0;

        } catch (SQLException e) {
            System.out.println("Error insertando parcela: " + e.getMessage());
            return false;
        }
    }

    public boolean update(int id, Parcela p) {
        String sql = "UPDATE parcelas SET nombre = ?, ubicacion = ? WHERE id = ?";

        try {
            Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getUbicacion());
            ps.setInt(3, id);

            int filas = ps.executeUpdate();
            ps.close();
            con.close();

            return filas > 0;

        } catch (SQLException e) {
            System.out.println("Error actualizando parcela: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM parcelas WHERE id = ?";

        try {
            Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            int filas = ps.executeUpdate();
            ps.close();
            con.close();

            return filas > 0;

        } catch (SQLException e) {
            System.out.println("Error eliminando parcela: " + e.getMessage());
            return false;
        }
    }
}
