package agricultura;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DispositivoDAO {

    private static final String URL  = "jdbc:postgresql://localhost:5432/juadelavi";
    private static final String USER = "juadelavi";
    private static final String PASS = "2002";

    public DispositivoDAO() {
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public List<Dispositivo> getAll() {
        String sql = "SELECT * FROM dispositivos";
        List<Dispositivo> resultado = new ArrayList<>();

        try {
            Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Dispositivo d = new Dispositivo();
                d.setId(rs.getInt("id"));
                d.setParcelaId(rs.getInt("parcela_id"));
                d.setUrn(rs.getString("urn"));
                d.setTipo(rs.getString("tipo"));
                d.setDescripcion(rs.getString("descripcion"));
                resultado.add(d);
            }

            rs.close();
            ps.close();
            con.close();

        } catch (SQLException e) {
            System.out.println("Error consultando dispositivos: " + e.getMessage());
        }

        return resultado;
    }

    public Dispositivo getById(int id) {
        String sql = "SELECT * FROM dispositivos WHERE id = ?";
        Dispositivo dispositivo = null;

        try {
            Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                dispositivo = new Dispositivo();
                dispositivo.setId(rs.getInt("id"));
                dispositivo.setParcelaId(rs.getInt("parcela_id"));
                dispositivo.setUrn(rs.getString("urn"));
                dispositivo.setTipo(rs.getString("tipo"));
                dispositivo.setDescripcion(rs.getString("descripcion"));
            }

            rs.close();
            ps.close();
            con.close();

        } catch (SQLException e) {
            System.out.println("Error consultando dispositivo: " + e.getMessage());
        }

        return dispositivo;
    }

    public boolean insert(Dispositivo d) {
        String sql = "INSERT INTO dispositivos (parcela_id, urn, tipo, descripcion) VALUES (?, ?, ?, ?)";

        try {
            Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, d.getParcelaId());
            ps.setString(2, d.getUrn());
            ps.setString(3, d.getTipo());
            ps.setString(4, d.getDescripcion());

            int filas = ps.executeUpdate();
            ps.close();
            con.close();

            return filas > 0;

        } catch (SQLException e) {
            System.out.println("Error insertando dispositivo: " + e.getMessage());
            return false;
        }
    }

    public boolean update(int id, Dispositivo d) {
        String sql = "UPDATE dispositivos SET parcela_id = ?, urn = ?, tipo = ?, descripcion = ? WHERE id = ?";

        try {
            Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, d.getParcelaId());
            ps.setString(2, d.getUrn());
            ps.setString(3, d.getTipo());
            ps.setString(4, d.getDescripcion());
            ps.setInt(5, id);

            int filas = ps.executeUpdate();
            ps.close();
            con.close();

            return filas > 0;

        } catch (SQLException e) {
            System.out.println("Error actualizando dispositivo: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM dispositivos WHERE id = ?";

        try {
            Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            int filas = ps.executeUpdate();
            ps.close();
            con.close();

            return filas > 0;

        } catch (SQLException e) {
            System.out.println("Error eliminando dispositivo: " + e.getMessage());
            return false;
        }
    }
}
