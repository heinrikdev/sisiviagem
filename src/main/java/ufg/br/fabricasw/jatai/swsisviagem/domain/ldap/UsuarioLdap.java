package ufg.br.fabricasw.jatai.swsisviagem.domain.ldap;

import javax.naming.Name;
import lombok.Data;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.DnAttribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

@Data
@Entry(
        objectClasses = {
                "brPerson",
                "inetOrgPerson",
                "person",
                "sambaSamAccount",
                "schacPersonalCharacteristics"
        },
        base = "ou=people")
public final class UsuarioLdap {

    @Id private Name dn;

    @Attribute(name = "uid")
    @DnAttribute(value = "uid", index = 1)
    private String loginUnico;

    @Attribute(name = "userPassword")
    private String password;

}
