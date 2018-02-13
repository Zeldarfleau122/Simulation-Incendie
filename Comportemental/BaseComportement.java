package Comportemental;

import Environnement.Case;
import Environnement.Terrain;
import Exceptions.HorsLimite;
import Exceptions.NoActor;
import Exceptions.NoTerrain;

/**
 * Comportement de base contenant les methodes utilisables a la construction d'un comportement specifique pour un acteur.
 * 
 * @author ALLAM Jonathan - <thomasallam@outlook.fr> - 29/12/2017
 *
 * @version 0.0.1
 */
public abstract class BaseComportement {
	/**
	 * Obtention du terrain de la simulation. ( terrain unique )
	 * 
	 * @return Renvoie le terrain de la simulation.
	 */
	public Terrain getTerrain() throws NoTerrain {
		return Terrain.getInstance() ;
	}
	
	/**
	 * Obtention d'une case du terrain via ses coordonnees.
	 * 
	 * @param x Abscisse de la case.
	 * @param y Ordonnee de la case.
	 * 
	 * @return Renvoie la case correspondante.
	 * 
	 * @throws HorsLimite Interruption issue du Terrain ( getCase ) prise en compte par chaque comportement fils !
	 * 
	 * @see Terrain
	 */
	public Case getCase(int x, int y) throws HorsLimite, NoTerrain {
		Terrain plateau = this.getTerrain() ;
		
		return plateau.getCase(x, y) ;
	}
	
	/**
	 * Obtention d'un acteur du terrain via ses coordonnees, utilise getCase de cette meme classe.
	 * 
	 * @param x Abscisse de l'acteur.
	 * @param y Ordonnee de l'acteur.
	 * 
	 * @return Renvoie l'acteur de la case correspondante.
	 * 
	 * @throws HorsLimite Interruption issue du Terrain ( getCase ) prise en compte par chaque comportement fils !
	 * 
	 * @see Terrain
	 */	
	public Acteur getActeur(int x, int y) throws HorsLimite, NoActor, NoTerrain {
		Case caseActeur = this.getCase(x, y) ;
		
		return caseActeur.getActeur() ;
	}
	
	/**
	 * Action a la mort de l'acteur.
	 * 
	 * @param x position en abscisse de l'acteur.
	 * @param y position en ordonnee de l'acteur.
	 */
	public void actionMort(int x, int y) {
		try {
			Terrain t = Terrain.getInstance() ;
			
			Acteur a = this.getActeur(x, y) ;
			
			t.stat.ajouterMort(a);
			
			this.actionMortSpecifique(x, y);
		} catch (Exception e) {}									// Aucune raison d'erreur.
	}
	
	/**
	 * Action specifique de chaque acteur a sa mort.
	 * 
	 * @param x position en abscisse de l'acteur.
	 * @param y position en ordonnee de l'acteur.
	 */
	public abstract void actionMortSpecifique(int x, int y) ;
}
