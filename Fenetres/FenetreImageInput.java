package Fenetres;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Environnement.ImageCarte;
import Environnement.Terrain;
import Exceptions.FenetreException;

/**
 * Fenetre d'interaction avec l'utilisateur : demande du chemin absolu a l'image a utiliser pour la simulation.
 * 
 * @author ALLAM Jonathan - <thomasallam@outlook.fr> - 06/01/2018
 *
 */
@SuppressWarnings("serial")
public class FenetreImageInput extends JFrame {
	private JLabel lNomSimulation ;
	private JTextField nomSimulation ;
	private JLabel lFichierInput ;				// Label du champ texte contenant le chemin absolu a l'image a utiliser pour la simulation.
	private JTextField fichierInput ;			// Champ texte contenant le chemin absolu.
	private JButton apercu ;					// Bouton permettant une observation de l'image convertie en Terrain.
	private JButton continuer ;					// Passage a l'etape suivante de l'initialisation de la simulation
	private JButton retour ;					// Retour au menu.
	private JButton creationPerso ;				// Creation d'un terrain personalise.
	private ImageCarte fichier ;				// Image issue du chemin absolu indiquee.
	
	/**
	 * Constructeur par defaut de la fenetre.
	 */
	public FenetreImageInput() {
		this.setTitle("Debut Initialisation Terrain");
		this.setLocation(100, 100);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.lNomSimulation = new JLabel("Nom simulation : ") ;
		this.nomSimulation = new JTextField(10) ;
		this.nomSimulation.setText("");
		
		JPanel pNorth = new JPanel() ;
		pNorth.add(this.lNomSimulation) ;
		pNorth.add(nomSimulation);
		
		this.add(pNorth, BorderLayout.NORTH);
		
		JPanel p = new JPanel() ;				// Panel NORTH contenant le label et le champs texte relatif au chemin absolu.
		this.lFichierInput = new JLabel() ;				// Initialisation du label
		this.fichierInput = new JTextField() ;			// Initialisation du champs texte.
		
		this.lFichierInput.setText("Image a convertir : ");				// init. des caracteristiques du champ texte.
		this.fichierInput.setColumns(20);								// Choix de "20" arbitraire.
		
		p.setLayout(new FlowLayout());							// Creation du panneau du haut ( NORTH )
		p.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		p.add(this.lFichierInput);
		p.add(this.fichierInput);
		
		this.add(p, BorderLayout.CENTER) ;				// Ajout du panneau du haut.
		
		this.setSize(600, 150);								// Fixer la dimension de la fenetre. ( en fonction des panneaux contenus )
		
		this.apercu = new JButton("Appercu") ;				// Initialisation des boutons
		this.continuer = new JButton("Continuer") ;
		this.creationPerso = new JButton("Creer une carte") ;
		this.retour = new JButton("Retour") ;
		
		this.apercu.addActionListener(new ApercuAction());	// Association avec les actions associees.
		this.continuer.addActionListener(new ContinuerAction());
		this.retour.addActionListener(new RetourAction());
		this.creationPerso.addActionListener(new CreationPersoAction());
		
		JPanel pSouth = new JPanel() ;						// Creation du panneau du bas ( SOUTH )
		
		pSouth.add(this.retour, BorderLayout.WEST) ;
		pSouth.add(Box.createHorizontalStrut(60), BorderLayout.CENTER) ;
		pSouth.add(this.creationPerso, BorderLayout.CENTER) ;
		pSouth.add(Box.createHorizontalStrut(50), BorderLayout.CENTER) ;
		pSouth.add(this.apercu, BorderLayout.EAST) ;				// Formation de ce panneau 
		pSouth.add(Box.createHorizontalStrut(5), BorderLayout.EAST) ;
		pSouth.add(this.continuer, BorderLayout.EAST) ;
		
		this.add(pSouth, BorderLayout.SOUTH);				// Ajout a la fenetre.
		
		this.setVisible(true) ;
	}
	
	private class CreationPersoAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (nomSimulation.getText().isEmpty()) {
				new FenetreException("Nom de simulation non valide.") ;
				return ;
			}
			
			dispose() ;
			
			new FenetreChoixCreationTerrain(nomSimulation.getText(), "Carte Perso") ;
		}
	}
	/**
	 * Action liee au bouton "Apercu" de la fenetre.
	 * 
	 * @author ALLAM Jonathan - <thomasallam@outlook.fr> - 06/01/2018
	 * 
	 * @version 0.0.1
	 */
	private class ApercuAction implements ActionListener {
		/**
		 * Au clic : recuperation de l'image et du terrain associe puis ouverture de la fenetre d'apercu.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			String path = fichierInput.getText() ;
			
			try {
				fichier = new ImageCarte(path) ;
				fichier.toTerrain();
			
				fichierInput.setBorder(null) ;
				
				new FenetreApercu() ;
			} catch (IOException exception) {
				fichierInput.setBorder(BorderFactory.createLineBorder(Color.red)) ;
				new FenetreException(exception.toString()) ;
			}
		}
	}

	/**
	 * Action liee au bouton "Continuer" de la fenetre.
	 * 
	 * @author ALLAM Jonathan - <thomasallam@outlook.fr> - 06/01/2018
	 * 
	 * @version 0.0.1
	 */
	private class ContinuerAction implements ActionListener {
		/**
		 * Au clic : fermeture de la fenetre actuelle et ouverture de la fenetre d'ajout de case / d'ateur ( FenetreCreationTerrain )
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			if (nomSimulation.getText().isEmpty()) {
				new FenetreException("Nom de simulation non valide.") ;
				
				nomSimulation.setBorder(BorderFactory.createLineBorder(Color.red)) ;
				
				return ;
			}
			
			nomSimulation.setBorder(BorderFactory.createLineBorder(Color.black)) ;
			
			String path = fichierInput.getText() ;				// Recuperation de l'image
			
			try {
				fichier = new ImageCarte(path) ;					// Analyse et obtention du terrain associe.
				fichier.toTerrain();
			
				Terrain.getInstance().stat.setNoms(nomSimulation.getText(), path);
				
				fichierInput.setBorder(null) ;
				
				dispose() ;
			
				new FenetreCreationTerrain() ;
			} catch (IOException exception) {
				fichierInput.setBorder(BorderFactory.createLineBorder(Color.red)) ;
				new FenetreException(exception.toString()) ;
			} catch (Exception except) {}
		}
	}
	
	/**
	 * Action liee au bouton "Retour" de la fenetre : effectue un retour au menu.
	 * 
	 * @author ALLAM Jonathan - <thomasallam@outlook.fr> - 06/01/2018
	 *
	 * @Version 0.0.1
	 */
	private class RetourAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			dispose() ;
			
			new FenetreMenu() ;
		}
	}
}
