package Exceptions;

/**
 * Exception : aucun terrain initialise.
 * 
 * @author ALLAM Jonathan - <thomasallam@outlook.fr> - 29/12/2017
 *
 */
@SuppressWarnings("serial")
public class NoTerrain extends TerrainException {
	/**
	 * Constructeur par defaut.
	 */
	public NoTerrain() {
		super("Aucun terrain n'a ete initialise.") ;
	}
}
