package Serveur;

/**
 * Classe qui permet le lancement du Serveur FTP
 */
public class MainServeur {

    public static void main(String[] args) {
        System.out.println("Bievenue sur le serveur FTP de Maël & Nicolas");
        System.out.println("-----------------------------------------------");

        int port = 1540;
        FtpServeur unFTP = new FtpServeur(port);
        unFTP.open();

        System.out.println("Votre serveur FTP est maintenant initialisé \n Je suis en attente de clients... ;-) !");


//        for(int i = 0; i < 1; i++){
//		         Thread t = new Thread(new ClientConnexion("127.0.0.1", port));
//		         t.start();
//		      }
    }
    }

