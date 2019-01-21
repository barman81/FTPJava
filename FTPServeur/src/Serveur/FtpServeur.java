package Serveur;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class FtpServeur {
    private String ip="127.0.0.1";
    private int port = 1234;
    private ServerSocket socketServeur;
    private int fileAttente = 100;

    /**
     * Constructeur par défaut d'un serveur FTP.
     */
    public FtpServeur(){
        this.port = getFreePort();
        this.socketServeur=setSocket(port, fileAttente);
    }

    /**
     * Contructeur d'un serveur FTP en lui précisant un numéro de port.
     * @param port
     */
    public FtpServeur(int port) {
        this.port = port;
        this.socketServeur = setSocket(this.port,fileAttente);
    }

    /**
     * Méthode qui verifie si le port est disponible. Si le port n'est pas disponible, il est décrementé de -1.
     * @return port
     */
    public int getFreePort() {
        int p=port;
        ServerSocket Socket=null;
        for(; p <= 65535&&Socket==null; p++){
            try {
                Socket= new ServerSocket(p);
            } catch (IOException e) {
                System.err.println("Le port " + port + " est déjà utilisé ! Le port va changer automatiquement");
            }
        }
        System.out.println(p);
        return p;
    }

    /**
     * Méthode qui crée une nouvelle socket.
     * @param p
     * @param file
     * @return ServerSocket
     */
    public ServerSocket setSocket(int p,int file) {
        ServerSocket sock=null;
        try{
            sock=new ServerSocket(p,file);
        }
        catch(IOException e) {

        }
        return sock;
    }

    /**
     *
     * Méthode qui retourne l'adresse du serveur FTP et son port.
     * @return Adresse IP du serveur FTP et son port.
     */
    public String toString() {
        return "Adresse du serveur FTP : " + ip + ":"+port;
    }

    /**
     *
     */
    public void open(){
//Execution dans un thread à part vu qu'il est dans une boucle infinie.
        boolean isRunning = true;
        Thread theThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(isRunning) try {
                    Socket unClient = socketServeur.accept();
                    //Une fois la demande reçue, il faut la traiter dans un thread appart afin de pouvoir traiter plusieurs clients à la fois.
                    System.out.println("Connexion d'un client reçu.");
                    Thread monThread = new Thread(new ClientProcessor(unClient));
                    monThread.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    socketServeur.close();
                } catch (IOException e){
                    e.printStackTrace();
                    socketServeur = null;
                }
            }
        });
        theThread.start();
    }


    /**
     * Méthode Main. Permet le lancement du programme.
     * @param args
     */
    public static void main(String[]args) {
        FtpServeur serveur=new FtpServeur();
        System.out.println(serveur.toString());
        serveur.open();
    }
}


