package agricultura;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Medicion {

    private int id;
    private int parcelaId;
    private String dispositivoUrn;
    private String magnitud;
    private String unidad;
    private Double valorNumerico;
    private Boolean valorBooleano;
    private String valorTexto;
    private Double timestampEpoch;

    public Medicion() {
    }

    @JsonProperty("id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @JsonProperty("parcelaId")
    public int getParcelaId() {
        return parcelaId;
    }

    public void setParcelaId(int parcelaId) {
        this.parcelaId = parcelaId;
    }

    @JsonProperty("dispositivoUrn")
    public String getDispositivoUrn() {
        return dispositivoUrn;
    }

    public void setDispositivoUrn(String dispositivoUrn) {
        this.dispositivoUrn = dispositivoUrn;
    }

    @JsonProperty("magnitud")
    public String getMagnitud() {
        return magnitud;
    }

    public void setMagnitud(String magnitud) {
        this.magnitud = magnitud;
    }

    @JsonProperty("unidad")
    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    @JsonProperty("valorNumerico")
    public Double getValorNumerico() {
        return valorNumerico;
    }

    public void setValorNumerico(Double valorNumerico) {
        this.valorNumerico = valorNumerico;
    }

    @JsonProperty("valorBooleano")
    public Boolean getValorBooleano() {
        return valorBooleano;
    }

    public void setValorBooleano(Boolean valorBooleano) {
        this.valorBooleano = valorBooleano;
    }

    @JsonProperty("valorTexto")
    public String getValorTexto() {
        return valorTexto;
    }

    public void setValorTexto(String valorTexto) {
        this.valorTexto = valorTexto;
    }

    @JsonProperty("timestampEpoch")
    public Double getTimestampEpoch() {
        return timestampEpoch;
    }

    public void setTimestampEpoch(Double timestampEpoch) {
        this.timestampEpoch = timestampEpoch;
    }
}
