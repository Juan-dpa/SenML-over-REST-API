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
public class ParcelaController {

    ParcelaDAO dao;

    public ParcelaController() {
        dao = new ParcelaDAO();
    }

    @GetMapping("/parcelas")
    public ResponseEntity<List<Parcela>> getParcelas() {
        List<Parcela> parcelas = dao.getAll();
        return new ResponseEntity<>(parcelas, HttpStatus.OK);
    }

    @GetMapping("/parcelas/{id}")
    public ResponseEntity<Parcela> getParcela(@PathVariable(value = "id") int id) {
        Parcela parcela = dao.getById(id);

        if (parcela != null) {
            return new ResponseEntity<>(parcela, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/parcelas")
    public ResponseEntity<String> postParcela(@RequestBody Parcela parcela) {
        boolean ok = dao.insert(parcela);

        if (ok) {
            return new ResponseEntity<>("Parcela creada", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Error al crear parcela", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/parcelas/{id}")
    public ResponseEntity<String> putParcela(
            @PathVariable(value = "id") int id,
            @RequestBody Parcela parcela) {

        boolean ok = dao.update(id, parcela);

        if (ok) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/parcelas/{id}")
    public ResponseEntity<String> deleteParcela(@PathVariable(value = "id") int id) {
        boolean ok = dao.delete(id);

        if (ok) {
            return new ResponseEntity<>("Parcela eliminada", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No encontrada", HttpStatus.NOT_FOUND);
        }
    }
}
