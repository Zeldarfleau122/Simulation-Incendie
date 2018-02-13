package Environnement;

import java.awt.Color;

import Comportemental.Acteur;
import Comportemental.ActeurType;
import Exceptions.NoActeurType;
import Exceptions.NoActor;
import Exceptions.NoTerrain;

/**
 * Element constitutif du plateau de jeu : une case n'est pas animee mais ses caracteristiques influent sur les actions des divers acteurs.
 *  
 * @author ALLAM Jonathan - <thomasallam@outlook.fr> - 29/12/2017
 *
 * @version 0.0.1
 */
public class Case {
	private int posX, posY ;				// Position de la case
	private CaractCaseType type ;					// Type de la case
	private boolean actPresent ;			// Indique presence d'un acteur sur la case.
	private Acteur act ;					// Acteur present sur la case.
	

	/**
	 * Constructeur d'une case : initialise sa position ainsi que le type de la case. ( qui influe sur ses caracteristiques. )
	 * 
	 * @param x Abscisse de la case
	 * @param y Ordonnee de la case
	 * @param type Type de case a creer
	 * 
	 * @see CaseType
	 */
	public Case(int x, int y, String nomType) throws NoTerrain {
		this.type = CaseType.getCaractCaseType(nomType) ;
		
		this.actPresent = false ;		// Pas d'acteur present.
		this.act = null ;
		
		this.posX = x ;				// Position en absisse
		this.posY = y ;				// Position en ordonnee
		
		try {
			Terrain.getInstance().stat.ajouterCreation(this);
		} catch (Exception e) {
		}
	}
	
	/**
	 * Constructeur d'une case : initialise sa position ainsi que le type de la case et le type d'acteur present dessus.
	 * 		( le type de la case influe sur les caracteristiques de celle-ci )
	 * 
	 * @param x Abscisse de la case
	 * @param y Ordonnee de la case
	 * @param type Type de case a creer
	 * @param nomActeur Type d'acteur present sur la case
	 */
	public Case(int x, int y, String nomType, ActeurType nomActeur) throws NoTerrain {
		this(x,y,nomType) ;				// Appel au constructeur de base. 
		
		this.act = new Acteur(nomActeur, x, y) ;
		this.actPresent = true ;
		Terrain.getInstance().ajouter(this.act);
	}
	
	/**
	 * Accesseur au type de l'acteur.
	 * 
	 * @return Renvoie le type de l'acteur.
	 */
	public ActeurType getActeurType() throws NoActeurType {												// Throw exception si pas d'acteur !!!
		try {
			return this.getActeur().getType() ;
		} catch(NoActor e) {
			throw new NoActeurType() ;
		}
	}
	
	/**
	 * Accesseur a l'acteur present sur la case.
	 * 
	 * @return Renvoie l'acteur present sur la case.
	 * 
	 * @throws NoActor si la case ne contient pas d'acteur envoie une exception.
	 */
	public Acteur getActeur() throws NoActor {														// Throw exception si pas d'acteur !!!
		if (this.act == null) throw new NoActor() ;
		return this.act ;
	}
	
	/**
	 * Indique si un acteur est present sur la case.
	 * 
	 * @return Renvoie true si acteur present sinon false.
	 */
	public boolean getActeurPresent() {
		return this.actPresent ;
	}
	
	/**
	 * Indique la couleur de la case.
	 * Depend du type de l'acteur ou du type de la case, selon si un acteur est present ou non.
	 * 
	 * @return Renvoie la couleur associee a la case.
	 */
	public Color getColor() {
		if (this.actPresent == true)
			return this.act.getColor() ;
		else
			return this.type.getColor() ;
	}
	
	/**
	 * Indique si la case est accessible ou non. Soit, si un acteur peut l'occuper ou non.
	 * 
	 * @return Renvoie true si la case est accessible sinon false.
	 */
	public boolean getAccessible() {
		return this.type.getAccessible() ;
	}
	
	/**
	 * Indique si la case est inflammable ou non. Soit, si elle peut bruler ou non.
	 * 
	 * @return Renvoie true si la case est inflammable sinon false.
	 */
	public boolean getInflammable() {
		return this.type.getInflammable() ;
	}
	
	/**
	 * Accesseur au type de la case.
	 * 
	 * @return Renvoie le type de la case.
	 */
	public CaractCaseType getCaseType() {
		return this.type ;
	}
	
	/**
	 * Renvoie la probabilite d'etre bruler de la case.
	 * 
	 * @return Renvoie probaBruler de la case.
	 */
	public double getCaseProbaBruler() {
		return this.type.getProbaBruler() ;
	}
	
	/**
	 * Ajouter un acteur sur une case a partir de son type.
	 * 
	 * @param nom Type de l'acteur a ajouter sur la case.
	 */
	public void ajoutActeur(ActeurType nom) throws NoTerrain {
		this.setActeur(new Acteur(nom, posX, posY)) ;
	}
	
	/**
	 * Suppression d'un acteur de la case, et de la simulation.
	 * 		Ne "tue" pas l'acteur donc aucune influence sur les statistiques.
	 */
	public void supprimerActeur() {
		try {
			if (this.getActeurPresent())								// Si acteur present, le supprimer des acteurs a faire jouer.
				Terrain.getInstance().supprimer(this.getActeur());				// Suppression de l'acteur dans la simulation.
		} catch (NoActor | NoTerrain e) {}
		
		this.actPresent = false ;				// Suppression de l'acteur de la case.
		this.act = null ;
	}
	
	/**
	 * Place un acteur, cree ulterieurement, sur la case.
	 * 
	 * @param newAct Acteur a placer sur la case.
	 */
	public void setActeur(Acteur newAct) throws NoTerrain {
		if (newAct != null) {
			if (this.actPresent)
				this.supprimerActeur() ;
			this.actPresent = true ;
			this.act = newAct ;
			Terrain.getInstance().ajouter(this.act) ;
		} else {
			this.supprimerActeur() ;
		}
	}
	
	/**
	 * Deplacement de l'acteur de cette case vers une case cible (x, y).
	 * 			L'acteur de cette case est "transfere" et n'influe donc pas le nombre de creation ou de mort d'un acteur.
	 * 
	 * @param x Abs. de la case cible.
	 * @param y Ordonnee de la case cible.
	 */
	public void deplacerActeur(int x, int y) {
		try {
			Terrain t = Terrain.getInstance();
			Case c = t.getCase(x, y) ;
			
			c.act = this.act ;
			c.actPresent = true ;
			
			this.act = null ;
			this.actPresent = false ;
		} catch (Exception e) {
		}
	}
	
	/**
	 * Change le type de la case, modifie les caracteristiques de celle-ci.
	 * 
	 * @param t Nouveau type de la case.
	 */
	public void changeType(CaractCaseType t) {
		try {
			Terrain.getInstance().stat.ajouterMort(this);
			this.type = t ;
			Terrain.getInstance().stat.ajouterCreation(this);
		} catch (Exception e) {}
	}
}
