package ufg.br.fabricasw.jatai.swsisviagem.domain.factories;

import java.util.ArrayList;
import java.util.List;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.Role;

/**
 * 
 * @author Ronaldo N. de Sousa
 */
public class UsuarioRolesFactorie {
    
    public static List<Role> build(String permissao) {
        
        ArrayList<Role> roles = new ArrayList<>();
        
        switch (permissao) {
            
            case "PASSAGEM":
                
                roles.add(Role.DIARIA);
                roles.add(Role.PASSAGEM);
                break;
                
            case "TRANSPORTE":
                
                roles.add(Role.TRANSPORTE);
                break;
                
            case "ADMINISTRADOR":
                
                roles.add(Role.ADMIN);
                break;
                
            case "AUDITOR":
                
                roles.add(Role.AUDITOR);
                break;
                
            default:
                break;
        }
        
        return roles;
    }
}
