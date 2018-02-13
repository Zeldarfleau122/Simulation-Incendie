package Fenetres;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import Database.DBConnection;
import Environnement.CaseType;
import Exceptions.FenetreErreurFatale;
import Exceptions.FenetreException;

/**
 * Fenetre de modification des types de case / de leur ajout, par l'utilisateur.
 * Contient une liste geree grace a CaseDisplayRenderer
 * 
 * @author ALLAM Jonathan - <thomasallam@outlook.fr> - 02/01/2018
 * 
 * @version 0.0.1
 * 
 * @see CaseDisplayRenderer
 */
@SuppressWarnings("serial")
public class FenetreAdministration extends JFrame {
	private DefaultListModel<String> d ;		// Ensemble des noms des types de case actuel
	private JList caseDisplay ;					// Liste des types de case cf. CaseDisplayRenderer
	private JButton ajout ;						// Bouton d'ajout d'un type de case
	private JButton modifier ;					// Bouton de modification d'un type de case
	private JButton supprimer ;					// Bouton de suppression d'un type de case
	private JButton retour ;					// Bouton de retour menu
	
	/**
	 * Constructeur par defaut.
	 */
	public FenetreAdministration() {
		this.setTitle("Administration");								// Caracteristiques generales de la fenetre
		this.setBounds(100,100,400,300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.d = new DefaultListModel<String>() ;						// Recuperation noms des types de case
		for (Object s : CaseType.ensCase.keySet().toArray())
			d.addElement((String) s);
		
		this.caseDisplay = new JList(d) ;								// Initialisation JList
		this.caseDisplay.setCellRenderer(new CaseDisplayRenderer());
		
		this.ajout = new JButton("Nouvelle Case") ;						// Creation des boutons
		this.modifier = new JButton("Modifier") ;
		this.supprimer = new JButton("Supprimer") ;
		this.retour = new JButton("Retour") ;
		
																		// Placement dans la fenetre des divers elements
		JPanel p = new JPanel() ;
		
		p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
		p.add(Box.createVerticalStrut(30));
		p.add(this.ajout) ;
		p.add(Box.createVerticalStrut(30));
		p.add(this.modifier);
		p.add(Box.createVerticalStrut(30));
		p.add(this.supprimer);
		
		this.add(p, BorderLayout.EAST) ;
		
		p = new JPanel() ;
		p.add(this.retour);
		
		this.add(p, BorderLayout.SOUTH) ;
		
		p = new JPanel() ;
		p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
		p.add(Box.createVerticalStrut(50));
		p.add(new JScrollPane(this.caseDisplay)) ;
		p.add(Box.createVerticalStrut(50));
		
		JPanel pTemp = new JPanel() ;
		pTemp.add(p) ;
		
		this.add(pTemp) ;
		this.add(Box.createHorizontalStrut(50), BorderLayout.WEST) ;
		
		this.setVisible(true);
		
		try {																// Verification de la bonne connection a la BD. Sinon n'associer aucune action aux boutons
			CaseType.creationCaseType() ;
			this.ajout.addActionListener(new AjoutAction());
			this.retour.addActionListener(new RetourAction());
			this.supprimer.addActionListener(new SupprimerAction());
			this.modifier.addActionListener(new ModifierAction());
		} catch (SQLException e) {
			new FenetreErreurFatale(e.toString()) ;							// Affichage erreur fatale ( forcer la fermeture du programme. )
		}
	}
	
	/**
	 * Action associee au bouton d'ajout d'un type de case.
	 */
	private class AjoutAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			dispose() ;
			
			new FenetreAjoutCase() ;
		}
	}
	
	/**
	 * Action associee au bouton "modifier".
	 */
	private class ModifierAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (caseDisplay.getSelectedValue() != null) {
				dispose() ;
			
				new FenetreModifierCase((String) caseDisplay.getSelectedValue()) ;
			}
		}
	}
	
	/**
	 * Action retour menu.
	 */
	private class RetourAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			dispose() ;
			
			new FenetreMenu() ;
		}
	}

	/**
	 * Action associee au bouton "supprimer".
	 */
	private class SupprimerAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (caseDisplay.getSelectedValue() != null) {
				String selection = (String) caseDisplay.getSelectedValue() ;
				if ((selection.compareTo("Arbre") != 0) && (selection.compareTo("Neutre") != 0) && (selection.compareTo("Habitation")!=0) 
						&& (selection.compareTo("Eau")!=0) && (selection.compareTo("Cendre")!=0)) {
					try {
						DBConnection.supprimerCaseType((String) caseDisplay.getSelectedValue());
						
						CaseType.creationCaseType() ;
						
						d.removeElementAt(caseDisplay.getSelectedIndex());
					} catch (SQLException except) {
						new FenetreException(except.toString()) ;
					}
				} else return ;
			}
		}
	}
}
