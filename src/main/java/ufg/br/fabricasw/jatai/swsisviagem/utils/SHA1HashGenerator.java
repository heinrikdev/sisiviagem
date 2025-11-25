package ufg.br.fabricasw.jatai.swsisviagem.utils;

import java.util.Base64;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.util.encoders.Hex;


public class SHA1HashGenerator {

    private SHA1HashGenerator() {}

    /**
     * Faz o hash SHA1 de uma string.
     *
     * @param toBeHashed string que será hasheada.
     * @return a string hasheada.
     */
    public static String hash(String toBeHashed) {
        SHA1Digest digester = new SHA1Digest();
        digester.update(toBeHashed.getBytes(), 0, toBeHashed.length());
        byte[] result = new byte[digester.getDigestSize()];
        digester.doFinal(result, 0);

        return Hex.toHexString(result);
    }

    /**
     * Faz o encoding Base64 de uma string.
     *
     * @param toBeEncoded string que será encodada.
     * @return a string encodada em Base64
     */
    private static String toBase64EncodedString(String toBeEncoded) {
        return Base64.getEncoder().encodeToString(Hex.decode(toBeEncoded));
    }

    /**
     * Faz hash SHA1 para o formato do LDAP de uma string ("{SHA}o_hash_aqui").
     *
     * @param toBeHashed a string que será hasheada.
     * @return a string hasheada com o formato exigido pelo LDAP.
     */
    public static String ldapHash(String toBeHashed) {
        String hashed = hash(toBeHashed);
        String encoded = hashed != null ? toBase64EncodedString(hashed) : "";
        return "{SHA}".concat(encoded);
    }
}
