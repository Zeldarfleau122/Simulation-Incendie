package Exceptions;

/**
 * Exception directement affiche a l'ecran lors de sa reception.
 * 
 * @author ALLAM Jonathan - <thomasallam@outlook.fr> - 29/12/2017
 *
 */
@SuppressWarnings("serial")
public class WindowedException extends Exception {
	private String erreur ;
	/**
	 * Constructeur par defaut.
	 * 
	 * @param erreur Texte de l'erreur.
	 */
	public WindowedException(String erreur) {
		this.erreur = erreur ;
		
		new FenetreException(this.erreur) ;
	}
}
