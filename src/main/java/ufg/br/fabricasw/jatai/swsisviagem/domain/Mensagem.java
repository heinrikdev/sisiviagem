package ufg.br.fabricasw.jatai.swsisviagem.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class Mensagem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Usuario usuario;

    private LocalDateTime data;

    private String hora;

    private String texto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Mensagem mensagem = (Mensagem) o;

        if (id != null ? !id.equals(mensagem.id) : mensagem.id != null) {
            return false;
        }
        if (usuario != null ? !usuario.equals(mensagem.usuario) : mensagem.usuario != null) {
            return false;
        }
        if (data != null ? !data.equals(mensagem.data) : mensagem.data != null) {
            return false;
        }
        if (hora != null ? !hora.equals(mensagem.hora) : mensagem.hora != null) {
            return false;
        }
        return texto != null ? texto.equals(mensagem.texto) : mensagem.texto == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (usuario != null ? usuario.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        result = 31 * result + (hora != null ? hora.hashCode() : 0);
        result = 31 * result + (texto != null ? texto.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Mensagem{"
                + "id=" + id
                + ", usuario=" + usuario
                + ", data='" + data + '\''
                + ", hora='" + hora + '\''
                + ", texto='" + texto + '\''
                + '}';
    }
}
