package Comportemental;

import Environnement.Terrain;
import Exceptions.*;
import Environnement.Case;
import Environnement.CaseType;

import java.util.Random;

/**
 * Comportement de l'ActeurType "Feu", contient les actions concretes associees.
 * 
 * @author ALLAM Jonathan - <thomasallam@outlook.fr> - 29/12/2017
 *
 * @version 0.0.1
 */
public class CompFeu extends BaseComportement implements Comportement {
	/**
	 * Enflamme une case a une position donee.
	 * 
	 * @param x Abscisse de la case a enflammer.
	 * @param y Ordonnee de la case a enflammer.
	 */
	public void toFire(int x, int y) {
		try {
			Terrain plateau = this.getTerrain() ;
			Case caseAdj = plateau.getCase(x, y) ;
			try {
				if (caseAdj.getActeurType() != ActeurType.Feu) {				// Ne peut pas enflammer un feu ...
					Acteur flamme = new Acteur(ActeurType.Feu, x, y) ;

					caseAdj.setActeur(flamme); ;
				}
			} catch (Exception e) {
				Acteur flamme = new Acteur(ActeurType.Feu, x, y) ;

				caseAdj.setActeur(flamme); ;
			}
		} catch (HorsLimite | NoTerrain e) {}							// Aucune raison d'erreur.
	}
	
	/**
	 * Action associee au feu : se consumme.
	 * 
	 * @param x Abscisse du feu.
	 * @param y Ordonnee du feu.
	 */
	@Override
	public void action(int x, int y) {
		try {
			Acteur act = this.getActeur(x, y) ;				// Recuperation du feu correspondant.
		
			act.decVie(1) ;				// Reduire sa duree de vie
		} catch (HorsLimite | NoActor | NoTerrain e) {}		// Aucune raison d'erreur.
	}
	
	/**
	 * Mouvement du feu : contamine les cases adjacentes avec une probabilite de 1/2. 
	 * 
	 * @param x Abscisse du feu.
	 * @param y Ordonnee du feu.
	 */
	@Override
	public void mouvement(int x, int y) {				// Prendre en compte la meteo ( changer la description ! )
		Random r = new Random() ;
		
		try {														// Pour chaque case verifier si inflammable et nombre r < proba bruler ( influencee par meteo et type case )
			if ((this.getCase(x+1,y).getInflammable()) && (r.nextFloat()<Terrain.m.facteurEst()*this.getCase(x+1, y).getCaseProbaBruler()))					
				this.toFire(x+1, y);
			if (((this.getCase(x,y+1).getInflammable())) && (r.nextFloat()<Terrain.m.facteurNord()*this.getCase(x, y+1).getCaseProbaBruler()))
				this.toFire(x, y+1);
			if (((this.getCase(x-1,y).getInflammable())) && (r.nextFloat()<Terrain.m.facteurOuest()*this.getCase(x-1, y).getCaseProbaBruler()))
				this.toFire(x-1, y);
			if (((this.getCase(x,y-1).getInflammable())) && (r.nextFloat()<Terrain.m.facteurSud()*this.getCase(x, y-1).getCaseProbaBruler()))
				this.toFire(x, y-1);
		} catch (Exception e) {}							// Aucune raison d'erreur.
	}
	
	/**
	 * Action a la mort naturelle du feu : convertie la case en cendre.
	 * 
	 * @param x Abscisse du feu.
	 * @param y Ordonnee du feu.
	 */
	@Override
	public void actionMortSpecifique(int x, int y) {
		try {
			this.getCase(x, y).changeType(CaseType.getCaractCaseType("Cendre")) ;				// La case devient de la cendre.
			this.getCase(x, y).setActeur(null);						// Elle ne contient plus d'acteur.
		} catch (HorsLimite | NoTerrain e) {}				// Aucune raison d'erreur.
	}
	

	/**
	 * Action a la mort non naturelle du feu : le feu disparait sans modifier la case.
	 * 
	 * @param x Abscisse du feu.
	 * @param y Ordonnee du feu.
	 */
	@Override
	public void mort(int x, int y) {
		try {
			this.getCase(x, y).setActeur(null) ;
		} catch (HorsLimite | NoTerrain e) {}				// Aucune raison d'erreur.
	}
}
