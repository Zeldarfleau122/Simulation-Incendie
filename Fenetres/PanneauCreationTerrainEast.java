package Fenetres;

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Comportemental.ActeurType;
import Environnement.CaseType;

/**
 * Panneau de droite de la fenetre "creation terrain". Comporte des listes deroulantes permettant a l'utilisateur de rajouter des acteur / changer des types
 * de case sur le plateau actuel.
 * 
 * @author ALLAM Jonathan - <thomasallam@outlook.fr> - 06/01/2018
 *
 * @version 0.0.1
 */
@SuppressWarnings("serial")
public class PanneauCreationTerrainEast extends JPanel {
	public JComboBox<String> choixAction ;
	public JComboBox<Object> listeType ;
	
	public PanneauCreationTerrainEast() {
		JPanel p1, p2, p3 ;
		JLabel l1, l2 ;
		
		p1 = new JPanel() ;
		p2 = new JPanel() ;
		p3 = new JPanel() ;
		l1 = new JLabel() ;
		l2 = new JLabel() ;
		
		l1.setText("Choix de l'element a rajouter :");
		l2.setText("Choix du type de l'element :");
		
		String[] s = {"Case", "Acteur"} ;
		this.choixAction = new JComboBox<String>(s) ;
		this.listeType = new JComboBox<Object>() ;
		
		for (String nomCaseType : CaseType.ensCase.keySet()) {
			this.listeType.addItem(nomCaseType);
		}
		
		p1.add(l1);
		p1.add(choixAction) ;
		p1.setBackground(Color.GRAY);
		
		p2.add(l2) ;
		p2.add(listeType);
		p2.setBackground(Color.GRAY);
		
		//p3.add(new JButton("Effacer case")) ;
		//p3.add(new JButton("Effacer Acteur")) ;
		
		this.choixAction.addItemListener(new ListeChangeAction());
		
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.add(p1) ;
		this.add(Box.createVerticalStrut(25));
		this.add(p2);
		this.add(Box.createVerticalStrut(25)) ;
		this.add(p3) ;
	}
	
	/**
	 * Action lors de la modification de la liste deroulante du haut.
	 */
	private class ListeChangeAction implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			if (e.getItem() != null) {							// Si une selection ...
				if (e.getItem() == "Case") {						// ... Si "case" Alors ...
					listeType.removeAllItems();								// Supprimer tous les elements de la liste deroulante du bas
					for (String nomCaseType : CaseType.ensCase.keySet()) 	// Rajouter tous les noms des types de cases actuel.
						listeType.addItem(nomCaseType) ;
				} else {											// ... Sinon "Acteur" Alors ...
					listeType.removeAllItems();								// Supprimer tous les elemtsnde  la liste deroulante du bas
					for (ActeurType t : ActeurType.values()) 				// Rajouter tous les noms des acteurs. ( de base Feu et Pompier )
						listeType.addItem(t);
				}
			}
		}
	}
}
