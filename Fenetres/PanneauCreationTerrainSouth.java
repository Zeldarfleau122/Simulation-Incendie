package Fenetres;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import Environnement.Terrain;
import Exceptions.FenetreException;

/**
 * Panneau du bas de la fenetre "creation terrain". Permet de lancer la simulation lorsque toutes les modifications / ajouts sont termines.
 * 
 * @author ALLAM Jonathan - <thomasallam@outlook.fr> - 06/01/2018
 *
 * @version 0.0.1
 */
@SuppressWarnings("serial")
public class PanneauCreationTerrainSouth extends JPanel {
	private JButton suivant ;								// Bouton de passage a l'etape suivante.
	private FenetreCreationTerrain fContainer ;				// Fenetre contenant se panneau.
	
	public PanneauCreationTerrainSouth(PanneauCreationTerrain p, FenetreCreationTerrain fContainer) {
		this.suivant = new JButton("Suivant") ;
		this.fContainer = fContainer ;
		
		this.add(this.suivant);
		
		this.suivant.addActionListener(new BoutonAction());
	}

	/*
	 * Action associee au bouton "suivant".
	 */
	private class BoutonAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				if (Terrain.getInstance().rightInit()) {
					
					fContainer.dispose() ;
				
					new FenetreInitMeteo() ;
				} else {
					new FenetreException("Ajouter au moins 1 Feu et 1 Pompier.") ;
				}
			} catch (Exception ee) {}
		}
	}
}
