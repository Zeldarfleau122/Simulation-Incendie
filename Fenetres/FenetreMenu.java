package Fenetres;

import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import Environnement.CaseType;
import Exceptions.FenetreErreurFatale;

/**
 * Fenetre Menu : Comporte les actions possibles a effectuer et initialise l'enchainement de fenetre associe.
 * 
 * @author ALLAM Jonathan - <thomasallam@outlook.fr> - 06/01/2018
 *
 */
@SuppressWarnings("serial")
public class FenetreMenu extends JFrame {
	private JButton toSimulation ;				// Lancer une simulation : commencement par le choix de la carte.
	private JButton toDB ;						// Acceder a la base de donnee : vision de la table de l'ensemble des resultats.
	private JButton toAdministration ;			// Administrer / Options : modification du comportement du programme.
	
	/**
	 * Constructeur par defaut.
	 */
	public FenetreMenu() {
		this.setIconImage(new ImageIcon("D:\\Informatique\\INFO4A\\Projet POO\\images\\icon_Fenetres.png").getImage());
		this.toSimulation = new JButton("Nouvelle simulation") ;							// Initialisation des boutons
		this.toDB = new JButton("Interroger la base de donnee") ;
		this.toAdministration = new JButton("Administration") ;
		
		this.setTitle("Menu");									// Caracteristiques de la fenetre
		this.setBounds(100,100,500,350);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel p = new JPanel() ;															// Mise en forme des boutons ( centres )
		p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
		this.toSimulation.setAlignmentX(Component.CENTER_ALIGNMENT);
		p.add(Box.createVerticalStrut(50));
		p.add(this.toSimulation) ;
		p.add(Box.createVerticalStrut(50));
		
		this.toDB.setAlignmentX(Component.CENTER_ALIGNMENT);
		p.add(this.toDB);
		p.add(Box.createVerticalStrut(50));
		
		this.toAdministration.setAlignmentX(Component.CENTER_ALIGNMENT);
		p.add(this.toAdministration);
		p.add(Box.createVerticalStrut(50));
		
		this.add(p);

		this.setVisible(true);
		
		try {
			CaseType.creationCaseType();
			this.toSimulation.addActionListener(new OuvrirSimulation());						// Actions associees aux boutons
			this.toDB.addActionListener(new OuvrirDB());
			this.toAdministration.addActionListener(new OuvrirAdministration());
		} catch (SQLException e) {
			new FenetreErreurFatale("Erreur connection à la base de donnée. Vérifier le bon lancement du server local et son paramétrage.") ;
		}
	}
	
	/**
	 * Action associe au bouton "toSimulation"
	 * 
	 * @author  ALLAM Jonathan - <thomasallam@outlook.fr> - 06/01/2018
	 *
	 */
	private class OuvrirSimulation implements ActionListener {
		/**
		 * Action : acceder a la fenetre d'initialisation de la carte.
		 */
		public void actionPerformed(ActionEvent e) {
			dispose() ;
			
			new FenetreImageInput() ;
		}
	}
	
	/**
	 * Action associee au bouton "toDB"
	 * 
	 * @authorALLAM Jonathan - <thomasallam@outlook.fr> - 06/01/2018
	 *
	 */
	private class OuvrirDB implements ActionListener {
		/**
		 * Action : acceder a la fenetre de communication avec la base de donnee.
		 */
		public void actionPerformed(ActionEvent e) {
			dispose() ;
			
			new FenetreAffichageResultats() ;
		}
	}
	
	private class OuvrirAdministration implements ActionListener {
		/**
		 * Action : acceder a la fenetre de communication avec la base de donnee.
		 */
		public void actionPerformed(ActionEvent e) {
			dispose() ;
			
			new FenetreAdministration() ;
		}
	}
	
	public static void main(String[] args) {
		new FenetreMenu() ;
	}
}
