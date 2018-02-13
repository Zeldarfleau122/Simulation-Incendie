package Comportemental;

import java.awt.Color;

import Environnement.Terrain;
import Exceptions.NoTerrain;

/**
 * Decrit un acteur, origine d'actions decrites par un comportement specifique a chaqu'un.
 * 
 * @author ALLAM Jonathan - <thomasallam@outlook.fr> - 29/12/2017
 * 
 * @version 0.0.1
 * 
 * @see ActeurType
 * @see Comportement
 */
public class Acteur {
	private ActeurType type ;				// Type d'acteur ( basiquement : feu ou pompier )
	private Comportement comp ;				// Comportement associé ( Design pattern : Strategy )
	private int posX, posY ;				// Position sur la plateau
	private int vie ;						// Nombre de point de vie.
	private boolean recente ;				// Indique si l'acteur est recent ou non. ( crée pendant ce tour ou non )
	private boolean isFocus ;				// Indique si l'acteur est actuellement la cible d'un autre acteur. ( Feu cible d'un pompier ? )
	
	/**
	 * Constructeur d'un acteur.
	 * 
	 * @param nom Nom de l'acteur definit dans l'enumerateur ActeurType
	 * @param nbrVie La duree de vie de l'acteur.
	 * @param x La position en abscisse de l'acteur sur le plateau.
	 * @param y La position en ordonnee de l'acteur sur le plateau.
	 * 
	 * @see ActeurType
	 */
	public Acteur(ActeurType nom, int x, int y) {
		this.type = nom ;
		this.comp = nom.getComportement() ;

		this.vie = nom.getVie() ;
			
		this.posX = x ;
		this.posY = y ;
		
		this.recente = false ;
		this.isFocus = false ;
		
		try {
			Terrain t = Terrain.getInstance();				// Incrementation nombre creation de ce type d'acteur.
			t.stat.ajouterCreation(this);
		} catch (NoTerrain e) {
			
		}
	}
	
	public Acteur(ActeurType nom, int x, int y, int nbrVie) {
		this(nom, x, y) ;
		
		this.vie = nbrVie ;
	}

	/**
	 * Accesseur au type de l'acteur.
	 * 
	 * @return Renvoie le type de l'acteur.
	 * 
	 * @see ActeurType
	 */
	public ActeurType getType() {
		return this.type ;
	}
	
	public int getVie() {
		return this.vie ;
	}
	
	/**
	 * Indique la couleur associee a cet acteur. ( selon son instanciation )
	 * 
	 * @return Renvoie la couleur de l'ActeurType associe.
	 */
	public Color getColor() {
		return this.getType().getColor() ;
	}
	
																				// METTRE (RECENT) DANS ACTEUR !!!! ????
	/**
	 * Indique si la case de l'acteur a ete recemment mise a jour.
	 * 
	 * @return Renvoie true si case recente et false sinon.
	 */
	public boolean getRecente() {
		return this.recente ;
	}
	
	/**
	 * Accesseur position de l'acteur.
	 * 
	 * @return Renvoie l'abscisse de l'acteur sur le plateau.
	 */
	public int getPosX() {
		return this.posX ;
	}
	/**
	 * Accesseur position de l'acteur.
	 * 
	 * @return Renvoie l'ordonnee de l'acteur sur le plateau.
	 */
	public int getPosY() {
		return this.posY ;
	}
	
	/**
	 * Mes a true l'attribue "recente" de la case. Indique que la case a ete modifiee pendant ce tour.
	 */
	public void setRecente() {
		this.recente = true ;
	}
	
	/**
	 * Focus setter.
	 */
	public void setFocus() {
		this.isFocus = true ;
	}
	
	/**
	 * Reinitialisation du focus.
	 */
	public void setUnFocus() {
		this.isFocus = false ;
	}
	
	/**
	 * Mes a false l'attribue "recente" de la case. Indique que la case n'a pas ete modifiee pendant ce tour.
	 */
	public void unsetRecente() {
		this.recente = false ;
	}
	
	/**
	 * Indique si l'acteur est mort ou non.
	 * 
	 * @return Renvoie true si l'acteur est mort, false sinon.
	 */
	public boolean estMort() {
		return this.vie == 0 ;
	}
	
	public boolean getIsFocus() {
		return this.isFocus ;
	}
	/**
	 * Reduit la vie de l'acteur d'un nombre entree en parametre.
	 * 
	 * @param nbr Le nombre de vie a retirer.
	 */
	public void decVie(int nbr) {
		this.vie = nbr < this.vie ? vie-nbr : 0 ;
		if (this.vie == 0) this.actionMortConcrete();
	}
	
	/**
	 * Action effectue par l'acteur selon le comportement definit a son initialisation.
	 * 
	 * @see Comportement
	 */
	public void actionConcrete() {
		this.comp.action(this.posX, this.posY);
	}
	
	/**
	 * Mouvement effectue par l'acteur selon le comportement definit a son initialisation.
	 */
	public void mouvementConcrete() {
		this.comp.mouvement(this.posX, this.posY);
	}
	
	/**
	 * Action effectue a la mort "naturelle" ( vie == 0 ) de l'acteur selon le comportement definit a son initialisation.
	 */
	public void actionMortConcrete() {
		this.comp.actionMort(posX, posY);
	}
	
	/**
	 * Action effectue a la mort prematuree de l'acteur ( vie != 0 ) selon le comportement definit a son initialisation.
	 */
	public void mortConcrete() {
		this.comp.mort(posX, posY) ;
	}
	
	/**
	 * Permettre l'affichage des informations fondamentales de l'acteur.
	 */
	public String toString() {
		return (this.type + " x : " + this.posX + " y : " + this.posY + " recente : " + this.getRecente()) ;
	}
	
	/**
	 * Deplace l'acteur a la case indiquée.
	 * 
	 * @param x Abscisse de la case cible.
	 * @param y Ordonnee de la case cible.
	 */
	public void deplacer(int x, int y) {
		try {
			Terrain t = Terrain.getInstance() ;
		
			
			t.getCase(this.posX, this.posY).deplacerActeur(x, y);
			
			this.posX = x ;
			this.posY = y ;
		} catch (Exception e) {
			
		}
	}
}
