package Fenetres;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import Database.DBConnection;
import Exceptions.FenetreErreurFatale;

/**
 * Fenetre d'affichage des resultats de toutes les simulations effectuees.
 * 
 * @author ALLAM Jonathan - <thomasallam@outlook.fr> - 06/01/2018
 * 
 * @version 0.0.1
 */
@SuppressWarnings("serial")
public class FenetreAffichageResultats extends JFrame {
	private JTable resultats ;					// Tableau : Ensemble des resultats
	private JButton retour ;					// Bouton de retour au menu.
	
	public FenetreAffichageResultats() {
		this.setTitle("Affichage des resultats simulations");
		this.setBounds(100,100, 1000,500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setVisible(true);
		
		DBConnection c = new DBConnection() ;
		String[] nomColonne = {"Nom Simulation", "Nom Carte", "Feu eteint ?", "Nombre Pompiers Mort", "Nombre habitations brulees" } ;
		
		
		try {
			c.recupererStat();
			this.resultats = new JTable(c.getResRecherche(), nomColonne) ;
			
			this.resultats.setFocusable(false);
			
			this.add(new JScrollPane(this.resultats)) ;
		} catch (SQLException e) {
			new FenetreErreurFatale(e.toString()) ;
		}
		
		
		this.retour = new JButton("Retour") ;
		JPanel pTemp = new JPanel() ;
		pTemp.add(this.retour) ;
		this.retour.addActionListener(new RetourAction());
		
		this.add(Box.createHorizontalStrut(50), BorderLayout.EAST);
		this.add(Box.createHorizontalStrut(50), BorderLayout.WEST);
		this.add(Box.createVerticalStrut(40), BorderLayout.NORTH);
		this.add(pTemp, BorderLayout.SOUTH) ;
	}
	
	private class RetourAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			dispose() ;
			
			new FenetreMenu() ;
		}
	}
}
