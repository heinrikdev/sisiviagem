package ufg.br.fabricasw.jatai.swsisviagem.service;

import de.triology.recaptchav2java.ReCaptcha;
import org.springframework.stereotype.Service;

/**
 *
 * @author Ronaldo N. de Sousa
 */
@Service
public class ReCaptchaService {
    
    private final ReCaptcha reCaptcha;
    private static final String KEY_RE_CAPTCHA = "6LcajJ8rAAAAAOzzMCdgHJaYOKU0OhK6t3HhjdTV";
    
    public ReCaptchaService() {
        
        this.reCaptcha = new ReCaptcha(KEY_RE_CAPTCHA);
    }
    
    public boolean isValid(String reCaptcha) {
        
        if (reCaptcha == null || reCaptcha.trim().length() <= 0) {
            return false;
        }
        
        return this.reCaptcha.isValid(reCaptcha);
    }

}
