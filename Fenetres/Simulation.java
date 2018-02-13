package Fenetres;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

import Database.DBConnection;
import Environnement.Terrain;
import Exceptions.FenetreErreurFatale;
import Exceptions.NoTerrain;

@SuppressWarnings("serial")
public class Simulation implements Runnable {
	private FenetreSimulation f ;
	public int mode ;
	
	/**
	 * constructeur
	 */
	public Simulation() {
		this.f = new FenetreSimulation() ;
		
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		this.mode = 0 ;
	}
	
	/**
	 * Commence la simulation par la creation d'une action tous les 0.25 secondes : effectuer un tour de jeu.
	 */
	public void run() {
		Timer t = new Timer() ;
		
		t.schedule(new SimulationTask() , 0, 250);
	}
	
	/**
	 * Tache de la simulation : effectuer tour, tant que simulation non terminee.
	 */
	private class SimulationTask extends TimerTask {
		public void run() {
			try {
				if (!Terrain.getInstance().estTermine()) {	// Si simulation non terminee alors ...
					f.simulation();								// Continuer ( depend du mode de fenetreSimulation )
				} else {									// Si simulation terminee alors ...
					f.dispose() ;								// Fermer fenetre simulation
					
					try {
						DBConnection.enregistrer(Terrain.getInstance().stat) ;				// Connection a la base de donnee pour enregistrer les resultats.
					} catch (Exception ee) {}
					
					new FenetreStat() ;							// Ouvrir la fenetre des resultats
					
					this.cancel() ;								// Arreter l'action periodique.
				}
			} catch (NoTerrain e) {								// Si pas de terrain ...
				new FenetreErreurFatale(e.toString()) ;					// ... Erreur fatale, impossible de lancer la simulation.
			}
		}
	}
}
