package agricultura;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) para interactuar con la tabla de Mediciones.
 * Esta tabla guarda los datos recogidos por los dispositivos, incluyendo variables booleanas, de texto y numéricas.
 */
public class MedicionDAO {

    private static final String URL  = "jdbc:postgresql://localhost:5432/juadelavi";
    private static final String USER = "juadelavi";
    private static final String PASS = "2002";

    public MedicionDAO() {
    }

    /**
     * Establece y devuelve una conexión con la base de datos PostgreSQL.
     * @return Conexión a la base de datos local.
     * @throws SQLException Si ocurre algún problema conectando.
     */
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    /**
     * Inserta una nueva medición aislada en la base de datos.
     * Detecta el tipo de dato que contiene (numérico, booleano o texto) y establece los valores adecuados o NULL.
     * @param m Objeto Medicion mapeado, usualmente a partir de SenML.
     * @return true si la inserción fue correcta.
     */
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

    /**
     * Obtiene el historial completo de mediciones pertenecientes a una misma parcela.
     * Los resultados vienen ordenados por dispositivo y tiempo.
     * @param parcelaId ID de la parcela objetivo.
     * @return Lista de objetos Medicion devueltos.
     */
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

    /**
     * Recupera el histórico de datos reportados específicamente por un dispositivo determinado.
     * @param urn Identificador URN del dispositivo (mota).
     * @return Lista cronológica de sus mediciones.
     */
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

    /**
     * Obtiene una sola medición mediante su ID primario.
     * @param id Clave primaria de la medición.
     * @return El objeto Medicion, o null si no se halla en BD.
     */
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

    /**
     * Modifica el registro de una medición.
     * Actualiza magnitud, unidad y su respectivo valor (número, texto o booleano),
     * anulando los otros campos para mantener consistencia.
     * @param id ID de la medición a modificar.
     * @param m Nuevos datos para aplicar.
     * @return true si se aplicó el cambio exitosamente.
     */
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

    /**
     * Borra una medición del registro histórico.
     * @param id ID de la medición a eliminar.
     * @return true si la fila existía y fue borrada.
     */
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
