package agricultura;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Controlador REST para gestionar las operaciones CRUD sobre los Dispositivos (motas).
 */
@RestController
public class DispositivoController {

    DispositivoDAO dao;

    public DispositivoController() {
        dao = new DispositivoDAO();
    }

    /**
     * Obtiene la lista completa de todos los dispositivos registrados en el sistema.
     * @return Lista de dispositivos y código de estado 200 (OK).
     */
    @GetMapping("/dispositivos")
    public ResponseEntity<List<Dispositivo>> getDispositivos() {
        List<Dispositivo> dispositivos = dao.getAll();
        return new ResponseEntity<>(dispositivos, HttpStatus.OK);
    }

    /**
     * Obtiene un dispositivo específico utilizando su ID proporcionado como parámetro.
     * Ejemplo de uso: GET /dispositivos?id=3
     * @param id Identificador numérico del dispositivo.
     * @return El dispositivo si se encuentra (200 OK), o 404 Not Found si no existe.
     */
    @GetMapping(value = "/dispositivos", params = "id")
    public ResponseEntity<Dispositivo> getDispositivo(@RequestParam(value = "id") int id) {
        Dispositivo dispositivo = dao.getById(id);

        if (dispositivo != null) {
            return new ResponseEntity<>(dispositivo, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Crea un nuevo dispositivo.
     * @param dispositivo Objeto JSON que representa al dispositivo (incluyendo a qué parcela pertenece).
     * @return Mensaje de éxito (201 Created) o error si falla la inserción.
     */
    @PostMapping("/dispositivos")
    public ResponseEntity<String> postDispositivo(@RequestBody Dispositivo dispositivo) {
        boolean ok = dao.insert(dispositivo);

        if (ok) {
            return new ResponseEntity<>("Dispositivo creado", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Error al crear dispositivo", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Modifica los atributos de un dispositivo existente.
     * @param id Identificador del dispositivo a modificar (parámetro de consulta).
     * @param dispositivo Los nuevos valores para el dispositivo.
     * @return 200 OK si se actualiza correctamente, 404 Not Found en caso contrario.
     */
    @PutMapping("/dispositivos")
    public ResponseEntity<String> putDispositivo(
            @RequestParam(value = "id") int id,
            @RequestBody Dispositivo dispositivo) {

        boolean ok = dao.update(id, dispositivo);

        if (ok) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Elimina un dispositivo de la base de datos o almacenamiento.
     * @param id Identificador del dispositivo a eliminar.
     * @return 200 OK si se borró con éxito, o 404 Not Found.
     */
    @DeleteMapping("/dispositivos")
    public ResponseEntity<String> deleteDispositivo(@RequestParam(value = "id") int id) {
        boolean ok = dao.delete(id);

        if (ok) {
            return new ResponseEntity<>("Dispositivo eliminado", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No encontrado", HttpStatus.NOT_FOUND);
        }
    }
}
