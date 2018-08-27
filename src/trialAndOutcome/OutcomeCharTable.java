package trialAndOutcome;

import java.util.ArrayList;
import java.util.List;

public class OutcomeCharTable {
	
	public OutcomeCharTable() {//empty for xml marshalling
	}
	
	protected List<OutcomeCharTable> outcomeCharTableList = new ArrayList<>();//the list where all outcomes from Characteristics table will be stored
	

	private UnusedOutcome unusedObj;
	private UsedOutcome usedObj;
	
	
	public OutcomeCharTable(String revManID,String reviewTitle, String outcomesString) {
		
		revManID = revManID.replaceAll("_x00df_", "ß").replaceAll("(_x002d_)", "-").replaceAll("(_x0026_)", "&").replaceAll("_x00e8_", "è").replaceAll("_x00f6_", "ö").replaceAll("_x00fc_", "ü").replaceAll("_x002b_", "+").replaceAll("_x002f_", "/").replaceAll("_x00a0_", " ").replaceAll("_x002c_", ",").replaceAll("_x0028_", "(").replaceAll("_x0029_", ")").replaceAll("_x00e7_", "ç").replaceAll("_x0027_", "'").replaceAll("_x002a_", "*").replaceAll("_x00e9_", "é").replaceAll("_x00e4_", "ä").replaceAll("_x00b4_", "´");
		
		//Splits the paragraph in 2 parts: first are used outcomes, second index are unused
		String[] usedUnused = outcomesString.split("((?=(Unusable.?[:-–]))|(?=(Un?able\\sto\\suse.?[:-–]))|(?=((Data\\s)?[nN]ot\\sable\\sto\\suse.?[:-–]))|(?=(Not?\\suse?able(\\sdata)?.?[:-–]))|(?=(Not\\sused\\sin\\s(this|the)?\\s?review.?[:-–])))", 2);
		
		try {
			String[] used = usedUnused[0].split("\\n");
			for (String thisItem : used) {
				thisItem = thisItem.trim();
				if (thisItem.equals("") == false) {//if it is not empty
					usedObj = new UsedOutcome(thisItem, revManID, reviewTitle);
					outcomeCharTableList.add(usedObj);
				}
				
			}
		} catch (Exception e) {
			// in case this section of the table is empty
		
		}
		
		if (usedUnused.length == 2) {
			String[] unused = usedUnused[1].split("\\n");
			for (String thisItem : unused) {
				thisItem = thisItem.trim();
				if (thisItem.equals("") == false) {
					unusedObj = new UnusedOutcome(thisItem, revManID, reviewTitle);
					outcomeCharTableList.add(unusedObj);
				}
				
			}
		}
		
	}
	
	public List<OutcomeCharTable> getOutcomeCharTableList() {
		return outcomeCharTableList;
	}


	public void setOutcomeCharTableList(List<OutcomeCharTable> outcomeCharTableList) {
		this.outcomeCharTableList = outcomeCharTableList;
	}
}
