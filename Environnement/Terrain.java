package Environnement;

import java.util.ArrayList;
import java.util.Iterator;

import Exceptions.HorsLimite;
import Exceptions.NoTerrain;
import Comportemental.Acteur;
import Comportemental.ActeurType;

/**
 * Plateau de la simulation, contient l'ensemble des "Case" et le mecanisme generale de simulation.
 * 
 * @author ALLAM Jonathan - <thomasallam@outlook.fr> -29/12/2017
 *
 */
public class Terrain {
	private Case[][] tableau ;						// Tableau de Case : etudiees pour la simulation.
	private int xMax ;								// Longueur du terrain
	private int yMax ;								// Hauteur du terrain
	private static Terrain uniqueInstance ;			// L'unique instance terrain. (Singleton)
	
	private ArrayList<Acteur> flammes ;				// Ensemble des flammes sur le terrain.
	private ArrayList<Acteur> pompiers ;			// Ensemble des pompiers sur le terrain.
	private ArrayList<Acteur> nouveauAct ;			// Ensemble des nouveaux acteurs a prendre en compte pour le prochain tour.
	private ArrayList<Acteur> acteurMort ;			// Ensemble des acteurs morts pendant ce tour.
	
	public static Meteo m ;							// Parametres meteo associes a la simulation actuelle.
	
	public FeuilleRes stat ;
	
	/**
	 * Constructeur par defaut du terrain.
	 */
	private Terrain() {
		this.flammes = new ArrayList<Acteur>() ;
		this.pompiers = new ArrayList<Acteur>() ;
		this.nouveauAct = new ArrayList<Acteur>() ;
		this.acteurMort = new ArrayList<Acteur>() ;
		
		this.stat = new FeuilleRes() ;
	}
	
	/**
	 * Constructeur du terrain, appelant le constructeur par defaut.
	 * Initialise la dimension du tableau de case.
	 * 
	 * @param xMax La longueur du tableau de cases.
	 * @param yMax La hauteur du tableau de cases.
	 */
	private Terrain(int xMax, int yMax) {
		this() ;
		
		this.tableau = new Case[yMax][xMax] ;
		
		int x, y ;
		for (x=0; x<xMax; x++) {
			for (y=0; y<yMax; y++) {
				try {
					this.tableau[y][x] = new Case(x, y, "Neutre") ; 
				} catch (Exception e) {}
			}
		}
		
		this.xMax = xMax ;
		this.yMax = yMax ;
	}
	
	/**
	 * Fonction de creation du terrain seulement si pas encore cree. ( Singleton )
	 * 
	 * @param xMax Abscisse maximale du plateau.
	 * @param j	Ordonnee maximale du plateau
	 * @return Renvoie le terrain ainsi cree si aucun n'existe deja, l'ancien sinon.
	 */
	public static Terrain create(int xMax, int yMax) {
		if (Terrain.uniqueInstance == null)
			return Terrain.uniqueInstance = new Terrain(xMax, yMax) ;
		else return Terrain.uniqueInstance ;
	}
	
	public static void reset() {
		Terrain.uniqueInstance = null ;
	}
	/**
	 * Supprime les acteurs morts pendant ce tour a la simulation. ( les supprimes des listes flammes ou pompiers )
	 */
	private void supprimerActeurs() {
		Iterator<Acteur> it = this.acteurMort.iterator() ;				
		Acteur a ;				// Acteur a supprimer.
		
		while (it.hasNext()) {				// Tant que la liste contient des acteurs
			a = it.next();							// Le recuperer
			
			if (a.getType() == ActeurType.Feu)				// Si "Feu"
				this.flammes.remove(a) ;							// Le supprimer de la liste des flammes de la simulation
			if (a.getType() == ActeurType.Pompier)			// Si "Pompier"
				this.pompiers.remove(a) ;							// Le supprimer de la liste des pompiers de la simulation
			
			it.remove() ;							// Supprimer des mort a enlever.
		}
	}
	
