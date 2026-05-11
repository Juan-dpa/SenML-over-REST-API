package agricultura;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
public class DispositivoController {

    DispositivoDAO dao;

    public DispositivoController() {
        dao = new DispositivoDAO();
    }

    @GetMapping("/dispositivos")
    public ResponseEntity<List<Dispositivo>> getDispositivos() {
        List<Dispositivo> dispositivos = dao.getAll();
        return new ResponseEntity<>(dispositivos, HttpStatus.OK);
    }

    @GetMapping("/dispositivos/{id}")
    public ResponseEntity<Dispositivo> getDispositivo(@PathVariable(value = "id") int id) {
        Dispositivo dispositivo = dao.getById(id);

        if (dispositivo != null) {
            return new ResponseEntity<>(dispositivo, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/dispositivos")
    public ResponseEntity<String> postDispositivo(@RequestBody Dispositivo dispositivo) {
        boolean ok = dao.insert(dispositivo);

        if (ok) {
            return new ResponseEntity<>("Dispositivo creado", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Error al crear dispositivo", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/dispositivos/{id}")
    public ResponseEntity<String> putDispositivo(
            @PathVariable(value = "id") int id,
            @RequestBody Dispositivo dispositivo) {

        boolean ok = dao.update(id, dispositivo);

        if (ok) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/dispositivos/{id}")
    public ResponseEntity<String> deleteDispositivo(@PathVariable(value = "id") int id) {
        boolean ok = dao.delete(id);

        if (ok) {
            return new ResponseEntity<>("Dispositivo eliminado", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No encontrado", HttpStatus.NOT_FOUND);
        }
    }
}
