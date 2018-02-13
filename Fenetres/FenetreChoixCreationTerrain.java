package Fenetres;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Environnement.Terrain;
import Exceptions.FenetreException;

/**
 * Fenetre d'entree des caracteristiques du Terrain a creer. ( longueur et hauteur ) Initialise par l'utilisateur.
 * 
 * @author ALLAM Jonathan - <thomasallam@outlook.fr> - 08/01/2018
 *
 * @version 0.0.1
 */
@SuppressWarnings("serial")
public class FenetreChoixCreationTerrain extends JFrame {
	private JLabel labelLongueur ;				// Label champ texte longueur
	private JTextField longueur ;				// champ texte longueur. ( Entree utilisateur )
	private JLabel labelLargeur ;				// Label champ texte largeur
	private JTextField largeur ;				// champ texte largeur. ( Entree utilisateur )
	
	private JButton retour ;					// Bouton de retour a la fenetre precedente
	private JButton suivant ;					// Bouton suivant : aller a la fenetre suivante.
	
	private String nomSimulation, path ;
	
	public FenetreChoixCreationTerrain(String nomSimulation, String path) {
		this.nomSimulation = nomSimulation ;
		this.path = path ;
		
		this.setTitle("Initialisation caracteristiques de la carte");
		this.setBounds(100, 100, 400, 300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.labelLongueur = new JLabel("Longueur : ") ;
		this.longueur = new JTextField() ;
		this.longueur.setColumns(4);
		
		this.labelLargeur = new JLabel("Largeur : ") ;
		this.largeur = new JTextField() ;
		this.largeur.setColumns(4);
		
		JPanel p = new JPanel() ;
		p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
		
		JPanel pTemp = new JPanel() ;
		pTemp.add(this.labelLongueur) ;
		pTemp.add(this.longueur);
		p.add(pTemp) ;
		
		pTemp = new JPanel() ;
		pTemp.add(this.labelLargeur);
		pTemp.add(this.largeur);
		p.add(pTemp);
		
		this.add(p, BorderLayout.CENTER) ;
		
		this.retour = new JButton("Retour") ;
		this.suivant = new JButton("Suivant") ;
		
		this.retour.addActionListener(new RetourAction());
		this.suivant.addActionListener(new SuivantAction());
		
		pTemp = new JPanel() ;
		pTemp.add(this.retour);
		pTemp.add(Box.createHorizontalStrut(50)) ;
		pTemp.add(this.suivant);
		this.add(pTemp, BorderLayout.SOUTH);
		
		this.setVisible(true);
	}
	
	/**
	 * 
	 * 
	 * @author ALLAM Jonathan - <thomasallam@outlook.fr> - 08/01/2018
	 *
	 */
	private class RetourAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			dispose() ;
			
			new FenetreImageInput() ;
		}
	}
	
	/**
	 * Action liee au bouton "Suivant" : acces a la fenetre suivante si aucune erreur dans les parametres saisis
	 * 
	 * @author ALLAM Jonathan - <thomasallam@outlook.fr> - 08/01/2018
	 *
	 */
	private class SuivantAction implements ActionListener {
		/**
		 * Indique si la chaine de caractere est constituee uniquement de chiffre.
		 * 
		 * @param s Chaine de caractere a tester.
		 * 
		 * @return Renvoie true si constituee que de chiffres ou false sinon.
		 */
		public boolean onlyInteger(String s) {
			if (s.isEmpty()) return false ;
			
			boolean ans = true ;
			for (int i = 0; i<s.length() && ans; i++) {								// Pour chaque lettre verifier qu'est bien un chiffre.
				if (!(('0'<=s.charAt(i)) && (s.charAt(i) <= '9'))) {
					ans = false ;
				}
			}
			
			return ans ;
		}
		
		/**
		 * Action liee
		 */
		public void actionPerformed(ActionEvent e) {
			String sl = longueur.getText() ;
			String sla = largeur.getText() ;
			if (!this.onlyInteger(sl)) {
				longueur.setBorder(BorderFactory.createLineBorder(Color.red)) ;
				
				new FenetreException("La longueur doit etre specifiee et comprise entre 1 et 800") ;
			} else {
				longueur.setBorder(null) ;
				
				if (!this.onlyInteger(sla)) {
					largeur.setBorder(BorderFactory.createLineBorder(Color.red)) ;
					
					new FenetreException("La largeur doit etre specifiee et comprise entre 1 et 800") ;
				} else {
					largeur.setBorder(null) ;
					
					int l = Integer.parseInt(sl) ;
					int la = Integer.parseInt(sla) ;
					
					if ((800 < l) && (l != 0)) {
						longueur.setBorder(BorderFactory.createLineBorder(Color.red)) ;
						
						new FenetreException("La longueur doit etre specifiee et comprise entre 1 et 800") ;
					} else {
						if ((800 < la) && (la != 0)) {
							largeur.setBorder(BorderFactory.createLineBorder(Color.red)) ;
							
							new FenetreException("La largeur doit etre specifiee et comprise entre 1 et 800") ;
						} else {
							longueur.setBorder(null) ;
							largeur.setBorder(null) ;
							
							Terrain.create(l, la) ;
							
							try {
								Terrain.getInstance().stat.setNoms(nomSimulation, path);
							} catch (Exception ee) {}
							
							dispose() ;
							
							new FenetreCreationTerrain() ;
						}
					}
				}
			}
		}
	}
}
