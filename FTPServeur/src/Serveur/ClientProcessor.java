package Serveur;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class ClientProcessor implements Runnable {
    /**
     * Socket qui permettra de se connecter au serveur.
     */
    private Socket socketClient;
    /**
     * Permet d'écrire
     */
    private PrintWriter writer = null;
    /**
     * Permet de lire le fichier.
     */
    private BufferedInputStream reader = null;

    /**
     * Dossier de base du ftp lors de la connexion. Par défaut : "/tmp/base"
     */
    private String pathCourant = "/tmp/base";
    /**
     * Liste des fichiers exclus des transferts. Pour l'instant la liste est vide.
     */
    private ArrayList<String> fichierExclus = new ArrayList<String>();

    /**
     * Constructeur qui permet d'instancier le traitement d'un nouveau client.
     * @param theSocket Socket qui doit se connecter
     */
    public ClientProcessor(Socket theSocket) {
        socketClient = theSocket;
    }

    /**
     * Méthode qui permet le lancement du traitement d'une connexion cliente.
     */
    public void run() {
        System.out.println("Lancement du traitement de la connexion cliente !");
        boolean closeConnexion = false;
        while (!socketClient.isClosed()) {
            try {
                writer = new PrintWriter(socketClient.getOutputStream());
                reader = new BufferedInputStream(socketClient.getInputStream());
                String answer = read();
                InetSocketAddress remote = (InetSocketAddress) socketClient.getRemoteSocketAddress();
                String debug = "";
                debug = "Thread : " + Thread.currentThread().getName() + ". ";
                debug += "Demande de l'adresse : " + remote.getAddress().getHostAddress();
                debug += " Sur le port (localport de la socket) : " + remote.getPort() +"\n";
                debug += "\t -> Commande reçue : " + answer + "\n";
                System.err.println("\n" + debug);

                //On traite la demande du client en fonction de la commande envoyée
                String toSend = "";
                switch (answer.toUpperCase()) {
                    //Commande pour faire une demande de récupération de fichier
                    case "RETR":
                        sendFile();
                        break;
                    //Commande pour faire une demande de transfert de fichier
                    case "STOR":
                        loadFile();
                        break;
                    //Commande pour faire une demande de suppression de fichier
                    case "DELE":
                        deleteFile();
                        break;
                    //Commande pour faire une fin de transfert ou abandon la connexion.
                    case "QUIT":
                        toSend = "Communication terminée";
                        closeConnexion = true;
                        break;
                    default:
                        toSend = "Commande inconnu !";
                        break;
                }
                send(toSend);
                if (closeConnexion) {
                    System.err.println("La commande close est détectée. \n La socket est fermée. \n Le Client est arrêté.");
                    writer = null;
                    reader = null;
                    socketClient.close();
                    break;
                }
            } catch (SocketException e) {
                System.err.println("La Connexion à ete interrompue");
                e.printStackTrace();
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Méthode qui détruit un fichier.
     * @throws IOException
     */
    private void deleteFile() throws IOException {
        String files=displayFile();
        send(files +" \n Quel fichier voulez vous supprimer ? \n");
        String answer=read();
        File file=isExist(answer, getFileCourant());
        boolean reponse  = file.delete();
        if(reponse) {
            System.out.println("Réussi");
            writer.write("Réussi");
        }
        else {
            System.out.println("Echec");
            writer.write("Echec");
        }
        writer.flush();
    }


    /**
     * Méthode qui permet de lire le contenu d'un fichier.
     * @throws IOException
     */
    private void loadFile() throws IOException {
        String fileName=getFileName();
        String fileText=getFileText();
        String path="/tmp";
        createFile(fileName, fileText, path);

    }

    /**
     * Méthode qui permet la création d'un fichier en utilisant le path courant.
     * @param fileName Nom du fichier.
     * @param fileText Contenu du fichier.
     * @param path Dossier dans lequel on exporte.
     * @throws FileNotFoundException
     * @throws IOException
     */

    private void createFile(String fileName, String fileText, String path) throws FileNotFoundException, IOException {
        File file=new File(path+"/"+fileName);
        FileOutputStream output;
        output = new FileOutputStream(file);
        for(int i=0;i<fileText.length();i++) {
            int c=(int) fileText.charAt(i);
            output.write(c);
        }
        System.out.println("Fichier reçu");
        output.close();
    }

    /**
     * Récupère le contenu du fichier. Utilise la méthode read.
     *
     * @throws IOException
     */

    private String getFileText() throws IOException {
        send("envoie texte");
        String fileText=read();
        return fileText;
    }

    /**
     *
     * @return fileName Nom du fichier
     * @throws IOException
     */
    private String getFileName() throws IOException {
        send("Saisir un  fichier : ");
        String fileName=read();
        return fileName;
    }

    /**
     * Permet d'envoyer un fichier.
     */
    private void sendFile() {
        String answer="";
        String strFile="";
        String toSend=displayFile();
        File file=null;
        send(toSend);
        try {
            answer=read();
            file=isExist(answer,getFileCourant());
            strFile=readFile(file);

        } catch (IOException e) {
            System.out.println("Erreur de connexion");
            e.printStackTrace();
        }
        send(strFile);
        if(file!=null) {
            System.out.println("Fichier envoyé");
        }
    }

    /**
     *
     * @param str
     */
    private void send(String str) {
        writer.write(str);
        writer.flush();
    }

    /**
     *
     * @return
     */
    private String displayFile() {
        File[] files=getFileCourant();
        String str="";
        for(File f : files) {
            if(!fichierExclus.contains(f.getName())) {
                str+=f.getName()+"\n";
            }
        }
        return str;
    }

    /**
     * Récupère les fichiers du répertoire courant.
     * @return
     */
    private File[] getFileCourant() {
        File repertoire = new File(pathCourant);
        File[] files=repertoire.listFiles();
        return files;
    }

    /**
     * Recherche si le fichier existe.
     * @param answer
     * @param files Liste des fichiers.
     * @return
     */
    private File isExist(String answer, File[] files) {
        System.out.println(answer);
        for(File f : files) {
            System.out.println(answer);
            System.out.println(f.getName());
            if(f.getName().equals(answer)) {
                System.out.println(f.getName());
                return f;
            }
        }
        return null;
    }

    /**
     * Lit le fichier
     * @param theFile Nom du fichier.
     * @return contenu Contenu du fichier.
     * @throws IOException
     */
    private String readFile(File theFile) throws IOException {
        String contenu="";
        if(theFile.exists()) {
            FileInputStream file=new FileInputStream(theFile);
            int lettre;
            while((lettre=file.read())!=-1) {
                contenu+=(char)lettre;
            }
        }
        return contenu;
    }

    /**
     * Permet d'écrire un fichier. Utilise FileOutputStream.
     * @param theFile Nom du fichier
     * @param text Contenu du fichier
     * @throws IOException
     */
    private void writeFile(File theFile,String text) throws IOException {
        FileOutputStream output=new FileOutputStream(theFile);
        for(int i=0;i<text.length();i++) {
            int c=(int) text.charAt(i);
            output.write(c);
        }
    }

    /**
     * Méthode qui permet de lire un fichier via les bytes. Bytes maximum de 4096.
     * @return
     * @throws IOException
     */
    private String read() throws IOException{
        byte[] b = new byte[4096];
        int stream= reader.read(b);
        String response = new String(b, 0, stream);
        return response;
    }
}

