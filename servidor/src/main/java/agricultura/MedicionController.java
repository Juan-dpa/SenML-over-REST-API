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

import teamethernet.senmlapi.SenMLAPI;
import teamethernet.senmlapi.JsonFormatter;
import teamethernet.senmlapi.Label;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Controlador REST encargado de gestionar las mediciones de los dispositivos.
 * Soporta la ingesta y consulta de datos utilizando el estándar SenML (JSON).
 */
@RestController
public class MedicionController {

    MedicionDAO dao;

    public MedicionController() {
        dao = new MedicionDAO();
    }

    /**
     * Endpoint para recibir y almacenar una lista de mediciones en formato SenML enviadas por una mota.
     * @param parcelaId ID de la parcela a la que pertenecen las mediciones.
     * @param cuerpo Bytes del payload SenML en formato JSON.
     * @return 201 Created con el número de métricas insertadas, o 400 Bad Request si el SenML es inválido.
     */
    @PostMapping(
        value = "/parcelas/{id}/mediciones/senml",
        consumes = "application/senml+json"
    )
    public ResponseEntity<String> recibirSenML(
            @PathVariable(value = "id") int parcelaId,
            @RequestBody byte[] cuerpo) {

        try {
            SenMLAPI<JsonFormatter> senmlAPI = SenMLAPI.initJson(cuerpo);
            List<byte[]> records = senmlAPI.getRecords();

            String baseName = null;
            String baseUnit = null;
            double baseTime = 0.0;
            int insertadas = 0;

            for (int i = 0; i < records.size(); i++) {

                List<Label> labels = senmlAPI.getLabels(i);

                if (labels.contains(Label.BASE_NAME)) {
                    baseName = senmlAPI.getValue(Label.BASE_NAME, i);
                }
                if (labels.contains(Label.BASE_UNIT)) {
                    baseUnit = senmlAPI.getValue(Label.BASE_UNIT, i);
                }
                if (labels.contains(Label.BASE_TIME)) {
                    baseTime = senmlAPI.getValue(Label.BASE_TIME, i);
                }

                String magnitud = null;
                String unidad = baseUnit;
                double timestamp = baseTime;

                if (labels.contains(Label.NAME)) {
                    magnitud = senmlAPI.getValue(Label.NAME, i);
                }
                if (labels.contains(Label.UNIT)) {
                    unidad = senmlAPI.getValue(Label.UNIT, i);
                }
                if (labels.contains(Label.TIME)) {
                    timestamp = baseTime + senmlAPI.getValue(Label.TIME, i);
                }

                if (labels.contains(Label.VALUE)) {
                    double valor = senmlAPI.getValue(Label.VALUE, i);

                    Medicion medicion = new Medicion();
                    medicion.setParcelaId(parcelaId);
                    medicion.setDispositivoUrn(baseName);
                    medicion.setMagnitud(magnitud);
                    medicion.setUnidad(unidad);
                    medicion.setValorNumerico(valor);
                    medicion.setTimestampEpoch(timestamp);

                    if (dao.insertarMedicion(medicion)) {
                        insertadas++;
                    }
                }

                if (labels.contains(Label.BOOLEAN_VALUE)) {
                    boolean valor = senmlAPI.getValue(Label.BOOLEAN_VALUE, i);

                    Medicion medicion = new Medicion();
                    medicion.setParcelaId(parcelaId);
                    medicion.setDispositivoUrn(baseName);
                    medicion.setMagnitud(magnitud);
                    medicion.setUnidad(unidad);
                    medicion.setValorBooleano(valor);
                    medicion.setTimestampEpoch(timestamp);

                    if (dao.insertarMedicion(medicion)) {
                        insertadas++;
                    }
                }

                if (labels.contains(Label.STRING_VALUE)) {
                    String valor = senmlAPI.getValue(Label.STRING_VALUE, i);

                    Medicion medicion = new Medicion();
                    medicion.setParcelaId(parcelaId);
                    medicion.setDispositivoUrn(baseName);
                    medicion.setMagnitud(magnitud);
                    medicion.setUnidad(unidad);
                    medicion.setValorTexto(valor);
                    medicion.setTimestampEpoch(timestamp);

                    if (dao.insertarMedicion(medicion)) {
                        insertadas++;
                    }
                }
            }

            return new ResponseEntity<>("Mediciones insertadas: " + insertadas, HttpStatus.CREATED);

        } catch (IOException e) {
            return new ResponseEntity<>("SenML no valido", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Recupera todas las mediciones de una parcela y las serializa en formato SenML.
     * @param parcelaId ID de la parcela a consultar.
     * @return Array de bytes que conforma el documento JSON SenML, o 500 en caso de error interno.
     */
    @GetMapping(
        value = "/parcelas/{id}/mediciones/senml",
        produces = "application/senml+json"
    )
    public ResponseEntity<byte[]> obtenerSenMLPorParcela(
            @PathVariable(value = "id") int parcelaId) {

        try {
            List<Medicion> mediciones = dao.getMedicionesByParcela(parcelaId);
            SenMLAPI<JsonFormatter> senmlAPI = SenMLAPI.initJson();
            String lastUrn = null;
            Double bt = null;

            for (Medicion m : mediciones) {
                if (m.getValorNumerico() == null && m.getValorBooleano() == null && m.getValorTexto() == null) {
                    continue;
                }

                List<Label.Pair> pares = new ArrayList<>();
                boolean nuevoDispositivo = !Objects.equals(m.getDispositivoUrn(), lastUrn);

                if (nuevoDispositivo) {
                    if (m.getDispositivoUrn() != null) {
                        pares.add(Label.BASE_NAME.attachValue(m.getDispositivoUrn()));
                    }
                    if (m.getTimestampEpoch() != null) {
                        pares.add(Label.BASE_TIME.attachValue(m.getTimestampEpoch()));
                        bt = m.getTimestampEpoch();
                    }
                    lastUrn = m.getDispositivoUrn();
                } else {
                    if (m.getTimestampEpoch() != null) {
                        double t = (bt != null) ? m.getTimestampEpoch() - bt : m.getTimestampEpoch();
                        pares.add(Label.TIME.attachValue(t));
                    }
                }

                if (m.getMagnitud() != null) {
                    pares.add(Label.NAME.attachValue(m.getMagnitud()));
                }
                if (m.getUnidad() != null) {
                    pares.add(Label.UNIT.attachValue(m.getUnidad()));
                }

                if (m.getValorNumerico() != null) {
                    pares.add(Label.VALUE.attachValue(m.getValorNumerico()));
                } else if (m.getValorBooleano() != null) {
                    pares.add(Label.BOOLEAN_VALUE.attachValue(m.getValorBooleano()));
                } else {
                    pares.add(Label.STRING_VALUE.attachValue(m.getValorTexto()));
                }

                senmlAPI.addRecord(pares.toArray(new Label.Pair[0]));
            }

            byte[] cuerpo = senmlAPI.getSenML();
            return new ResponseEntity<>(cuerpo, HttpStatus.OK);

        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Actualiza una medición específica enviando el nuevo valor en formato SenML.
     * @param parcelaId ID de la parcela.
     * @param mid ID de la medición a actualizar en la base de datos.
     * @param cuerpo Payload SenML con el nuevo valor.
     * @return 200 OK si se actualiza, 404 si la medición no se encontró.
     */
    @PutMapping(
        value = "/parcelas/{parcelaId}/mediciones/{mid}/senml",
        consumes = "application/senml+json"
    )
    public ResponseEntity<String> actualizarSenML(
            @PathVariable(value = "parcelaId") int parcelaId,
            @PathVariable(value = "mid") int mid,
            @RequestBody byte[] cuerpo) {

        try {
            SenMLAPI<JsonFormatter> senmlAPI = SenMLAPI.initJson(cuerpo);
            List<byte[]> records = senmlAPI.getRecords();

            if (records.isEmpty()) {
                return new ResponseEntity<>("SenML vacio", HttpStatus.BAD_REQUEST);
            }

            List<Label> labels = senmlAPI.getLabels(0);

            String baseName = null;
            String baseUnit = null;
            double baseTime = 0.0;

            if (labels.contains(Label.BASE_NAME)) {
                baseName = senmlAPI.getValue(Label.BASE_NAME, 0);
            }
            if (labels.contains(Label.BASE_UNIT)) {
                baseUnit = senmlAPI.getValue(Label.BASE_UNIT, 0);
            }
            if (labels.contains(Label.BASE_TIME)) {
                baseTime = senmlAPI.getValue(Label.BASE_TIME, 0);
            }

            String magnitud = null;
            String unidad = baseUnit;
            double timestamp = baseTime;

            if (labels.contains(Label.NAME)) {
                magnitud = senmlAPI.getValue(Label.NAME, 0);
            }
            if (labels.contains(Label.UNIT)) {
                unidad = senmlAPI.getValue(Label.UNIT, 0);
            }
            if (labels.contains(Label.TIME)) {
                timestamp = baseTime + senmlAPI.getValue(Label.TIME, 0);
            }

            Medicion medicion = new Medicion();
            medicion.setParcelaId(parcelaId);
            medicion.setDispositivoUrn(baseName);
            medicion.setMagnitud(magnitud);
            medicion.setUnidad(unidad);
            medicion.setTimestampEpoch(timestamp);

            if (labels.contains(Label.VALUE)) {
                medicion.setValorNumerico(senmlAPI.getValue(Label.VALUE, 0));
            } else if (labels.contains(Label.BOOLEAN_VALUE)) {
                medicion.setValorBooleano(senmlAPI.getValue(Label.BOOLEAN_VALUE, 0));
            } else if (labels.contains(Label.STRING_VALUE)) {
                medicion.setValorTexto(senmlAPI.getValue(Label.STRING_VALUE, 0));
            }

            boolean ok = dao.updateMedicion(mid, medicion);

            if (ok) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (IOException e) {
            return new ResponseEntity<>("SenML no valido", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Elimina una medición puntual utilizando su ID.
     * @param parcelaId ID de la parcela.
     * @param mid ID de la medición.
     * @return 200 OK si se borró, o 404 Not Found si no existía.
     */
    @DeleteMapping("/parcelas/{parcelaId}/mediciones/{mid}")
    public ResponseEntity<String> deleteMedicion(
            @PathVariable(value = "parcelaId") int parcelaId,
            @PathVariable(value = "mid") int mid) {

        boolean ok = dao.deleteMedicion(mid);

        if (ok) {
            return new ResponseEntity<>("Medicion eliminada", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No encontrada", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Recupera todas las mediciones de un dispositivo concreto (mota) en formato SenML.
     * @param urn URN o identificador único del dispositivo.
     * @return Archivo JSON SenML con todas sus medidas registradas.
     */
    @GetMapping(
        value = "/dispositivos/{urn}/mediciones/senml",
        produces = "application/senml+json"
    )
    public ResponseEntity<byte[]> obtenerSenMLPorDispositivo(
            @PathVariable(value = "urn") String urn) {

        try {
            List<Medicion> mediciones = dao.getMedicionesByDispositivo(urn);
            SenMLAPI<JsonFormatter> senmlAPI = SenMLAPI.initJson();
            String lastUrn = null;
            Double bt = null;

            for (Medicion m : mediciones) {
                if (m.getValorNumerico() == null && m.getValorBooleano() == null && m.getValorTexto() == null) {
                    continue;
                }

                List<Label.Pair> pares = new ArrayList<>();
                boolean nuevoDispositivo = !Objects.equals(m.getDispositivoUrn(), lastUrn);

                if (nuevoDispositivo) {
                    if (m.getDispositivoUrn() != null) {
                        pares.add(Label.BASE_NAME.attachValue(m.getDispositivoUrn()));
                    }
                    if (m.getTimestampEpoch() != null) {
                        pares.add(Label.BASE_TIME.attachValue(m.getTimestampEpoch()));
                        bt = m.getTimestampEpoch();
                    }
                    lastUrn = m.getDispositivoUrn();
                } else {
                    if (m.getTimestampEpoch() != null) {
                        double t = (bt != null) ? m.getTimestampEpoch() - bt : m.getTimestampEpoch();
                        pares.add(Label.TIME.attachValue(t));
                    }
                }

                if (m.getMagnitud() != null) {
                    pares.add(Label.NAME.attachValue(m.getMagnitud()));
                }
                if (m.getUnidad() != null) {
                    pares.add(Label.UNIT.attachValue(m.getUnidad()));
                }

                if (m.getValorNumerico() != null) {
                    pares.add(Label.VALUE.attachValue(m.getValorNumerico()));
                } else if (m.getValorBooleano() != null) {
                    pares.add(Label.BOOLEAN_VALUE.attachValue(m.getValorBooleano()));
                } else {
                    pares.add(Label.STRING_VALUE.attachValue(m.getValorTexto()));
                }

                senmlAPI.addRecord(pares.toArray(new Label.Pair[0]));
            }

            byte[] cuerpo = senmlAPI.getSenML();
            return new ResponseEntity<>(cuerpo, HttpStatus.OK);

        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
