package juxo.triephotoV2.accessFichier;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Fichiers extends ArrayList<Fichier>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private File[] listeFichiers;

	/***
	 * Création d'un objet fichiers avec sa liste de fichiers format String
	 * @param dossier
	 * @throws IOException
	 */
	public static void generationListe(Fichier dossier) throws IOException {
		listFichier(dossier.listFiles());
	}

	/***
	 * Création d'un objet fichiers avec sa liste de fichier format objet Fichier
	 * En fait le fichier est un dossier
	 * @param dossier
	 * @throws IOException
	 */
	/*public Fichiers(Fichier dossier) throws IOException {
		listeFichiers = dossier.listFiles();
	}

	public Fichiers() {
		listeFichiers = null;
	}*/

	public void deplacerTousLesFichier(String Nwxdossier, String nomDossier){
		for(Fichier f : this){
			f.Deplacer(Nwxdossier, nomDossier);
		}
	}
	
	public void deplacerTousLesFichier(String Nwxdossier){
		for(Fichier f : this){
			f.Deplacer(Nwxdossier);
		}
	}
	
	public static void renommerFichiers(File[] listeFichiers){
		int it=1;
		//On parcours tous les fichiers
		for (File fichierCourant : listeFichiers) {
			if (fichierCourant.isFile()) {
				//System.out.println(it);
				//System.out.println(getFileExtension(fichierCourant));
				Fichier monfic = new Fichier(fichierCourant.getPath());
				monfic.renommerFichier("fichier-" + it++);
			}
		}
	}
	
	
	/***
	 * Trie des fichiers contenus dans un dossier
	 * On parcour le dossier sans se soucier de son arborescence
	 * @param Nwxdossier
	 * @throws IOException
	 */
	public static void listFichier(File[] listeFichiers) throws IOException {
		//On parcours tous les fichiers
		for (File fic : listeFichiers) {
			Fichier monfic = new Fichier(fic.getPath());
			System.out.println(monfic);
			if (monfic.isDirectory()) {
				listFichier(monfic.listFiles());
			}

		}
	}
}
