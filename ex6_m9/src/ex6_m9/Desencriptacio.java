package ex6_m9;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class Desencriptacio {
    public static void main(String[] args) 
            throws InvalidKeySpecException, NoSuchAlgorithmException, 
            IOException, NoSuchProviderException{
        
        SecretKey sKey;
        
        String resultado;
        
        byte[] arrayPrivada, missatgeencrip , clauencrip , arraydecrypkey, 
                arraydecryp;
        arrayPrivada = Files.readAllBytes(Paths.get("clauPRIVADA"));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

    String privateKeyContent = new String(arrayPrivada);
    privateKeyContent = privateKeyContent
        .replace("—-BEGIN RSA PRIVATE KEY—-", "")
        .replace("—-END RSA PRIVATE KEY—-", "")
        .replace("\n", "");

    byte[] privateKeyDecoded = Base64.getDecoder()
        .decode(privateKeyContent);

    PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyDecoded);
    PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
    
    clauencrip = Files.readAllBytes(Paths.get("ZZZ_clau_encriptada"));
    missatgeencrip = Files.readAllBytes(Paths.get("ZZZ_missatge_encriptat"));
    
    arraydecrypkey = decryptKey (clauencrip , privateKey);
    sKey = new SecretKeySpec(arraydecrypkey, 0, arraydecrypkey.length , "AES");
    
    arraydecryp = decryptData(sKey, missatgeencrip);
    
    resultado = new String (arraydecryp);
    
    System.out.println("La frase desencriptada es : " + resultado);
    
   
    }
    
    
public static byte[] decryptKey(byte[] array, PrivateKey priv) throws NoSuchProviderException {
    byte[] encryptedData = null;
    try {
    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding","SunJCE");
    cipher.init(Cipher.DECRYPT_MODE, priv);
    encryptedData = cipher.doFinal(array);
    }
    catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
    System.err.println("Error xifrant les dades: " + ex);
    }
    return encryptedData;
}

public static byte[] decryptData(SecretKey sKey, byte[] data) {
    byte[] encryptedData = null;
    try {
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, sKey);
    encryptedData = cipher.doFinal(data);
    }
    catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
    System.err.println("Error xifrant les dades: " + ex);
    }
    return encryptedData;
}
}
    
