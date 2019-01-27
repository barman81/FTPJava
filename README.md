# Serveur FTP en JAVA

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
   Aide destinée aux développeurs pour savoir comment le coeur de l'application fonctionne. Pour y accdéder, il faut lancer le fichier index.html disponible dans le dossier /Javadoc.


## Comment l'utiliser ?
### Côté serveur :
1 - Démarrer le serveur ftp disponible dans FTPServeur 

2 - Le serveur va vous répondre dans la console : "Bievenue sur le serveur FTP de Maël & Nicolas.
Votre serveur FTP est maintenant initialisé. Je suis en attente d'un ou des clients... ;-) !"

3- 

### Côté client :
1 - Démarrer le client ftp disponible dans FTPClient

2 - Le client doit répondre dans la console : "Vous avez le droit aux commandes RETR, STOR, DELE, QUIT"

3- Vous pouvez ensuite executer une des commandes affichées.

4-

## Fait Avec : 

* JAVA
* Intellj

## Auteurs :

* **Maël Baron**
* **Nicolas Guitard**

