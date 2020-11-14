package ex7_m9;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.System.out;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Scanner;
import java.util.logging.Level;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Signatura {

    public static void main(String[] args) throws IOException{
        Scanner sc = new Scanner(System.in);
        
        //las variables
        int numero = 512;
        String missatge;
        
        
        byte[] arraymissatge;
        byte[] arrayfirma;
        
        System.out.println("Generant claus publiques i provades "
                + "(arxius clauPublica i clauPrivada)...OK");
        
        System.out.println("Introduce el mensaje a signar: ");
        missatge = sc.nextLine();
        arraymissatge = missatge.getBytes();
        
        KeyPair keys;
        keys = randomGenerate(numero);
        PrivateKey privatekey = keys.getPrivate();
        PublicKey publickey = keys.getPublic();
        
        
       
        // private
        
        //esto para crear la clau privada
        Base64.Encoder encoder = Base64.getEncoder();
        try (FileWriter out = new FileWriter("clauPRIVADA")) {
        out.write("—-BEGIN RSA PRIVATE KEY—-");
        out.write("\n");

        out.write(encoder.encodeToString(privatekey.getEncoded()));
        out.write("\n");

        out.write("—-BEGIN RSA PRIVATE KEY—-");
        out.write("\n");
        out.close();
        
        } catch (IOException ex) {
           System.out.println("No se ha guardado la llave privada"); 
        }
        
        //para crear la clau publica
        try (FileWriter out = new FileWriter("clauPublica")) {
        out.write("—-BEGIN RSA PUBLIC KEY—-");
        out.write("\n");

        out.write(encoder.encodeToString(publickey.getEncoded()));
        out.write("\n");

        out.write("—-END RSA PUBLIC KEY—-");
        out.write("\n");
        out.close();
        
        } catch (IOException ex) {
            System.out.println("No se ha guardado la llave publica");
        }
        
        //crear misastge 
        try (FileOutputStream fos = new FileOutputStream("missatge")) {

        fos.write(arraymissatge);
        fos.close();
        
        } catch (IOException ex) {
           System.out.println("No se ha guardado el mensaje"); 
        }
        
        // crear la firma
        try (FileOutputStream fos = new FileOutputStream("firma")) {
        arrayfirma = signData(arraymissatge, privatekey);
        fos.write(arrayfirma);
        fos.close();
        
        } catch (IOException ex) {
           System.out.println("No se ha guardado el mensaje"); 
        }
        
        
    }
    
    public static KeyPair randomGenerate(int longuitudClau) {
        KeyPair keys = null;
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(longuitudClau);
            keys = keyGen.genKeyPair();
        }
        catch (Exception ex) {
            System.err.println("Generador no disponible.");
        }
        return keys;
    }
    
    public static byte[] signData(byte[] missatgeBytes, PrivateKey clauPrivadaText) {
        byte[] signature = null;
        try {
            Signature signer = Signature.getInstance("SHA1withRSA");
            signer.initSign(clauPrivadaText);
            signer.update(missatgeBytes);
            signature = signer.sign();
            System.out.println("Signant el missatge...OK");
            System.out.println("Generant arxiu firma_missatge...OK");
            System.out.println("Generant arxiu missatge...OK");
        }catch (Exception ex) {
            System.err.println("Error signant les dades: " + ex);
        }
        return signature;
        
    }
    
}


