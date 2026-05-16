package agricultura;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) para la gestión de Parcelas en la base de datos PostgreSQL.
 * Contiene todas las operaciones CRUD (Crear, Leer, Actualizar, Borrar).
 */
public class ParcelaDAO {

    private static final String URL  = "jdbc:postgresql://localhost:5432/juadelavi";
    private static final String USER = "juadelavi";
    private static final String PASS = "2002";

    public ParcelaDAO() {
    }

    /**
     * Establece y devuelve una conexión con la base de datos.
     * @return Conexión activa a PostgreSQL.
     * @throws SQLException Si hay un error de conexión.
     */
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    /**
     * Recupera todas las parcelas almacenadas en la base de datos.
     * @return Lista de objetos Parcela.
     */
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

    /**
     * Busca y devuelve una parcela específica dado su ID.
     * @param id Identificador de la parcela a buscar.
     * @return Objeto Parcela encontrado o null si no existe.
     */
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

    /**
     * Inserta una nueva parcela en la base de datos.
     * @param p Objeto Parcela con los datos a insertar.
     * @return true si la inserción fue exitosa, false en caso de error.
     */
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

    /**
     * Actualiza los datos de una parcela existente.
     * @param id Identificador de la parcela a modificar.
     * @param p Objeto con los nuevos datos de la parcela.
     * @return true si se actualizó al menos una fila, false si no se encontró o hubo error.
     */
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

    /**
     * Elimina una parcela de la base de datos dado su ID.
     * @param id Identificador de la parcela a eliminar.
     * @return true si se eliminó exitosamente, false en caso contrario.
     */
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
