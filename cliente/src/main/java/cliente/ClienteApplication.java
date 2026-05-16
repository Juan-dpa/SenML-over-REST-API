package cliente;

import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * Aplicación cliente de consola para interactuar con la API REST de Agricultura Inteligente.
 * Proporciona un menú interactivo para realizar peticiones HTTP (GET, POST, PUT, DELETE).
 */
@SpringBootApplication
public class ClienteApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClienteApplication.class, args);
    }

    /**
     * Configura y ejecuta el cliente interactivo.
     * @param builder Constructor del cliente REST proporcionado por Spring.
     * @return ApplicationRunner que contiene el bucle principal del programa.
     */
    @Bean
    @Profile("!test")
    public ApplicationRunner run(RestClient.Builder builder) {
        // Inicializar el cliente apuntando al servidor local
        RestClient restClient = builder.baseUrl("http://localhost:8080").build();

        return args -> {
            Terminal terminal = TerminalBuilder.builder().system(true).dumb(true).build();
            LineReader reader = LineReaderBuilder.builder().terminal(terminal).build();
            PrintWriter out = terminal.writer();

            boolean running = true;

            while (running) {
                out.println();
                out.println("=== CLIENTE AGRICULTURA ===");
                out.println(" 1.  Listar parcelas");
                out.println(" 2.  Obtener parcela por id");
                out.println(" 3.  Crear parcela");
                out.println(" 4.  Actualizar parcela");
                out.println(" 5.  Eliminar parcela");
                out.println(" 6.  Listar dispositivos");
                out.println(" 7.  Obtener dispositivo por id");
                out.println(" 8.  Crear dispositivo");
                out.println(" 9.  Actualizar dispositivo");
                out.println("10.  Eliminar dispositivo");
                out.println("11.  Enviar mediciones SenML a parcela");
                out.println("12.  Consultar mediciones de parcela (SenML)");
                out.println("13.  Consultar mediciones de dispositivo (SenML)");
                out.println("14.  Eliminar medicion");
                out.println(" 0.  Salir");
                out.flush();

                String opcion;
                try {
                    opcion = reader.readLine("> ").trim();
                } catch (UserInterruptException | EndOfFileException e) {
                    break;
                }

                try {
                    switch (opcion) {

                        case "1": {
                            // Obtiene y muestra todas las parcelas (GET /parcelas)
                            String resp = restClient.get().uri("/parcelas")
                                .retrieve().body(String.class);
                            out.println(resp);
                            break;
                        }

                        case "2": {
                            // Pide el ID y consulta una parcela específica (GET /parcelas?id=X)
                            int id = Integer.parseInt(reader.readLine("Id parcela: ").trim());
                            String resp = restClient.get().uri("/parcelas?id=" + id)
                                .retrieve().body(String.class);
                            out.println(resp);
                            break;
                        }

                        case "3": {
                            // Lee datos y crea una nueva parcela enviando un JSON (POST /parcelas)
                            String nombre = reader.readLine("Nombre: ").trim();
                            String ubicacion = reader.readLine("Ubicacion: ").trim();
                            String body = "{\"nombre\":\"" + nombre + "\",\"ubicacion\":\"" + ubicacion + "\"}";
                            String resp = restClient.post().uri("/parcelas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(body).retrieve().body(String.class);
                            out.println(resp);
                            break;
                        }

                        case "4": {
                            int id = Integer.parseInt(reader.readLine("Id parcela: ").trim());
                            String nombre = reader.readLine("Nuevo nombre: ").trim();
                            String ubicacion = reader.readLine("Nueva ubicacion: ").trim();
                            String body = "{\"nombre\":\"" + nombre + "\",\"ubicacion\":\"" + ubicacion + "\"}";
                            restClient.put().uri("/parcelas?id=" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(body).retrieve().toBodilessEntity();
                            out.println("Parcela actualizada");
                            break;
                        }

                        case "5": {
                            int id = Integer.parseInt(reader.readLine("Id parcela: ").trim());
                            String resp = restClient.delete().uri("/parcelas?id=" + id)
                                .retrieve().body(String.class);
                            out.println(resp);
                            break;
                        }

                        case "6": {
                            String resp = restClient.get().uri("/dispositivos")
                                .retrieve().body(String.class);
                            out.println(resp);
                            break;
                        }

                        case "7": {
                            int id = Integer.parseInt(reader.readLine("Id dispositivo: ").trim());
                            String resp = restClient.get().uri("/dispositivos?id=" + id)
                                .retrieve().body(String.class);
                            out.println(resp);
                            break;
                        }

                        case "8": {
                            int parcelaId = Integer.parseInt(reader.readLine("Id parcela: ").trim());
                            String urn = reader.readLine("URN: ").trim();
                            String tipo = reader.readLine("Tipo: ").trim();
                            String desc = reader.readLine("Descripcion: ").trim();
                            String body = "{\"parcelaId\":" + parcelaId + ",\"urn\":\"" + urn +
                                "\",\"tipo\":\"" + tipo + "\",\"descripcion\":\"" + desc + "\"}";
                            String resp = restClient.post().uri("/dispositivos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(body).retrieve().body(String.class);
                            out.println(resp);
                            break;
                        }

                        case "9": {
                            int id = Integer.parseInt(reader.readLine("Id dispositivo: ").trim());
                            int parcelaId = Integer.parseInt(reader.readLine("Id parcela: ").trim());
                            String urn = reader.readLine("URN: ").trim();
                            String tipo = reader.readLine("Tipo: ").trim();
                            String desc = reader.readLine("Descripcion: ").trim();
                            String body = "{\"parcelaId\":" + parcelaId + ",\"urn\":\"" + urn +
                                "\",\"tipo\":\"" + tipo + "\",\"descripcion\":\"" + desc + "\"}";
                            restClient.put().uri("/dispositivos?id=" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(body).retrieve().toBodilessEntity();
                            out.println("Dispositivo actualizado");
                            break;
                        }

                        case "10": {
                            int id = Integer.parseInt(reader.readLine("Id dispositivo: ").trim());
                            String resp = restClient.delete().uri("/dispositivos?id=" + id)
                                .retrieve().body(String.class);
                            out.println(resp);
                            break;
                        }

                        case "11": {
                            // Genera y envía un array JSON en formato SenML para una parcela (POST)
                            int parcelaId = Integer.parseInt(reader.readLine("Id parcela: ").trim());
                            String bn = reader.readLine("URN dispositivo (ej: urn:dev:greenhouse-01/): ").trim();
                            int n = Integer.parseInt(reader.readLine("Numero de mediciones: ").trim());

                            double bt = System.currentTimeMillis() / 1000.0;
                            StringBuilder senml = new StringBuilder("[");
                            senml.append("{\"bn\":\"").append(bn).append("\",\"bt\":").append(bt);

                            for (int i = 0; i < n; i++) {
                                out.println("--- Medicion " + (i + 1) + " ---");
                                out.flush();
                                String name = reader.readLine("Nombre (n): ").trim();
                                String unit = reader.readLine("Unidad (u): ").trim();
                                String tipoValor = reader.readLine("Tipo valor [num/bool/text]: ").trim();

                                if (i == 0) {
                                    senml.append(",\"n\":\"").append(name)
                                         .append("\",\"u\":\"").append(unit).append("\"");
                                } else {
                                    senml.append(",{\"n\":\"").append(name)
                                         .append("\",\"u\":\"").append(unit).append("\"");
                                }

                                if ("bool".equals(tipoValor)) {
                                    senml.append(",\"vb\":").append(reader.readLine("Valor (true/false): ").trim());
                                } else if ("text".equals(tipoValor)) {
                                    senml.append(",\"vs\":\"").append(reader.readLine("Valor: ").trim()).append("\"");
                                } else {
                                    senml.append(",\"v\":").append(reader.readLine("Valor numerico: ").trim());
                                }
                                senml.append("}");
                            }
                            senml.append("]");

                            String resp = restClient.post()
                                .uri("/parcelas/" + parcelaId + "/mediciones/senml")
                                .contentType(new MediaType("application", "senml+json"))
                                .body(senml.toString().getBytes(StandardCharsets.UTF_8))
                                .retrieve().body(String.class);
                            out.println(resp);
                            break;
                        }

                        case "12": {
                            int parcelaId = Integer.parseInt(reader.readLine("Id parcela: ").trim());
                            byte[] resp = restClient.get()
                                .uri("/parcelas/" + parcelaId + "/mediciones/senml")
                                .accept(new MediaType("application", "senml+json"))
                                .retrieve().body(byte[].class);
                            out.println(new String(resp, StandardCharsets.UTF_8));
                            break;
                        }

                        case "13": {
                            String urn = reader.readLine("URN dispositivo (ej: urn:dev:greenhouse-01): ").trim();
                            byte[] resp = restClient.get()
                                .uri(u -> u.path("/dispositivos/{urn}/mediciones/senml").build(urn))
                                .accept(new MediaType("application", "senml+json"))
                                .retrieve().body(byte[].class);
                            out.println(new String(resp, StandardCharsets.UTF_8));
                            break;
                        }

                        case "14": {
                            int parcelaId = Integer.parseInt(reader.readLine("Id parcela: ").trim());
                            int mid = Integer.parseInt(reader.readLine("Id medicion: ").trim());
                            String resp = restClient.delete()
                                .uri("/parcelas/" + parcelaId + "/mediciones/" + mid)
                                .retrieve().body(String.class);
                            out.println(resp);
                            break;
                        }

                        case "0":
                            running = false;
                            break;

                        default:
                            if (!opcion.isEmpty()) {
                                out.println("Opcion no valida");
                            }
                    }

                } catch (HttpClientErrorException e) {
                    // Captura errores HTTP devueltos por el servidor (ej. 404, 400, 500)
                    out.println("Error " + e.getStatusCode().value() + ": " + e.getResponseBodyAsString());
                } catch (NumberFormatException e) {
                    // Captura errores si el usuario introduce letras en vez de números
                    out.println("Valor numerico invalido");
                }

                out.flush();
            }

            try { terminal.close(); } catch (IOException ignored) {}
        };
    }
}
