package Fenetres;


import java.awt.BorderLayout;

import javax.swing.JFrame;

import Environnement.Terrain;
import Exceptions.FenetreErreurFatale;
import Exceptions.NoTerrain;

/**
 * Fenetre montrant le deroulement de la simulation.
 * 
 * @author ALLAM Jonathan - <thomasallam@outlook.fr> - 06/01/2018
 *
 * @version 0.0.1
 */
@SuppressWarnings("serial")
public class FenetreSimulation extends JFrame {
	public PanneauSimulation p ;
	private PanneauSimulationSouth pSouth ;
	private int mode ;
	
	/**
	 * Constructeur par defaut.
	 */
	public FenetreSimulation() {
		super() ;
		
		this.setTitle("Simulation");
		this.setLocation(100, 100);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.p = new PanneauSimulation() ;
		this.pSouth = new PanneauSimulationSouth(this) ;
		
		int l, h ;
		l = this.p.getPreferredSize().width < this.pSouth.getPreferredSize().width ?  this.pSouth.getPreferredSize().width : this.p.getPreferredSize().width ;
		h = this.p.getPreferredSize().height + this.pSouth.getPreferredSize().height + 50 ;
		this.setSize(l+50, h);
		
		this.add(p) ;
		this.add(pSouth, BorderLayout.SOUTH) ;
		
		this.validate();
		
		this.mode = 0 ;								// Par defaut : simulation en pause.
		
		this.setVisible(true);
	}
	
	/**
	 * Passage de la simulation en pause.
	 */
	public void toPause() {
		this.mode = 0 ;
	}
	
	/**
	 * Effectuer 1 tour de simulation puis mettre en pause.
	 */
	public void toSuivant() {
		this.mode = 2 ;
	}
	
	/**
	 * Continuer tant que non en pause ou mise en "suivant".
	 */
	public void toContinuer() {
		this.mode = 1 ;
	}
	
	/**
	 * Effectue l'action associe au mode de la simulation selectionne.
	 */
	public void simulation() {
		try {
			Terrain t = Terrain.getInstance() ;
		
			if (this.mode == 2) {
				t.evolution();
				
				repaint() ;
				
				this.mode = 0 ;
			} else {
				if (this.mode == 1) {
					t.evolution();
					
					repaint() ;
				}
			}
		} catch (NoTerrain e) {
			new FenetreErreurFatale(e.toString()) ;
		}
	}
}
