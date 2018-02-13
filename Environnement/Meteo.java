package Environnement;

/**
 * Classe comportant les caracteristiques meteo d'une simulation. Ainsi que les fonctions permettant d'acceder a leur influence sur les probabilites
 * de propagation du feu.
 * 
 * @author ALLAM Jonathan - <thomasallam@outlook.fr> - 29/12/2017
 * 
 * @version 0.0.1
 *
 * @see CompFeu
 */
public class Meteo {
	private int temp ;				// Temperature en degre celsius
	private int humidite ;			// humidite en mm sur 24h
	private int vitVent ;			// vitesse du vent en km/h
	private int dirVentVert ;		// Direction verticale du vent
	private int dirVentHor ;		// Direction horizontale du vent.
	
	/**
	 * Constructeur initialisation de l'ensemble des parametres.
	 * 
	 * @param temp Temperature en degre celsius
	 * @param humidite humidite en mm sur 24h
	 * @param vitVent vitesse du vent en km/h
	 * @param dirVentVert Direction verticale du vent
	 * @param dirVentHor Direction horizontale du vent.
	 */
	public Meteo(int temp, int humidite, int vitVent, int dirVentVert, int dirVentHor) {
		this.temp = temp ;
		this.humidite = humidite ;
		this.vitVent = vitVent ;
		this.dirVentVert = dirVentVert ;
		this.dirVentHor = dirVentHor ;
	}
	
	/**
	 * Calcul probabilite propagation du feu vers le nord. ( haut du plateau )
	 * 
	 * @return Renvoie la propabilite de propagation d'un feu vers le nord. ( haut du plateau )
	 */
	public double facteurNord() {
		return 1.0 + this.dirVentVert*(this.vitVent/1000.0) - (this.humidite/3000.0) + (this.temp*this.temp)/(10000) ;
	}
	
	/**
	 * Calcul probabilite propagation du feu vers l'ouest. ( gauche du plateau )
	 * 
	 * @return Renvoie la propabilite de propagation d'un feu vers le ouest. ( gauche du plateau )
	 */
	public double facteurOuest() {
		return 1.0 + this.dirVentHor*(this.vitVent/1000.0) - (this.humidite/3000.0) + (this.temp*this.temp)/(10000) ;
	}
	
	/**
	 * Calcul probabilite propagation du feu vers le sud. ( bas du plateau )
	 * 
	 * @return Renvoie la propabilite de propagation d'un feu vers le sud. ( bas du plateau )
	 */
	public double facteurSud() {
		return 1.0 + this.dirVentVert*(this.vitVent/1000.0) - (this.humidite/3000.0) + (this.temp*this.temp)/(10000) ;
	}
	
	/**
	 * Calcul probabilite propagation du feu vers le est. ( droite du plateau )
	 * 
	 * @return Renvoie la propabilite de propagation d'un feu vers le est. ( droite du plateau )
	 */
	public double facteurEst() {
		return 1.0 + this.dirVentHor*(this.vitVent/1000.0) - (this.humidite/3000.0) + (this.temp*this.temp)/(10000) ;
	}
}
