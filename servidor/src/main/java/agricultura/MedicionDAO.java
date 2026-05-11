package agricultura;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicionDAO {

    private static final String URL  = "jdbc:postgresql://localhost:5432/juadelavi";
    private static final String USER = "juadelavi";
    private static final String PASS = "2002";

    public MedicionDAO() {
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public boolean insertarMedicion(Medicion m) {
        String sql = "INSERT INTO mediciones " +
            "(parcela_id, dispositivo_urn, magnitud, unidad, valor_numerico, " +
            "valor_booleano, valor_texto, timestamp_epoch) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, m.getParcelaId());
            ps.setString(2, m.getDispositivoUrn());
            ps.setString(3, m.getMagnitud());
            ps.setString(4, m.getUnidad());

            if (m.getValorNumerico() != null) {
                ps.setDouble(5, m.getValorNumerico());
            } else {
                ps.setNull(5, java.sql.Types.DOUBLE);
            }

            if (m.getValorBooleano() != null) {
                ps.setBoolean(6, m.getValorBooleano());
            } else {
                ps.setNull(6, java.sql.Types.BOOLEAN);
            }

            ps.setString(7, m.getValorTexto());

            if (m.getTimestampEpoch() != null) {
                ps.setDouble(8, m.getTimestampEpoch());
            } else {
                ps.setNull(8, java.sql.Types.DOUBLE);
            }

            int filas = ps.executeUpdate();
            ps.close();
            con.close();

            return filas > 0;

        } catch (SQLException e) {
            System.out.println("Error insertando medicion: " + e.getMessage());
            return false;
        }
    }

    public List<Medicion> getMedicionesByParcela(int parcelaId) {
        String sql = "SELECT * FROM mediciones WHERE parcela_id = ? ORDER BY dispositivo_urn, timestamp_epoch NULLS LAST";
        List<Medicion> resultado = new ArrayList<>();

        try {
            Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, parcelaId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Medicion m = new Medicion();
                m.setId(rs.getInt("id"));
                m.setParcelaId(rs.getInt("parcela_id"));
                m.setDispositivoUrn(rs.getString("dispositivo_urn"));
                m.setMagnitud(rs.getString("magnitud"));
                m.setUnidad(rs.getString("unidad"));
                m.setValorNumerico((Double) rs.getObject("valor_numerico"));
                m.setValorBooleano((Boolean) rs.getObject("valor_booleano"));
                m.setValorTexto(rs.getString("valor_texto"));
                m.setTimestampEpoch((Double) rs.getObject("timestamp_epoch"));
                resultado.add(m);
            }

            rs.close();
            ps.close();
            con.close();

        } catch (SQLException e) {
            System.out.println("Error consultando mediciones: " + e.getMessage());
        }

        return resultado;
    }

    public List<Medicion> getMedicionesByDispositivo(String urn) {
        String sql = "SELECT * FROM mediciones WHERE dispositivo_urn = ? ORDER BY dispositivo_urn, timestamp_epoch NULLS LAST";
        List<Medicion> resultado = new ArrayList<>();

        try {
            Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, urn);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Medicion m = new Medicion();
                m.setId(rs.getInt("id"));
                m.setParcelaId(rs.getInt("parcela_id"));
                m.setDispositivoUrn(rs.getString("dispositivo_urn"));
                m.setMagnitud(rs.getString("magnitud"));
                m.setUnidad(rs.getString("unidad"));
                m.setValorNumerico((Double) rs.getObject("valor_numerico"));
                m.setValorBooleano((Boolean) rs.getObject("valor_booleano"));
                m.setValorTexto(rs.getString("valor_texto"));
                m.setTimestampEpoch((Double) rs.getObject("timestamp_epoch"));
                resultado.add(m);
            }

            rs.close();
            ps.close();
            con.close();

        } catch (SQLException e) {
            System.out.println("Error consultando mediciones: " + e.getMessage());
        }

        return resultado;
    }

    public Medicion getMedicionById(int id) {
        String sql = "SELECT * FROM mediciones WHERE id = ?";
        Medicion m = null;

        try {
            Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                m = new Medicion();
                m.setId(rs.getInt("id"));
                m.setParcelaId(rs.getInt("parcela_id"));
                m.setDispositivoUrn(rs.getString("dispositivo_urn"));
                m.setMagnitud(rs.getString("magnitud"));
                m.setUnidad(rs.getString("unidad"));
                m.setValorNumerico((Double) rs.getObject("valor_numerico"));
                m.setValorBooleano((Boolean) rs.getObject("valor_booleano"));
                m.setValorTexto(rs.getString("valor_texto"));
                m.setTimestampEpoch((Double) rs.getObject("timestamp_epoch"));
            }

            rs.close();
            ps.close();
            con.close();

        } catch (SQLException e) {
            System.out.println("Error consultando medicion: " + e.getMessage());
        }

        return m;
    }

    public boolean updateMedicion(int id, Medicion m) {
        String sql = "UPDATE mediciones SET dispositivo_urn = ?, magnitud = ?, unidad = ?, " +
            "valor_numerico = ?, valor_booleano = ?, valor_texto = ?, timestamp_epoch = ? " +
            "WHERE id = ?";

        try {
            Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, m.getDispositivoUrn());
            ps.setString(2, m.getMagnitud());
            ps.setString(3, m.getUnidad());

            if (m.getValorNumerico() != null) {
                ps.setDouble(4, m.getValorNumerico());
            } else {
                ps.setNull(4, java.sql.Types.DOUBLE);
            }

            if (m.getValorBooleano() != null) {
                ps.setBoolean(5, m.getValorBooleano());
            } else {
                ps.setNull(5, java.sql.Types.BOOLEAN);
            }

            ps.setString(6, m.getValorTexto());

            if (m.getTimestampEpoch() != null) {
                ps.setDouble(7, m.getTimestampEpoch());
            } else {
                ps.setNull(7, java.sql.Types.DOUBLE);
            }

            ps.setInt(8, id);

            int filas = ps.executeUpdate();
            ps.close();
            con.close();

            return filas > 0;

        } catch (SQLException e) {
            System.out.println("Error actualizando medicion: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteMedicion(int id) {
        String sql = "DELETE FROM mediciones WHERE id = ?";

        try {
            Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            int filas = ps.executeUpdate();
            ps.close();
            con.close();

            return filas > 0;

        } catch (SQLException e) {
            System.out.println("Error eliminando medicion: " + e.getMessage());
            return false;
        }
    }
}
