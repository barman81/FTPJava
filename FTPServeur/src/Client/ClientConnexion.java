package Client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Classe qui permet de gérer l'ensemble d'une connexion au serveur FTP.
 */
public class ClientConnexion implements Runnable{
    /**
     * Socket qui permet de se connecter au serveur.
     */
    private Socket connexion = null;

    /**
     *
     */
    private PrintWriter writer = null;

    /**
     *
     */
    private BufferedInputStream reader = null;

    /**
     * Permet de recuperer la saisie du clavier de la console.
     */
    private Scanner sc = new Scanner(System.in);


    /**
     * Liste des commandes disponibles pour notre serveur. Selon la commande utilisee il ne ferra pas la meme chose.
     */
    private ArrayList<String> listCommands = new ArrayList<String>() ;

    /**
     * Compteur initialise par defaut a 0. Permet de stocker le nombre de client connecte.
     */
    private static int count = 0;

    /**
     * Permet de nommer le client en cour.
     */
    private String name = "Client-";

    /**
     * Chemin de stockage des documents telecharges.
     */
    private String path="/tmp/result";

    /**
     * Constructeur d'un ClientConnexion.
     * @param host Adresse Ip du serveur
     * @param port Port du serveur
     */
    public ClientConnexion(String host, int port){
        FillCommandes();
        name += ++count;
        try {
            connexion = new Socket(host, port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Methode qui permet d'ajouter une commande a la liste des commandes disponibles.
     */
    private void FillCommandes() {
        listCommands.add("RETR");
        listCommands.add("STOR");
        listCommands.add("DELE");
        listCommands.add("QUIT");
    }

    /**
     * Methode qui lance un thread Client pour se connecter au serveur FTP.
     */
    public void run(){
        //limite de connexion 10.
        for(int i =0; i < 10; i++){
            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                writer = new PrintWriter(connexion.getOutputStream(), true);
                reader = new BufferedInputStream(connexion.getInputStream());
                //On envoie la commande au serveur
                String commande = getCommand();
                //String file=getFile();
                writer.write(commande);
                writer.flush();
                System.out.println("Commande " + commande + " envoyée au serveur");
                //On attend la réponse
                String response = read();
                System.out.println("\t * " + name + " : Réponse reçue " + response);
                switch(commande) {
                    case "RETR"://récupération de fichier du serveur ftp
                        writeFile();
                        break;
                    case "STOR": // envoie de fichier sur le serveur
                        sendFile();
                        break;
                    case "DELE":
                        deleteFile();
                        break;
                    case "QUIT":
                        displayEndMess();
                        break;
                    default :
                        commande = "Commande inconnu !";
                        break;
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //send("CLOSE");
        writer.write("Close");
        writer.flush();
        writer.close();
    }

    /**
     * Methode qui permet d'envoyer une commande passee en paramètre.
     * @param str commande à envoyer.
     */
    public void send(String str) {
        writer.write(str);
        writer.flush();
    }

    /**
     * Permet d'afficher un message.
     * @param str
     */
    public void displayMessage(String str) {
        //fenetre.getInfoConnexion().setText(str);
        System.out.println(str);
    }

    /**
     *
     * @throws IOException
     */
    public void deleteFile() throws IOException {
        String file="";
        boolean r=true;
        while(r) {
            file=sc.nextLine();
            if(!file.equals("")) {
                r=false;
            }
        }
        deleteFile(file);
    }

    /**
     * Methode qui permet de suppirmer un fichier du serveur.
     * @param file nom du fichier à supprimer.
     */
    public void deleteFile(String file) {
        send(file);
    }

    /**
     * Affiche un message de fin.
     * @throws IOException
     */
    public void displayEndMess() throws IOException {
        String endMess=read();
        displayMessage(endMess);
    }

    /**
     * Envoie de fichier au serveur
     * @throws IOException
     */
    public void sendFile() throws IOException {
        File file=getFileSend();//envoie du chemin complet
        String toSend= readFile(file);
        send(toSend);
        if(!toSend.equals("")) {
            System.out.println("Fichier envoyé");
        }
        else {
            System.out.println("Fichier  non envoyé");
        }
    }


    /**
     * Envoie le fichier par la socket
     * @param file
     * @throws IOException
     */
    public void sendFile(File file) throws IOException {
        send(file.getName());
        String toSend=readFile(file);
        send(toSend);

    }

    /**
     * Lecture du contenu du fichier pour l'envoi
     * @param nomFile
     * @return
     * @throws IOException
     */
    private String readFile(File nomFile) throws IOException {
        String str="";
        if(nomFile.exists()) {
            FileInputStream file=new FileInputStream(nomFile);
            int lettre;
            while((lettre=file.read())!=-1) {
                str+=(char)lettre;
            }
        }
        return str;
    }

    /**
     * Recupere les donnees du fichier envoyes.
     * @return File nom du fichier demande.
     */
    private File getFileSend() {
        String reponse = "";
        boolean encore = true;
        while (encore) {
            System.out.println("Saisir un fichier");
            reponse = sc.nextLine();
            if(!reponse.equals("")) {
                encore=false;
            }
        }
        File file = new File(reponse);
        send(file.getName());
        return file;
    }

    /**
     * Recuperation du texte du fichier
     * @return String contenu du fichier.
     * @throws IOException
     */
    private String getFileText() throws IOException {
        String strTextFile="";
        strTextFile=read();
        return strTextFile;
    }

    /**
     * Recuperation de ??
     * @return String
     */
    private String getFile() {
        String reponse = "";
        boolean encore = true;
        while (encore) {
            System.out.println("Saisir un fichier");
            reponse = sc.nextLine();
            if(!reponse.equals("")) {
                encore=false;
            }
        }

        return reponse;
    }

    /**
     *
     * @throws IOException
     */
    private void writeFile() throws IOException {
        String fileName=getFile();
        writeFile(fileName);
    }

    /**
     * Methode qui permet d'écrire un fichier. Peut retourner une exception du type IOException.
     * @param fileName Nom du fichier.
     * @throws IOException
     */
    public void writeFile(String fileName) throws IOException {
        send(fileName);
        String fileText=getFileText();
        File file=new File(path+"/"+fileName);
        FileOutputStream output;
        output = new FileOutputStream(file);
        for(int i=0;i<fileText.length();i++) {
            int c=(int) fileText.charAt(i);
            output.write(c);
        }
        output.close();
    }



    /**
     * Methode qui permet d'envoyer la commande qu'on rentre.
     * @return String laCommande
     */
    private String getCommand(){
        boolean encore=true;
        String reponse = "";
        while (encore) {
            displayMessage("Vous avez le droit aux commande "+ getListCommandes());
            reponse = sc.nextLine().toUpperCase();
            if(listCommands.contains(reponse)){
                encore=false;
            }
        }
        return reponse;
    }


    /**
     * Retourne la liste des commandes disponibles.
     * @return String commande
     */
    public String getListCommandes() {
        String commande="";
        for(String str : listCommands) {
            commande+=str+", ";
        }
        commande+="\n\n";
        return commande;
    }


    /**
     * Methode pour lire les reponses envoyees par le serveur.
     * @return
     * @throws IOException
     */
    public String read() throws IOException{
        byte[] b = new byte[4096];
        int stream = reader.read(b);
        System.out.println(new String(b, 0, stream));
        return new String(b, 0, stream);
    }






}