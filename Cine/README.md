Cette application est un site de critiques cin�ma.
Dans la classe Jobs.java(5.) : l'envoi d'un fichier excel toute les 5 minutes contenant la liste des films du site.

Utilisation de la librairie jsoup pour r�cup�rer 10 films � l'affiche sur allocin�.
Et cr�ation des fiches des films � l'affiche en r�cup�rant les donn�es sur allocin�.(Controllers "Application" derni�re m�thode "filmsAffiche()"

1. Installer play 1.2.5

2. Ajout de la librairie jsoup dans l'installation de play:
   Placez vous dans le dossier cinecritique et lancer la commande :
	  play deps --verbose
	  
   puis revenez dans le dossier au-dessus: 
	  cd .. 

3. Pour ouvrir le projet sur eclipse:
   Lancer la commande: 
	  play eclipsify cinecritique 
	
4. Modifier le fichier cinecritique\app\controllers\Jobs.java :
		ligne 59: � modifier si vous utiliser un autre serveur smtp
		ligne 60: identifiant et mot de passe de votre compte mail.

5.(facultatif) Configuration de la base de donn�e:
	Dans le fichier : cinecritique\conf\application.conf 
	actuellement (ligne 85): db=mem (base de donn�es en m�moire)	
			
6. Lancer la commande: play run cinecritique

7. Dans le navigateur: localhost:9000
