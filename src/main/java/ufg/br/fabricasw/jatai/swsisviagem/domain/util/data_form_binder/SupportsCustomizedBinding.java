package ufg.br.fabricasw.jatai.swsisviagem.domain.util.data_form_binder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Esta anotação vai em cima do nome da classe, isto faz com quê a classe
 * supporte binding customizável, ou seja, o name do input do formulário será
 * diferente do nome do campo/propriedade da classe.
 *
 * Importante: Para tornar um campo/proprieda/field de uma classe customizado,
 * este dever ter a anotção "@ParamFieldName("NOVO_PARAMETRO_NAME_AQUI").
 *
 * Porquê usar? Bom, as vezes em um único formulário há vários objetos e pode
 * acontecer desses objetos terem campos/propriedades com nomes iguais, o
 * problema surge quando o spring vai associar os campos dos formulários com os
 * campos/propriedades do objeto e pode acontecer de valores irem para objetos
 * diferentes, um bom exemplo é o 'id' uma vez que todos os objetos possuem o
 * campo 'id'.
 *
 * @author Ronaldo N. de Sousa
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SupportsCustomizedBinding {

}
