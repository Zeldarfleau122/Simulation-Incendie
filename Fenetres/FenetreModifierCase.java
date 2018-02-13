package Fenetres;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import Database.DBConnection;
import Environnement.CaractCaseType;
import Environnement.CaseType;
import Exceptions.FenetreException;

/**
 * Fenetre de modification d'un type de case par l'utilisateur apres selection dans la fenetreAdministration.
 * 
 * @author ALLAM Jonathan - <thomasallam@outlook.fr> - 06/01/2018
 * 
 * @version 0.0.1
 */
@SuppressWarnings("serial")
public class FenetreModifierCase extends JFrame {
	private JLabel lNom ;
	private String nomCase ;
	
	private JLabel lR ;							// Labels et inputs : couleur ( RGB ) du type de la case
	private JLabel lG ;
	private JLabel lB ;
	private JTextField R ;
	private JTextField G ;
	private JTextField B ;
	
	private JLabel lAccess ;					// Label et input : accessibilite de la case.
	private JTextField access ;
	private JLabel lInflammable ;				// Label et input : indique si case inflammable ou non.
	private JTextField inflammable ;
	
	private JLabel lRecordStat ;				// Label et input : indique si case possedera un suivie statistique ou non.
	private JTextField recordStat ;
	
	private JLabel lProbBrul ;					// Label et input : probabilite d'etre brulee ( <= 1.0 )
	private JTextField probBrul ;
	
	private JButton retour ;					// Bouton de retour a la fenetre precedente. ( Ici fenetreAdministration )
	private JButton modifier ;					// Bouton pour modifier le type de case selectionne
	
	/**
	 * Constructeur de la fenetre a partir du nom de la case a modifier ( en remplissant les divers champs avec ses caract )
	 * 
	 * @param nomCase Nom de la case a modifier.
	 */
	public FenetreModifierCase(String nomCase) {
		this.setBounds(100,100,300,500);
		this.setTitle("Modifier Case - " + nomCase);
		
		CaractCaseType ct = CaseType.getCaractCaseType(nomCase) ;
		
		lNom = new JLabel("Nom case : " + nomCase) ;
		this.nomCase = nomCase ;
		
		JPanel pCenter ;
		JPanel p ;
		JPanel pTemp ;
		
		pTemp = new JPanel() ;
		pTemp.add(lNom) ;
		
		this.add(pTemp, BorderLayout.NORTH) ;
		
		Integer iTemp ;
		
		this.lR = new JLabel("R : ") ;
		this.lG = new JLabel("G : ") ;
		this.lB = new JLabel("B : ") ;
		this.R = new JTextField(4) ;
		iTemp = ct.getColor().getRed() ;
		this.R.setText(iTemp.toString()); 
		this.G = new JTextField(4) ;
		iTemp = ct.getColor().getGreen();
		this.G.setText(iTemp.toString());
		this.B = new JTextField(4) ;
		iTemp = ct.getColor().getBlue();
		this.B.setText(iTemp.toString());
		
		this.lR.setLabelFor(R);
		this.lR.setHorizontalAlignment(SwingConstants.LEFT);
		this.lG.setLabelFor(G);
		this.lG.setHorizontalAlignment(SwingConstants.LEFT);
		this.lB.setLabelFor(B);
		this.lB.setHorizontalAlignment(SwingConstants.LEFT);
		
		pCenter = new JPanel() ;
		pCenter.add(Box.createVerticalStrut(25));
		
		pCenter.setLayout(new BoxLayout(pCenter, BoxLayout.PAGE_AXIS));
		
		p = new JPanel() ;
		pTemp = new JPanel() ;
		
		pTemp.add(this.lR);
		pTemp.add(this.R) ;
		pTemp.add(this.lG);
		pTemp.add(this.G) ;
		pTemp.add(this.lB);
		pTemp.add(this.B) ;
		
		p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS)) ;
		p.add(new JLabel("Couleur :")) ;
		p.add(pTemp);
		pCenter.add(p);
		
		this.lAccess = new JLabel("Accessibilite ( 1 ou 0 ) : ") ;
		this.lInflammable = new JLabel("Inflammable ( 1 ou 0 ) : ") ;
		this.access = new JTextField(3) ;
		this.access.setText(ct.getAccessible() ? "1" : "0");
		this.inflammable = new JTextField(3) ;
		this.inflammable.setText(ct.getAccessible() ? "1" : "0");
		
		pTemp = new JPanel() ;
		pTemp.add(this.lAccess);
		pTemp.add(this.access);
		pCenter.add(pTemp) ;
		
		pTemp = new JPanel() ;
		pTemp.add(this.lInflammable);
		pTemp.add(this.inflammable);
		pCenter.add(pTemp) ;

		
		this.lProbBrul = new JLabel("Prob. de bruler ( < 1 ) : ") ;
		this.probBrul = new JTextField(3) ;
		Double dTemp = ct.getProbaBruler() ;
		this.probBrul.setText(dTemp.toString());
		
		pTemp = new JPanel() ;
		pTemp.add(this.lProbBrul);
		pTemp.add(this.probBrul);
		
		pCenter.add(pTemp);
		
		
		this.lRecordStat = new JLabel("Suivie statistique ( 1 ou 0 ) : ") ;
		this.recordStat = new JTextField(3) ;
		this.recordStat.setText(ct.getRecordStat() ? "1" : "0");
		
		pTemp = new JPanel() ;
		pTemp.add(this.lRecordStat);
		pTemp.add(this.recordStat);
		
		pCenter.add(pTemp);
		
		this.add(pCenter);
		
		JPanel pSouth = new JPanel() ;
		
		this.retour = new JButton("Retour") ;
		this.retour.addActionListener(new RetourAction());
		this.modifier = new JButton("Modifier") ;
		this.modifier.addActionListener(new ModifierAction());
		
		pSouth.add(this.retour) ;
		pSouth.add(Box.createHorizontalStrut(100)) ; 
		pSouth.add(this.modifier);
		
		this.add(pSouth, BorderLayout.SOUTH) ;
		
		this.setVisible(true);
	}
	
	/**
	 * Action associee au bouton "retour".
	 */
	private class RetourAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			dispose() ;
			
			new FenetreAdministration() ;
		}
	}
	
	/**
	 * Action associee au bouton "modifier".
	 */
	private class ModifierAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String[] parameters = new String[6] ;
			
			parameters[0] = nomCase ;
			parameters[1] = access.getText() ;
			parameters[2] = inflammable.getText() ;
			parameters[4] = recordStat.getText() ;
			parameters[5] = probBrul.getText() ;
			
			int r, g, b ;
			r = Integer.parseInt(R.getText()) ;
			g = Integer.parseInt(G.getText()) ;
			b = Integer.parseInt(B.getText()) ;
			Color c = new Color(r,g,b) ;
			
			parameters[3] = Integer.toString(c.getRGB()) ;
			
			try {
				DBConnection.modifierCase(parameters[0], parameters[1], parameters[2], parameters[3], parameters[4], "0", parameters[5]);
			
				dispose() ;
				
				CaseType.creationCaseType() ;
				new FenetreAdministration() ;
			} catch (SQLException exc) {
				new FenetreException(exc.toString()) ;
			}
		}
	}
}
