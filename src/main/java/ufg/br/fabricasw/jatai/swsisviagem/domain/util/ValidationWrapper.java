package ufg.br.fabricasw.jatai.swsisviagem.domain.util;

import org.springframework.validation.BindingResult;

/**
 * Classe que encapsula algumas operações básicas de validações de forma
 * segura,tomando os devidos cuidados para não causar NullPointException.
 *
 * @author ronogue
 */
public class ValidationWrapper {

    private BindingResult validation;
    private final static String NO_ERROR = "";

    public ValidationWrapper(BindingResult validation) {
        this.validation = validation;
    }
    
    public ValidationWrapper() {
        this.validation = null;
    }

    /**
     * Verifica se a validação possui erros.
     *
     * @return true em caso afirmativo, false do contrário.
     */
    public boolean hasErrors() {
        return this.getValidation() != null && this.getValidation().hasErrors();
    }

    /**
     * Verifica se um campo possui erro.
     *
     * @param fieldName - nome do campo correspondente ao do objeto a ser
     * validado.
     * @return true em caso afirmativo, false do contrário.
     */
    public boolean fieldHasError(String fieldName) {
        return this.getValidation() != null && this.getValidation().getFieldError(fieldName) != null;
    }

    /**
     * Pega a mensagem padrão de erro.
     *
     * É importante verificar antes se o campo há erro antes de executar este
     * método.
     *
     * @param fieldName nome do campo correspondente ao do objeto a ser
     * validado.
     * @return Caso haja mensagem retorna a mensagem, do contrário uma String
     * vazia.
     */
    public String getErrorMessage(String fieldName) {

        if (this.fieldHasError(fieldName)) {
            return this.getValidation().getFieldError(fieldName).getDefaultMessage();
        }

        return NO_ERROR;
    }

    /**
     * @return the validation
     */
    public BindingResult getValidation() {
        return validation;
    }

    /**
     * @param validation the validation to set
     */
    public void setValidation(BindingResult validation) {
        this.validation = validation;
    }
}
