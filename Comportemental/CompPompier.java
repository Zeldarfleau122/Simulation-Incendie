package Comportemental;

import java.util.Random;

import Environnement.Case;
import Environnement.Terrain;
import Exceptions.*;

/**
 * Comportement de l'ActeurType "Pompier", contient les actions concretes associees.
 * 
 * @author ALLAM Jonathan - <thomasallam@outlook.fr> - 29/12/2017
 *
 * @version 0.0.1
 */
public class CompPompier extends BaseComportement implements Comportement {
	int[] fireFocus ;
	
	/**
	 * Recupere la cible du feu, le plus proche et non focus en premier, si aucun alors le plus proche.
	 * 
	 * @param x Abscisse de l'acteur.
	 * @param y Ordonnee de l'acteur.
	 */
	public void getFocus(int x, int y) {
		try {
			this.fireFocus = Terrain.getInstance().getClosestFireNotFocus(x, y) ;				// Recuperer la cible ( cf. description )
		} catch (Exception e) {}				// Aucune raison d'erreur.
		
		this.setFocus(this.fireFocus);			// Indiquer au feu autour de la cible qu'ils sont focus par un pompier.
	}
	/**
	 * Verifie si une case est en flamme dans un rayon de 3 cases. ( cad : contient un acteur "Feu" ) 
	 * 
	 * @param x l'abscisse du pompier.
	 * @param y Coordonnee du pompier.
	 * 
	 * @return Renvoie la position d'un feu si present dans un rayon de 3 case sinon (-1,-1).
	 */
	public int[] fireInRange(int x, int y) {
		int i, j ;				
		Case caseTemp ;
		boolean answer = false ;				// Indique si un feu est trouve ou non.
		int[] firePos = new int[2] ;			// Position du feu trouve ( -1, -1 si rien. )
		
		firePos[0] = -1 ;
		firePos[1] = -1 ;
		for (i = x-3; (i<=x+3) && !answer; i++) {
			for (j = y-3; (j<=y+3) && !answer; j++) {				// Pour toute case a 3 de distance et tant que pas de feu trouver ...
				try {
					caseTemp = this.getCase(i, j) ;
					
					if (caseTemp.getActeurType() == ActeurType.Feu) {			// Verifier si feu present.
						answer = true ;				// Feu trouve
						firePos[0] = i ;			// Enregistrement de la position du feu.
						firePos[1] = j ;
					}
				} catch (HorsLimite | NoActeurType | NoTerrain e) {
														// Aucune raison d'erreur.
				}
			}
		}
		
		return firePos ;				// Renvoyer les positions du feu (-1,-1) si rien.
	}
	
	public void setFocus(int[] firePos) {
		int xFire = firePos[0] ;
		int yFire = firePos[1] ;
		int i ;
		int j ;
		for (i=xFire-5; i<=xFire+5; i++) {
			for (j=yFire-5; j<=yFire+5; j++) {
				try {
					if (this.getActeur(i, j).getType() == ActeurType.Feu) {
						this.getActeur(i, j).setFocus();
					}
				} catch (NoActor e) {
														// Aucune raison d'erreur.
				} catch (NoTerrain | HorsLimite ee) {
														// Aucune raison d'erreur.
				}
			}
		}
	}
	
	public void eteindre(int[] firePos) {
		Random r = new Random() ;
		
		try {
			if (r.nextDouble() <= 0.5) {
				Case caseFeu = this.getCase(firePos[0], firePos[1]) ;
				
				caseFeu.setActeur(null);				// Extinction du feu sur la case.
			}
		} catch (Exception e) {}						// Aucune raison d'erreur.
	}
	
