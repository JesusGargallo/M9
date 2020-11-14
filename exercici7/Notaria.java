package ex7_m9;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Scanner;

public class Notaria {
    public static void main(String[] args) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException{
        Scanner sc = new Scanner(System.in);
        
        boolean boo;
        
        String publica;
        
        byte[] array, arraysignature, arraypublica;
        
        System.out.println("Nombre del archivo de la clave publica: ");
        publica = sc.nextLine();
        arraypublica = Files.readAllBytes(Paths.get(publica));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        
        // esto para quitar el begin y el end poner nada
        String publicKeyContent = new String(arraypublica);
        publicKeyContent = publicKeyContent
                .replace("—-BEGIN RSA PUBLIC KEY—-", "")
                .replace("—-END RSA PUBLIC KEY—-", "")
                .replace("\n", "");

        byte[] publicKeyDecoded = Base64.getDecoder()
                .decode(publicKeyContent);
        

        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyDecoded);
        PublicKey publicKey;
        publicKey = keyFactory.generatePublic(publicKeySpec);
        
        array = Files.readAllBytes(Paths.get("missatge"));
        arraysignature = Files.readAllBytes(Paths.get("firma"));
        
        boo = validateSignature(array, arraysignature, publicKey);
        
        if(boo){
            System.out.println("Comprovant signatura de l’arxiu missatge...OK");
        }else{
            System.out.println("Error....");
        }
        
    }

    
    public static boolean validateSignature(byte[] data, byte[] signature, PublicKey pub) {
        boolean isValid = false;
        try {
            Signature signer = Signature.getInstance("SHA1withRSA");
            signer.initVerify(pub);
            signer.update(data);
            isValid = signer.verify(signature);
        }
        catch (Exception ex) {
            System.err.println("Error validant les dades: " + ex);
        }
        return isValid;
    }
}


