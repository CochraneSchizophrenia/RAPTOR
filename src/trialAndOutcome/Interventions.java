package trialAndOutcome;

import java.util.ArrayList;
import java.util.List;

public class Interventions {
	
	public Interventions() {
		
	}
	
	
	
	protected List<Interventions> interventionList = new ArrayList<>();
	
	private ProbablyIntervention probObj;
	private UnlikelyIntervention unlikelyObj;
	private UnclearIntervention unclearObj;
	
	public Interventions(String revManID,String reviewTitle, List <String> interventionsList) {
		
		revManID = revManID.replaceAll("_x00df_", "ß").replaceAll("(_x002d_)", "-").replaceAll("(_x0026_)", "&").replaceAll("_x00e8_", "è").replaceAll("_x00f6_", "ö").replaceAll("_x00fc_", "ü").replaceAll("_x002b_", "+").replaceAll("_x002f_", "/").replaceAll("_x00a0_", " ").replaceAll("_x002c_", ",").replaceAll("_x0028_", "(").replaceAll("_x0029_", ")").replaceAll("_x00e7_", "ç").replaceAll("_x0027_", "'").replaceAll("_x002a_", "*").replaceAll("_x00e9_", "é").replaceAll("_x00e4_", "ä").replaceAll("_x00b4_", "´");
			
		
		
		for(String thisItem : interventionsList) {
			thisItem = thisItem.trim();
			if (thisItem.equals("") == false) {
				if (thisItem.matches("(([1-9])|([ABCDE]\\s?))[.:]\\s?.*")) {//if String starts with a number, followed by . or :, or if it is lettered
					probObj = new ProbablyIntervention(thisItem, revManID, reviewTitle);
					interventionList.add(probObj);
				} else if (thisItem.matches("^([a-zA-Z]+\\s?){1,4}:.*")) { //If this String starts with up to 4 words that are followed by a :
					unclearObj = new UnclearIntervention(thisItem, revManID, reviewTitle);
					interventionList.add(unclearObj);
					
				} else {//most likely this String is a comment or extremely unpredictable.
					unlikelyObj = new UnlikelyIntervention(thisItem, revManID, reviewTitle);
					interventionList.add(unlikelyObj);
				}
			}
			
		}
		
	}
	
	

	public List<Interventions> getInterventionList() {
		return interventionList;
	}



	public void setInterventionList(List<Interventions> interventionList) {
		this.interventionList = interventionList;
	}



	

	
	
	

}
