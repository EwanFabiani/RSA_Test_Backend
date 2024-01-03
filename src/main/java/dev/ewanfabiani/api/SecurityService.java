package dev.ewanfabiani.api;

import dev.ewanfabiani.api.data.User;
import dev.ewanfabiani.database.tables.AuthTable;
import dev.ewanfabiani.database.tables.UserTable;
import dev.ewanfabiani.exceptions.DatabaseException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

public class SecurityService {

    public boolean verifyMessage(String message, String signature, String sender) {
        try {
            UserTable userTable = new UserTable();
            User user = userTable.getUser(sender);
            boolean valid = verifySignature(message, signature, user.getExponent(), user.getModulus());
            System.out.println("Valid: " + valid);
            return user != null && valid;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean verifySignature(String message, String signature, String exponent, String modulus) {
        Security.addProvider(new BouncyCastleProvider());
        try {
            byte[] expBytes = Base64.getDecoder().decode(exponent);
            byte[] modBytes = Base64.getDecoder().decode(modulus);

            BigInteger exp = new BigInteger(new String(expBytes));
            BigInteger mod = new BigInteger(new String(modBytes));

            RSAPublicKeySpec spec = new RSAPublicKeySpec(mod, exp);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            PublicKey pub = factory.generatePublic(spec);

            Signature sig = Signature.getInstance("SHA256withRSA", "BC");
            sig.initVerify(pub);
            sig.update(message.getBytes(StandardCharsets.UTF_8));

            System.out.println("Exponent: " + exponent);
            System.out.println("Message: " + message);
            System.out.println("Signature: " + signature);

            byte[] sigBytes = Base64.getDecoder().decode(signature);
            return sig.verify(sigBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public String generateChallenge() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[128];
        random.nextBytes(bytes);
        System.out.println("Challenge: " + Base64.getEncoder().encodeToString(bytes));
        return Base64.getEncoder().encodeToString(bytes);
    }

    public boolean verifyChallenge(String username, String solve) throws DatabaseException {
        UserTable userTable = new UserTable();
        User user = userTable.getUser(username);
        AuthTable authTable = new AuthTable();
        String challenge = authTable.getToken(username);
        authTable.deleteToken(username);
        return verifySignature(challenge, solve, user.getExponent(), user.getModulus());
    }

}
