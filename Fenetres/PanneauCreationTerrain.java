package Fenetres;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import Comportemental.ActeurType;
import Environnement.Case;
import Environnement.CaseType;
import Environnement.Terrain;
import Exceptions.FenetreErreurFatale;
import Exceptions.HorsLimite;
import Exceptions.NoTerrain;

/**
 * Panneau de la fenetre "CreatonTerrain". Affiche le terrain, un clic effectue une action en fonction des choix effectues dans les diverses listes deroulantes
 * du panneau PanneauCreationTerrainEast.
 * 
 * @author ALLAM Jonathan - <thomasallam@outlook.fr> - 06/01/2018
 *
 * @version 0.0.1
 */
@SuppressWarnings("serial")
public class PanneauCreationTerrain extends PanneauAffichageTerrain {
	private PanneauCreationTerrainEast p ;
	private boolean leftButtonDown ;
	
	public PanneauCreationTerrain(PanneauCreationTerrainEast p) {
		super() ;
		
		this.setSize(100, 100);
		this.setFocusable(true);
		this.addMouseListener(new MouseAction());
		this.addMouseMotionListener(new MouseMoveAction());
		
		this.p = p ;
	}
	
	private class MouseMoveAction extends MouseMotionAdapter {
		public boolean inPlateau(MouseEvent e) throws NoTerrain {
			Terrain t = Terrain.getInstance();
			
			if ((10 <= e.getX()) && (e.getX() <= 10+4*(t.getXMax()+1))) 
				if ((10 <= e.getY()) && (e.getY() <= 10+4*(t.getYMax()+1))) 
					return true ;
			return false ;
		}
		
		public Case getCase(MouseEvent e) throws NoTerrain {
			try {
				Terrain t = Terrain.getInstance() ;
				int x = e.getX();
				int y = e.getY();
				int i, j ;
			
				i = (x-10)/4 ;
				j = (y-10)/4 ;
			
				return t.getCase(i,j) ;
			} catch (HorsLimite h) {
				return null ;
			} 
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			if (leftButtonDown) {				// Si clic gauche ...
				try {
					if (this.inPlateau(e)) {
						Case c = this.getCase(e) ;
						if (c != null) {
							if (p.choixAction.getSelectedItem() == "Case") {
								String nomCase = (String) p.listeType.getSelectedItem() ;
								
								c.changeType(CaseType.getCaractCaseType(nomCase));
								repaint() ;
							} else {
								c.ajoutActeur((ActeurType) p.listeType.getSelectedItem());
								repaint() ;
							}
						}
					}
				} catch (NoTerrain exception) {
					new FenetreErreurFatale(exception.toString()) ;
				}
			}
		}
	}
	
	private class MouseAction extends MouseAdapter {
		public boolean inPlateau(MouseEvent e) throws NoTerrain {
			Terrain t = Terrain.getInstance();
			
			if ((10 <= e.getX()) && (e.getX() <= 10+4*(t.getXMax()+1))) 
				if ((10 <= e.getY()) && (e.getY() <= 10+4*(t.getYMax()+1))) 
					return true ;
			return false ;
		}
		
		public Case getCase(MouseEvent e) throws NoTerrain {
			try {
				Terrain t = Terrain.getInstance() ;
				int x = e.getX();
				int y = e.getY();
				int i, j ;
			
				i = (x-10)/4 ;
				j = (y-10)/4 ;
			
				return t.getCase(i,j) ;
			} catch (HorsLimite h) {
				return null ;
			} 
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			int bouton = e.getButton() ;
			
			if (bouton == MouseEvent.BUTTON1) {				// Si clic gauche ...
				leftButtonDown = true ;
				
				try {
					if (this.inPlateau(e)) {
						Case c = this.getCase(e) ;
						if (c != null) {
							if (p.choixAction.getSelectedItem() == "Case") {
								String nomCase = (String) p.listeType.getSelectedItem() ;
								
								c.changeType(CaseType.getCaractCaseType(nomCase));
								
								repaint() ;
							} else {
								c.ajoutActeur((ActeurType) p.listeType.getSelectedItem());
								repaint() ;
							}
						}
					}
				} catch (NoTerrain exception) {
					new FenetreErreurFatale(exception.toString()) ;
				}
			}
		}
		
		public void mouseReleased(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) 
				leftButtonDown = false ;
		}
	}
}