	/**
	 * Action associe au Pompier : En fonction de la distance au feu le plus proche :
	 * 			Si trop proche ( < 2 cases ) => Partir dans la direction opposee.
	 * 			Si a distance moyenne ( <= 3 Cases ) => Essayer d'eteindre le feu.
	 * 			Si a distance elevee ( > 3 Cases ) => Si possible ( en fonction de la vie ) se diviser.
	 * 
	 * @param x Abscisse du pompier.
	 * @param y Coordonnee du pompier.
	 */
	@Override
	public void action(int x, int y) {
		this.getFocus(x, y) ;
		
		int[] posFire ;
		
		try {
			posFire = Terrain.getInstance().getClosestFire(x, y) ;
			
											/* Etude des actions en fonction du feu le plus proche, qui menace le pompier */
			Double d = Math.sqrt(Math.abs(posFire[0]-x)*Math.abs(posFire[0]-x) + Math.abs(posFire[1]-y)*Math.abs(posFire[1]-y)) ;
			if (d < 2.0) {												// Si un feu est trop proche : tenter une retraite.
				try {
					int depX, depY ;
					
					depX = (posFire[0] < x) ? 2 : -2 ;								// Determiner le deplacement selon x et y ( aller a l'oppose du feu )
					depY = (posFire[1] < y) ? 2 : -2 ;
					
					if (this.getActeur(x+depX, y+depY) == null)						// Si aucun acteur a la case voulue pour le deplacement alors se deplacer
						this.getActeur(x, y).deplacer(x+depX, y+depY);
					else															// Sinon retraite impossible, le pompier tente d'eteindre le feu.
						this.eteindre(posFire) ;
				} catch (Exception e) {}								// Aucune raison d'erreur.
			} else {								
																/* Etude des actions en fonction du feu cible par le pompier */
								
				posFire = this.fireFocus ;
				d = Math.sqrt(Math.abs(posFire[0]-x)*Math.abs(posFire[0]-x) + Math.abs(posFire[1]-y)*Math.abs(posFire[1]-y)) ;
				
				if (d<=3.0) {								// Si feu a portee de tir alors tenter de l'eteindre.
					if (posFire[0] != -1) {
						Random r = new Random() ;
						
						if (r.nextFloat() <= 1) {
							try {
								Case caseFeu = this.getCase(posFire[0], posFire[1]) ;
							
								caseFeu.setActeur(null);				// Extinction du feu sur la case.
							} catch (HorsLimite | NoTerrain e) {
																		// Enregistrer dans un fichier log les erreurs.
							}
						}
					}
																/* Etude des actions en fonction du feu le plus proche au pompier */
				} else {
					posFire = Terrain.getInstance().getClosestFire(x, y) ;
					
					d = Math.sqrt(Math.abs(posFire[0]-x)*Math.abs(posFire[0]-x) + Math.abs(posFire[1]-y)*Math.abs(posFire[1]-y)) ;
					
					if (d<=3.0) {
						if (posFire[0] != -1) {
							Random r = new Random() ;
							
							if (r.nextFloat() <= 1) {
								try {
									Case caseFeu = this.getCase(posFire[0], posFire[1]) ;
								
									caseFeu.setActeur(null);				// Extinction du feu sur la case.
								} catch (HorsLimite | NoTerrain e) {
																			// Enregistrer dans un fichier log les erreurs.
								}
							}
						}
					} else {
														/* Sinon : si pompier possède une vie > 1 alors le diviser en plusieurs pompiers */
														
						int vie = this.getActeur(x, y).getVie() ;
						
						if (1 < vie) {
							if (vie % 2 == 0) {
								int depX, depY ;
								
								depX = (posFire[0] < x) ? 2 : 2 ;
								depY = (posFire[1] < y) ? -2 : 2 ;
								
								this.getActeur(x, y).decVie(vie/2);
								this.getCase(x+depX, y+depY).ajoutActeur(ActeurType.Pompier);
								this.getActeur(x+depX, y+depY).decVie(ActeurType.Pompier.getVie()-(vie/2));
							} else {
								int depX, depY ;
								
								depX = (posFire[0] < x) ? 2 : 2 ;
								depY = (posFire[1] < y) ? -2 : 2 ;
								
								this.getActeur(x, y).decVie(vie-1);
								
								this.getCase(x+depX, y+depY).ajoutActeur(ActeurType.Pompier);
								this.getActeur(x+depX, y+depY).decVie(ActeurType.Pompier.getVie()-(vie-1)/2);
								
								depX = (posFire[0] < x) ? -2 : 2 ;
								depY = (posFire[1] < y) ? 2 : 2 ;
								
								this.getCase(x+depX, y+depY).ajoutActeur(ActeurType.Pompier);
								this.getActeur(x+depX, y+depY).decVie(ActeurType.Pompier.getVie()-(vie-1)/2);
							}
						}
					}
				}
			}
		} catch (Exception e) {}
	}
	
	/**
	 * Deplace l'acteur de (depX,depY) depuis (x,y)
	 * 
	 * @param x Abscisse de l'acteur.
	 * @param y Ordonnee de l'acteur.
	 * @param depX Deplacement en x.
	 * @param depY Deplacement en y.
	 * 
	 * @return Indique si le deplacement a pu etre fait ou non.
	 */
	public boolean deplacement(int x, int y, int depX, int depY) {
		try {
			if (this.getCase(x+depX, y+depY).getActeurPresent() == false) {
				this.getActeur(x, y).deplacer(x+depX, y+depY);
				
				return true ;
			} else return false ;
		} catch (Exception e) {
			return false ;
		}
	}
	/**
	 * Mouvement du Pompier : Si pas de feu a portee alors se deplace sur toute les cases adjacentes.
	 * 
	 * @param x Abscisse du pompier.
	 * @param y Coordonnee du pompier.
	 */
	@Override
	public void mouvement(int x, int y) {
		int[] firePos ;
		
		firePos = this.fireInRange(x, y) ;
		try {
			if (firePos[0] == -1) {				// Sil il n'y a pas de feu a porter alors avancer.
				firePos = this.fireFocus ;
				
				int depX, depY ;
				
				depX = (firePos[0] < x) ? -2 : 2 ;
				depY = (firePos[1] < y) ? -2 : 2 ;
				
				if (!this.deplacement(x, y, depX, depY))
					this.deplacement(x, y, --depX, --depY);;
			}
		} catch (Exception e) {}
	}
	
	/**
	 * Action a la mort naturelle du Pompier : disparait de la case.
	 * 
	 * @param x Abscisse du pompier.
	 * @param y Coordonnee du pompier.
	 */
	@Override
	public void actionMortSpecifique(int x, int y) {
		try {
			this.getCase(x, y).setActeur(null);
		} catch (HorsLimite | NoTerrain e) {}
	}
	
	/**
	 * Action a la mort non naturelle du Pompier :  disparait de la case.
	 * 
	 * @param x Abscisse du pompier.
	 * @param y Coordonnee du pompier.
	 */
	@Override
	public void mort(int x, int y) {
		try {
			this.getCase(x, y).setActeur(null);
		} catch (HorsLimite | NoTerrain e) {}
	}
}
