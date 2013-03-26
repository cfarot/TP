Cette application est un site de critiques cinéma avec dans la classe Jobs.java(4.) l'envoi d'un fichier excel toute les 5 minutes contenant la liste des films du site.

1. Installer play 1.2.5

2. Ajout du module Excel à votre installation de play:
   utiliser la commande : play install excel-1.2.3x

3. Pour ouvrir le projet sur eclipse:
	lancer la commande: play eclipsify cinecritique
	
4. Modifier le fichier TP Excel&Mail\cinecritique\app\controllers\Jobs.java :
		ligne 59: à modifier si vous utiliser un autre serveur smtp
		ligne 60: identifiant et mot de passe de votre compte mail.

(facultatif) 5. Configuration de la base de donnée:
	Dans le fichier : C:\Users\cfarot\Desktop\TP Excel&Mail\cinecritique\conf\application.conf 
	actuellement (ligne 85): db=mem (base de données en mémoire)	
			
6. lancer la commande: play run cinecritique

7. Dans le navigateur: localhost:9000