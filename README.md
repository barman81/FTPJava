# Client/Serveur FTP en JAVA

Serveur FTP multi-thread utilisant les sockets.
Plusieurs clients peuvent se connecter au même serveur FTP.

## Pour Commencer : 
### Structure du projet :
Ce projet contient 2 sous projets : 
   ### FTPClient :
   Ce dossier est le client FTP.
   
   Il contient un dossier package " Client " avec :
   - ClientConnexion.java
   - MainClient.java
   
   ### FTPServeur :
   Ce dossier est le Serveur FTP.
   
   Il contient un dossier package " Serveur " avec :
   - ClientProcessor.java
   - FtpServeur.java
   - MainServeur.java
   
   ### Javadoc :
   Aide destinée aux développeurs pour savoir comment le coeur de l'application fonctionne. Pour y accéder, il faut lancer le fichier index.html disponible dans le dossier /Javadoc.


## Comment l'utiliser ?

### A Savoir :
Par défaut, le Serveur est initialisé sur le port : 1531.
Le client se connecte donc aussi sur le port 1531.

L'ip par défaut est 127.0.0.1

Le serveur et le client communique donc via : 127.0.0.1:1531 

### Côté serveur :
1 - Démarrer le serveur ftp disponible dans FTPServeur. (Il faut lancer le fichier MainServeur.)

2 - Le serveur va vous répondre dans la console : "Bievenue sur le serveur FTP de Maël & Nicolas.
Votre serveur FTP est maintenant initialisé. Je suis en attente d'un ou des clients... ;-) !"

3 - Laisser le serveur tourner.

### Côté client :
1 - Démarrer le client ftp disponible dans FTPClient. (Il faut lancer le fichier MainClient.)

2 - Le client doit répondre dans la console : "Vous avez le droit aux commandes RETR, STOR, DELE, QUIT".

3 - Vous pouvez ensuite exécuter une des commandes affichées.

4 - Une fois terminé, exécutez la commande "QUIT". Elle arrêtera le client (tout en veillant à le déconnecter de la socket et à la fermer).

## Fait Avec : 

* JAVA
* Intellj

## Auteurs :

* **Maël Baron**
* **Nicolas Guitard**

