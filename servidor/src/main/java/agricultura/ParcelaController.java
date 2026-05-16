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
 * Controlador REST para gestionar las operaciones CRUD sobre Parcelas.
 */
@RestController
public class ParcelaController {

    ParcelaDAO dao;

    public ParcelaController() {
        dao = new ParcelaDAO();
    }

    /**
     * Obtiene la lista completa de todas las parcelas.
     * @return Lista de parcelas y estado HTTP 200 (OK).
     */
    @GetMapping("/parcelas")
    public ResponseEntity<List<Parcela>> getParcelas() {
        List<Parcela> parcelas = dao.getAll();
        return new ResponseEntity<>(parcelas, HttpStatus.OK);
    }

    /**
     * Obtiene una parcela específica por su ID mediante un parámetro de consulta (query parameter).
     * Ejemplo de uso: GET /parcelas?id=1
     * @param id Identificador de la parcela.
     * @return Parcela encontrada (200 OK) o un estado 404 si no existe.
     */
    @GetMapping(value = "/parcelas", params = "id")
    public ResponseEntity<Parcela> getParcela(@RequestParam(value = "id") int id) {
        Parcela parcela = dao.getById(id);

        if (parcela != null) {
            return new ResponseEntity<>(parcela, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Crea una nueva parcela a partir de los datos JSON enviados en el cuerpo de la petición.
     * @param parcela Objeto Parcela deserializado automáticamente.
     * @return Mensaje de éxito (201 Created) o error (500 Internal Server Error).
     */
    @PostMapping("/parcelas")
    public ResponseEntity<String> postParcela(@RequestBody Parcela parcela) {
        boolean ok = dao.insert(parcela);

        if (ok) {
            return new ResponseEntity<>("Parcela creada", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Error al crear parcela", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Actualiza los datos de una parcela existente.
     * El ID se pasa como parámetro de consulta: PUT /parcelas?id=1
     * @param id Identificador de la parcela a modificar.
     * @param parcela Nuevos datos de la parcela.
     * @return Estado 200 (OK) si se actualizó correctamente, o 404 si no se encontró.
     */
    @PutMapping("/parcelas")
    public ResponseEntity<String> putParcela(
            @RequestParam(value = "id") int id,
            @RequestBody Parcela parcela) {

        boolean ok = dao.update(id, parcela);

        if (ok) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Elimina una parcela del sistema.
     * @param id Identificador de la parcela a eliminar (pasado como query parameter).
     * @return Estado 200 (OK) si se eliminó, o 404 si no existía.
     */
    @DeleteMapping("/parcelas")
    public ResponseEntity<String> deleteParcela(@RequestParam(value = "id") int id) {
        boolean ok = dao.delete(id);

        if (ok) {
            return new ResponseEntity<>("Parcela eliminada", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No encontrada", HttpStatus.NOT_FOUND);
        }
    }
}
