package ufg.br.fabricasw.jatai.swsisviagem.componente.relatorio_jasper;

public class ItemItinerario {
    private String data;
    private String origem;
    private String destino;
    private String transporte;

    // Construtor
    public ItemItinerario(String data, String origem, String destino, String transporte) {
        this.data = data;
        this.origem = origem;
        this.destino = destino;
        this.transporte = transporte;
    }

    // Getters e Setters
    public String getData() { return data; }
    public void setData(String data) { this.data = data; }
    public String getOrigem() { return origem; }
    public void setOrigem(String origem) { this.origem = origem; }
    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }
    public String getTransporte() { return transporte; }
    public void setTransporte(String transporte) { this.transporte = transporte; }
}