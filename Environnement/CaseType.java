package Environnement;

import java.sql.SQLException;
import java.util.TreeMap;

import Database.DBConnection;

/**
 * Enumeration de l'ensemble des types de cases avec leurs caracteristiques :
 * 		accessible ( la case peut contenir un acteur ou non ), inflammable ( la case peut etre brulee ) et sa couleur.
 * 
 * @author ALLAM Jonathan - <thomasallam@outlook.fr> - 29/12/2017
 *
 * @version 0.0.2
 */
public class CaseType {
	public static TreeMap<String, CaractCaseType> ensCase ;
	
	private static CaseType uniqueInstance ;
	
	public CaseType() throws SQLException {
		CaseType.ensCase = new TreeMap<String, CaractCaseType>() ;
		
		DBConnection ensBD = new DBConnection() ;
		Object[][] res = ensBD.recupererEnsCaseType() ;
		CaractCaseType caractTemp ;
		
		for (int i = 0; i < res.length; i++) {
			caractTemp = new CaractCaseType((String) res[i][0], (boolean) res[i][1], (boolean) res[i][2], (int) res[i][3], (boolean) res[i][4], (boolean) res[i][5], (double) res[i][6]) ;
		
			CaseType.ensCase.put((String) res[i][0], caractTemp) ;
		}
	}
	
	public static void reload() throws SQLException {
		DBConnection ensBD = new DBConnection() ;
		Object[][] res = ensBD.recupererEnsCaseType() ;
		CaractCaseType caractTemp ;
		
		CaseType.ensCase = new TreeMap<String, CaractCaseType>() ;
		
		for (int i = 0; i < res.length; i++) {
			if (!(CaseType.ensCase.containsKey((String) res[i][0]))) {
				caractTemp = new CaractCaseType((String) res[i][0], (boolean) res[i][1], (boolean) res[i][2], (int) res[i][3], (boolean) res[i][4], (boolean) res[i][5], (double) res[i][6]) ;
		
				CaseType.ensCase.put((String) res[i][0], caractTemp) ;
			}
		}
	}
	
	public static CaseType creationCaseType() throws SQLException {
		if (uniqueInstance == null)
			CaseType.uniqueInstance = new CaseType() ;
		else
			CaseType.reload() ;
		
		return CaseType.uniqueInstance ;
	}
	
	public static CaractCaseType getCaractCaseType(String nom) {
		return CaseType.ensCase.get(nom) ;
	}
}
