package Exceptions;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Fenetre indiquant qu'une erreur est survenue.
 * 
 * @author ALLAM Jonathan - <thomasallam@outlook.fr> - 09/01/2017
 *
 */
@SuppressWarnings("serial")
public class FenetreException extends JFrame {
	private JLabel erreur ;
	
	/**
	 * Constructeur par defaut : affichage d'une fenetre avec un message d'erreur.
	 * 
	 * @param exception Message d'erreur a afficher.
	 */
	public FenetreException(String exception) {	
		this.setTitle("Erreur");
		this.setLocation(500, 500) ;
		this.addWindowListener(new Fermeture());
		
		JPanel p = new JPanel() ;
		this.erreur = new JLabel(exception) ;
		
		p.add(this.erreur) ;
		
		this.add(p) ;
		
		Dimension d = this.erreur.getPreferredSize() ;
		this.setSize(d.width+50, d.height+50);
		
		this.setVisible(true);
		this.toFront();
	}
	
	/**
	 * Evenement de fermture de la fenetre.
	 * 
	 * @author ALLAM Jonathan - <thomasallam@outlook.fr> - 09/01/2017
	 *
	 */
	private class Fermeture extends WindowAdapter {
		/**
		 * Ferme la fenetre. Sans arreter le programme.
		 */
		@Override
		public void windowClosing(WindowEvent e) {
			dispose() ;
		}
	}
}
