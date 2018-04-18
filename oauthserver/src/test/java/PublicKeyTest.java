import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;


public class PublicKeyTest {
    @Test

    public void test() {

        Resource resource = new ClassPathResource("kevin_key.jks");
        KeyPair keyPair = new KeyStoreKeyFactory(new ClassPathResource("kevin_key.jks"),
                "123456".toCharArray())
                .getKeyPair("kevin_key");
        RSAPublicKey key = (RSAPublicKey) keyPair.getPublic();
        String verifierKey = "-----begin-----\n" + new String(Base64.encode(key.getEncoded()))
                             + "\n------end-------";
        System.out.println(verifierKey);

    }
}
