package Client;

/**
 * Classe qui permet le lancement d'une connexion cliente.
 */
public class MainClient {

    public static void main(String[] args) {
        int port = 1540;
            Thread t = new Thread(new ClientConnexion("127.0.0.1", port));
            t.start();

    }
}