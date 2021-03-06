package juxo.triephotoV2.accessFichier;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import juxo.UiTriePhotoV2.UiQuestion;
import juxo.apiCalendar.Evenement;
import juxo.apiCalendar.Evenements;
import juxo.system.Parametrage;

public class MapDateFichiers extends HashMap<Calendar, Fichiers> {

	/**
	 * Default serial id
	 */
	private static final long serialVersionUID = 6L;

	/**
	 * Déplace tous les fichiers en indiquant le nom du dossier de destination
	 * Les fichiers sont triés dans ANNEE/MOIS/NomDossierDestination
	 * @param nomDossier
	 */
	public void trieFichiers(String nomDossierDestination){
		for(Fichiers lesFichiers : this.values()){
			lesFichiers.deplacerTousLesFichier(nomDossierDestination);
		}
	}
	 
	/**
	 * Déplace tous les fichiers par date du jour de prise de vue
	 * Les fichiers sont triés dans ANNEE/MOIS/JOUR
	 * @param nomDossier
	 */
	public void trieFichiersDateJour(String nomDossierDestination){
		for(Fichiers lesFichiers : this.values()){
			lesFichiers.deplacerTousLesFichierDateJour(nomDossierDestination);
		}
	}
	
	/**
	 * Trie tous les fichiers qu'il peut par lieu de prise de vue
	 * Les fichiers sont triés dans ANNEE/MOIS/Lieu
	 * @param nomDossierDestination
	 */
	public void trieFichiersParLieu(String nomDossierDestination){
		ArrayList<Calendar> listeDesObjetsSupprimer = new ArrayList<Calendar>();
		Iterator<Calendar> it = this.keySet().iterator();
		while(it.hasNext()){
			Calendar maClefDeListe = it.next();
			Fichiers LesFichiers = Fichier.listFic.get(maClefDeListe);
			LesFichiers.deplacerTousLesFichiersParLieu(nomDossierDestination);
			if(LesFichiers.size()==0){
				listeDesObjetsSupprimer.add(maClefDeListe);
			}
		}
		supprimerDesEntrees(listeDesObjetsSupprimer);
	}
	
	/**
	 * Trie tous les fichiers qu'il peut par date d'événement
	 * Et nomme le dossier selon la date d'un événement
	 */
	public void trieFichiersParEvenement(String nomDossierDestination){
		Iterator<Calendar> it = this.keySet().iterator();
		ArrayList<Calendar> listeDesObjetsSupprimer = new ArrayList<Calendar>();
		while(it.hasNext()){
			Calendar maClefDeListe = it.next();
			Fichiers listFichier = Fichier.listFic.get(maClefDeListe);
			Evenements listEv = Evenement.searchDateEvenement(maClefDeListe);
			if(listEv!=null){
				SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
				UiQuestion ui = new UiQuestion(null, format1.format(maClefDeListe.getTime()), true, listEv, listFichier);
				Evenement evenement = ui.showUiQuestion();
				if(evenement!=null && !(evenement.nomEvenement.equals("aucun"))){
					listFichier.deplacerTousLesFichier(Parametrage.getInstance().getDossierDestination(), evenement.getNomPropre());
					listeDesObjetsSupprimer.add(maClefDeListe);
				}
			}
		}
		supprimerDesEntrees(listeDesObjetsSupprimer);
	}
	
	/**
	 * Supprime de la liste de fichiers à déplacer
	 * les fichiers passés en paramètre
	 * @param l
	 */
	public void supprimerDesEntrees(ArrayList<Calendar> l){
		for(Calendar c : l){
			this.remove(c);
		}
	}
	
	/**
	 * Renvoie une collection de fichiers indépendamment de la map
	 * @return
	 */
	public Fichiers getAllFichierItem(){
		Fichiers newList  = new Fichiers();
		for(Fichiers lesFichiers : this.values()){
			for(Fichier fic : lesFichiers){
				newList.add(fic);
			}
		}
		return newList;
	}
	
	/**
	 * Permet de renvoyer la liste de fichiers concernant une date
	 * @param d
	 * @return
	 */
	public Fichiers searchExistDate(Calendar d){
		Iterator<Calendar> it = this.keySet().iterator();
		boolean trouve = false;
		Fichiers ficList = null;
		while(it.hasNext() && !trouve){
			Calendar key = it.next();
			if(		d.get(Calendar.DAY_OF_MONTH) == key.get(Calendar.DAY_OF_MONTH) &&
					d.get(Calendar.MONTH) == key.get(Calendar.MONTH) &&
					d.get(Calendar.YEAR) == key.get(Calendar.YEAR)){
				trouve = true;
				ficList = this.get(key);
			}
		}
		return ficList;
	}
	
	/**
	 * Chargement d'une liste de fichier à partir d'un dossier de base
	 * On parcour le dossier sans se soucier de son arborescence
	 * @param Nwxdossier
	 * @throws IOException
	 */
	
	public static void listFichier(File[] listeFichiers) throws IOException {
		//On parcours tous les fichiers
		Fichier monfic = null;
		for (File fic : listeFichiers) {
			if(!(fic.isHidden())){
				monfic = new Fichier(fic.getPath());
			} 
			if (monfic!= null && monfic.isDirectory()){
				listFichier(monfic.listFiles());
			}
		}
	}
	
	/***
	 * Création d'un objet fichiers avec sa liste de fichiers format String
	 * @param dossier
	 * @throws IOException
	 */
	public static void listFichier(Fichier dossier) throws IOException {
		listFichier(dossier.listFiles());
	}
	
}
