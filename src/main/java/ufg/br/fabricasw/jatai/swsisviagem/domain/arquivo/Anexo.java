package ufg.br.fabricasw.jatai.swsisviagem.domain.arquivo;

import java.io.IOException;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;
import ufg.br.fabricasw.jatai.swsisviagem.domain.util.data_form_binder.ParamFieldName;
import ufg.br.fabricasw.jatai.swsisviagem.domain.util.data_form_binder.SupportsCustomizedBinding;
import ufg.br.fabricasw.jatai.swsisviagem.domain.util.validators.MultipartFileNotEmpty;

@Entity
@SupportsCustomizedBinding
public class Anexo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ParamFieldName("anexo_id")
    private Long id;
    
    @NotBlank(message = "Este campo é obrigatório.")
    @ParamFieldName("anexo_nome")
    private String nome;
    
    @NotBlank(message = "Este campo é obrigatório.")
    private String rotulo;
    
    private String caminho;
    
    private String descricao;
    
    @Transient
    @MultipartFileNotEmpty
    private MultipartFile file;
    
    @Transient
    private byte[] fileBytes;

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Anexo anexo = (Anexo) o;

        if (id != null ? !id.equals(anexo.id) : anexo.id != null) {
            return false;
        }
        if (nome != null ? !nome.equals(anexo.nome) : anexo.nome != null) {
            return false;
        }
        if (rotulo != null ? !rotulo.equals(anexo.rotulo) : anexo.rotulo != null) {
            return false;
        }
        if (caminho != null ? !caminho.equals(anexo.caminho) : anexo.caminho != null) {
            return false;
        }
        return descricao != null ? descricao.equals(anexo.descricao) : anexo.descricao == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (nome != null ? nome.hashCode() : 0);
        result = 31 * result + (rotulo != null ? rotulo.hashCode() : 0);
        result = 31 * result + (caminho != null ? caminho.hashCode() : 0);
        result = 31 * result + (descricao != null ? descricao.hashCode() : 0);
        return result;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRotulo() {
        return rotulo;
    }

    public void setRotulo(String rotulo) {
        this.rotulo = rotulo;
    }

    public String getCaminho() {
        return caminho;
    }

    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }

    /**
     * @return the file
     */
    public MultipartFile getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(MultipartFile file) {
        try {
            
            this.file = file;
            this.setFileBytes(file.getBytes());
            
        } catch (IOException ex) {
            
            Logger.getLogger(Anexo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String toString() {
        
        String fileName = (file !=null && file.getOriginalFilename() != null) ?  file.getOriginalFilename(): "";
        
        return 
        "\n\tAnexo {" + 
            "\n\t\tid=" + id + ","+ 
            "\n\t\tnome=" + nome + ","+ 
            "\n\t\trotulo=" + rotulo + "," +
            "\n\t\tcaminho=" + caminho + "," + 
            "\n\t\tdescricao=" + descricao + "," + 
            "\n\t\tdescricao=" + fileName + "," + 
        "\n\t}";
    }

    /**
     * @return the fileBytes
     */
    public byte[] getFileBytes() {
        return fileBytes;
    }

    /**
     * @param fileBytes the fileBytes to set
     */
    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }
    
    
}