	/**
	 * Ajoute les acteurs crees pendant ce tour a la simulation. ( les ajoute dans les liste flammes ou pompiers )
	 */
	private void ajouterActeurs() {
		Iterator<Acteur> it = this.nouveauAct.iterator() ;
		Acteur a ;
		
		while (it.hasNext()) {
			a = it.next();
			
			if (a.getType() == ActeurType.Feu)
				this.flammes.add(a) ;
			if (a.getType() == ActeurType.Pompier)
				this.pompiers.add(a) ;
			
			it.remove() ;
		}
	}
	
	/**
	 * Ajoute un acteur a la liste acteurMort.
	 * 
	 * @param a Acteur a supprimer pour la suite de la simulation.
	 */
	public void supprimer(Acteur a) {
		this.acteurMort.add(a) ;
	}
	
	/**
	 * Ajoute un acteur a la liste nouveauAct.
	 * 
	 * @param a Acteur a ajouter pour la suite de la simulation.
	 */
	public void ajouter(Acteur a) {
		this.nouveauAct.add(a) ;
	}
	
	/**
	 * Accesseur a l'abscisse maximale du plateau.
	 * 
	 * @return Renvoie l'abscisse maximale du plateau de simulation.
	 */
	public int getXMax() {
		return this.xMax ;
	}
	/**
	 * Accesseur a l'ordonnee maximale du plateau.
	 * 
	 * @return Renvoie l'ordonnee maximale du plateau de simulation.
	 */
	public int getYMax() {
		return this.yMax ;
	}
	
	/**
	 * Indique l'unique instance de terrain.
	 * 
	 * @return Renvoie le terrain actuel.
	 */
	public static Terrain getInstance() throws NoTerrain {
		if (Terrain.uniqueInstance == null) throw new NoTerrain() ;
		return Terrain.uniqueInstance ;
	}

	/**
	 * Renvoie la case aux coordonnees indiquees. Une exception est envoyee si la case n'existe pas ( hors des limite du plateau )
	 * 
	 * @param x L'abscisse de la case ( origine en "haut a gauche" quand observation du plateau sur une fenetre de simulation )
	 * @param y L'ordonnee de la case ( origine en "haut a gauche" quand observation du plateau sur une fenetre de simulation )
	 * 
	 * @return Renvoie la case au coordonnees indiquees si la case existe.
	 * 
	 * @throws HorsLimite La case n'existe pas, est hors des limites du plateau.
	 */
	public Case getCase(int x, int y) throws HorsLimite {
		if ((xMax<=x) || (yMax<=y)) throw new HorsLimite(x, y, xMax, yMax) ;
		if ((x<0) || (y<0)) throw new HorsLimite(x, y, xMax, yMax) ;
		return this.tableau[y][x] ;
	}
	
	/**
	 * Initialisation de la simulation. Ajouter tous les acteurs mis sur le plateau de simulation.
	 */
	public void initialiser() {
		this.ajouterActeurs();
		this.supprimerActeurs();
	}
	
	/**
	 * Deroule un tour de la simulation :
	 * 		realiser les actions du "Feu" puis des "Pompiers"
	 * 		"actions" = Acteur.actionConcrete() puis Acteur.mouvementConcret()
	 * 		Puis ajout des acteurs crees pendant ce tour.
	 * 		Enfin suppression des acteurs mort pendant ce tour.
	 */
	public void evolution() {
		this.stat.incTour(); 				// Augmenter le nombre de tour.
		
		Iterator<Acteur> aIterator = this.flammes.iterator() ;
		Acteur f ;
		
		while (aIterator.hasNext()) {								// Pour chaque flamme enlever le focus.
			f = aIterator.next();
			
			f.setUnFocus(); 
		}
		
		aIterator = this.flammes.iterator() ;
		while (aIterator.hasNext()) {								// Chaque flamme joue son tour.
	
			f = aIterator.next() ;
			
			f.actionConcrete();
			f.mouvementConcrete();
		}
		
		this.ajouterActeurs();
		this.supprimerActeurs();
		
		aIterator = this.pompiers.iterator() ;
		Acteur p ;
		while (aIterator.hasNext()) {								// Chaque pompier joue son tour.
			p = aIterator.next();
			
			p.actionConcrete();
			p.mouvementConcrete();
		}
		
		this.ajouterActeurs();								// Ajouter les acteurs crees pendant ce tour.
		this.supprimerActeurs();							// Enlever les acteurs morts pendant ce tour.
	}
	
