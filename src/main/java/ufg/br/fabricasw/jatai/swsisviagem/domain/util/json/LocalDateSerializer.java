package ufg.br.fabricasw.jatai.swsisviagem.domain.util.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Ronaldo N. de Sousa
 */
public class LocalDateSerializer extends StdSerializer<LocalDate> {

    public LocalDateSerializer() {
        this(null);
    }
    
    
    public LocalDateSerializer(Class t) {
        super(t);
    } 
    
    @Override
    public void serialize(LocalDate date, JsonGenerator gen, SerializerProvider arg2) throws IOException {
        
        DateTimeFormatter ofPattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        gen.writeString(date.format(ofPattern));
    }

}
