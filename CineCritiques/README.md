Cette application est un site de critiques cinéma.
Dans la classe Jobs.java(5.) : l'envoi d'un fichier excel toute les 5 minutes contenant la liste des films du site.

Utilisation de la librairie jsoup pour récupérer 10 films à l'affiche sur allociné.
Et création des fiches des films à l'affiche en récupérant les données sur allociné.(Controllers "Application" dernière méthode "filmsAffiche()"

1. Installer play 1.2.5

2. Ajout de la librairie jsoup dans l'installation de play:
   Placez vous dans le dossier cinecritique et lancer la commande :
	  play deps --verbose
	  
   puis revenez dans le dossier au-dessus: 
	  cd .. 

3. Pour ouvrir le projet sur eclipse:
   Lancer la commande: 
	  play eclipsify cinecritique 

4.(facultatif) Configuration de la base de donnée:
	Dans le fichier : cinecritique\conf\application.conf 
	actuellement (ligne 85): db=mem (base de données en mémoire)	
			
5. lancer la commande: play run cinecritique

6. Dans le navigateur: localhost:9000