	/**
	 * Indique si la simulation est achevee.
	 * 
	 * @return Renvoie true si simulation achevee sinon false.
	 */
	public boolean estTermine() {
		if (this.flammes.isEmpty() || this.pompiers.isEmpty()) {				// Si plus de flammes ou plus de pompiers alors simulation terminee.
			this.stat.feuEteint = this.feuEteint() ;
			return true ;
		} else
			return false ;
	}
	
	/**
	 * Indique si le feu a ete eteint ou non.
	 * 
	 * @return Renvoie true si le feu a ete eteint sinon false.
	 */
	public boolean feuEteint() {
		return this.flammes.isEmpty() ;
	}
	
	public boolean rightInit() {
		boolean pompierPres = false, feuPres = false ;
		
		for (Acteur a : this.nouveauAct) {
			if (a.getType() == ActeurType.Feu) {
				feuPres = true ;
			} else pompierPres = true ;
		}
		
		return pompierPres && feuPres ;
	}
		
	/**
	 * Indique le feu le plus proche de la case en parametre.
	 * 
	 * @param x Abs. de la case source de la demande.
	 * @param y Ordonnee de la case source de la demande
	 * 
	 * @return Renvoie la case en Feu la plus proche.
	 */
	public int[] getClosestFire(int x, int y) {
		int difXTemp, difYTemp ;
		double d, dTemp ;
		int[] ans = new int[2] ;
		
		d = Double.MAX_VALUE ;
		
		for (Acteur a : this.flammes) {
			difXTemp = Math.abs(x - a.getPosX()) ;
			difYTemp = Math.abs(y - a.getPosY()) ;
			
			dTemp = difXTemp*difXTemp + difYTemp*difYTemp ;
			dTemp = Math.sqrt(dTemp) ;
			if (dTemp < d) {
				ans[0] = a.getPosX() ;
				ans[1] = a.getPosY() ;
				d = dTemp ;
			}
		}
		
		for (Acteur a : this.nouveauAct) {
			if (a.getType() == ActeurType.Feu) {
				difXTemp = Math.abs(x - a.getPosX()) ;
				difYTemp = Math.abs(y - a.getPosY()) ;
				
				dTemp = difXTemp*difXTemp + difYTemp*difYTemp ;
				dTemp = Math.sqrt(dTemp) ;
				if (dTemp < d) {
					ans[0] = a.getPosX() ;
					ans[1] = a.getPosY() ;
					d = dTemp ;
				}
			}
		}
		
		return ans ;
	}
	
	/**
	 * Indique le feu le plus proche et non focus de la case en parametre, si aucun resultat, renvoie le feu le plus proche.
	 * 
	 * @param x Abs. de la case source de la demande.
	 * @param y Ordonnee de la case source de la demande
	 * 
	 * @return Renvoie la case en Feu la plus proche et non focus, ou le feu le plus proche si aucun resultat.
	 */
	public int[] getClosestFireNotFocus(int x, int y) {
		int difXTemp, difYTemp ;
		double dBestAns, dTemp ;
		int[] bestAns = new int[2] ;				// Feu non focus le plus proche.
		
		dBestAns = Double.MAX_VALUE ;
		bestAns[0] = -1 ;
		
		for (Acteur a : this.flammes) {				// pour chaque flamme verifier son focus et sa distance au pompier.
			difXTemp = Math.abs(x - a.getPosX()) ;
			difYTemp = Math.abs(y - a.getPosY()) ;
			
			dTemp = difXTemp*difXTemp + difYTemp*difYTemp ;
			dTemp = Math.sqrt(dTemp) ;
			if (a.getIsFocus() == false) {
				if (dTemp < dBestAns) {
					bestAns[0] = a.getPosX() ;
					bestAns[1] = a.getPosY() ;
					dBestAns = dTemp ;
				}
			}
		}
		
		if (bestAns[0] != -1) {						// Si resultat alors le renvoyer
			return bestAns ;
		} else return this.getClosestFire(x, y) ;		// Sinon renvoyer le feu le plus proche du pompier.
	}
}
