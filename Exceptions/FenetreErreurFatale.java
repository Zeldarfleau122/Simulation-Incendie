package Exceptions;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Fenetre indiquant une erreur fatale : son ouverture mes en pause le programme ( bloc try / catch appelant )
 * 										sa fermeture arrete le programme.
 * 
 * @author ALLAM Jonathan - <thomasallam@outlook.fr> - 06/01/2018
 * 
 * @version 0.0.1
 */
@SuppressWarnings("serial")
public class FenetreErreurFatale extends JFrame implements ActionListener {
	private JLabel erreur ;					// Texte indiquant l'erreur survenue.
	private JButton boutonFin ;				// Bouton d'arret du programme.
	
	public FenetreErreurFatale(String erreur) {
		this.erreur = new JLabel(erreur) ;
		
		this.setTitle("Erreur fatale");								// Initialisation des caracteristiques de la fenetre.
		this.setBounds(300, 300, 300, 100) ;
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JPanel p = new JPanel() ;								// Initialisation du panel contenant le texte d'erreur.
		p.add(this.erreur) ;
		this.add(p) ;
		
		p = new JPanel() ;										// Initialisation panel contenant boutonFin.
		this.boutonFin = new JButton("Fermer l'application") ;
		this.boutonFin.addActionListener(this);
		p.add(this.boutonFin) ;
		this.add(p, BorderLayout.SOUTH);
		
		this.setVisible(true);
	}
	
	/**
	 * Action d'arret du programme au clic sur boutonFin.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		System.exit(0);
	}
}
