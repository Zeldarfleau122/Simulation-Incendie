package Environnement;

import java.awt.Color;

/**
 * Classe decrivant les caractéristiques de chaque type de case. 
 * Initialiser dynamiquement avant le debut initialisation simulation via la BD. ( cf. classe Case et DBConnection )
 * 
 * @author ALLAM Jonathan - <thomasallam@outlook.fr> - 29/12/2017
 * 
 * @version 0.0.1
 * 
 * @see Case
 */
public class CaractCaseType {
	private String nom ;
	private boolean accessible ;				// Ce type de case peut contenir un acteur ou non.
	private boolean inflammable ;				// Ce type de case peut etre en feu ou non.
	private Color couleur ;						// Couleur de ce type de case.
	private boolean recordStat ;				// Indique si suivie statistique pour cette case ou non. ( affichage en fin de simulation des resultats sans sauvegarde dans la BD )
	private boolean analysable ;
	private double probaBruler ;
	
	/**
	 * Constructeur d'une CaractCaseType.
	 * 
	 * @param accessible Indique si ce type de case est accessible. ( peut contenir un acteur )
	 * @param inflammable Indique si ce type de  case peu etre en feu.
	 * @param couleur La couleur de ce type de case.
	 * @param recordStat Indique si la case doit etre suivie. ( Construction de statistiques )
	 */
	public CaractCaseType(String nom, boolean accessible, boolean inflammable, int couleur, boolean recordStat, boolean analysable, double probaBruler) {
		this.nom = nom ;
		this.accessible = accessible ;
		this.inflammable = inflammable ;
		this.couleur = new Color(couleur) ;
		this.recordStat = recordStat ;
		this.analysable = analysable ;
		this.probaBruler = probaBruler ;
	}
	
	/**
	 * Renvoie le nom du type de la case.
	 */
	public String getNom() {
		return this.nom ;
	}
	
	/**
	 * Renvoie l'accessibilite de ce type de case.
	 * 
	 * @return Renvoie true si ce type de case est accessible sinon false.
	 */
	public boolean getAccessible() {
		return this.accessible ;
	}
	
	/**
	 * Accesseur a l'attribut inflammable.
	 * 
	 * @return Renvoie true si ce type de case est inflammable sinon false.
	 */
	public boolean getInflammable() {
		return this.inflammable ;
	}
		
	/**
	 * Accesseur a la couleur de ce type de case.
	 * 
	 * @return Renvoie la couleur de ce type de case.
	 */
	public Color getColor() {
		return this.couleur ;
	}
	
	/**
	 * Indique si les donnees de la case doivent ou non etre observees.
	 * 
	 * @return Renvoie true si la case doit etre suivie, false sinon.
	 */
	public boolean getRecordStat() {
		return this.recordStat ;
	}
	
	/**
	 * Indique si la case peut etre reconnu sur une image ou non.
	 * 
	 * @return Renvoie true si la case peu etre analysee et false sinon.
	 */
	public boolean getAnalysable() {
		return this.analysable ;
	}
	
	/**
	 * Indique la probabilite d'etre brulee de la case.
	 * 
	 * @return Renvoie probaBruler de la case type.
	 */
	public double getProbaBruler() {
		return this.probaBruler ;
	}
	
	/**
	 * Renvoie le nom du type de la case.
	 */
	public String toString() {
		return this.nom ;
	}
}
