package trialAndOutcome;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class GenericInverseOutcome extends Outcome{
	
	
	public String getReviewTitle() {
	return reviewTitle;
}

public void setReviewTitle(String reviewTitle) {
	this.reviewTitle = reviewTitle;
}
@XmlElement(name = "Outcome")
public String getOutcomeName() {
	return outcomeName;
}

	@XmlElement(name = "Subgroup")
	public String getSubgroupName() {
		return subgroupName;
	}

	public void setSubgroupName(String subgroupName) {
		this.subgroupName = subgroupName;
	}
	
	



	public void setOutcomeName(String outcomeName) {
		this.outcomeName = outcomeName;
	}


	
	public String getOutcomeType() {
		return outcomeType;
	}
	@XmlElement(name = "Comparison")
	public String getComparisonName() {
		return comparisonName;
	}

	public void setComparisonName(String comparisonName) {
		this.comparisonName = comparisonName;
	}

	public String getInterventionGroupName() {
		return interventionGroupName;
	}

	public void setInterventionGroupName(String interventionGroupName) {
		this.interventionGroupName = interventionGroupName;
	}

	public String getInterventionProse() {
		return interventionProse;
	}

	public void setInterventionProse(String interventionProse) {
		this.interventionProse = interventionProse;
	}

	public String getControlGroupName() {
		return controlGroupName;
	}

	public void setControlGroupName(String controlGroupName) {
		this.controlGroupName = controlGroupName;
	}

	public String getControlProse() {
		return controlProse;
	}

	public void setControlProse(String controlProse) {
		this.controlProse = controlProse;
	}

	public float getStandardError() {
		return standardError;
	}

	public void setStandardError(float standardError) {
		this.standardError = standardError;
	}

	public float getLogOddsRatio() {
		return logOddsRatio;
	}

	public void setLogOddsRatio(float logOddsRatio) {
		this.logOddsRatio = logOddsRatio;
	}

	public float getCiStart() {
		return ciStart;
	}

	public void setCiStart(float ciStart) {
		this.ciStart = ciStart;
	}

	public float getCiEnd() {
		return ciEnd;
	}

	public void setCiEnd(float ciEnd) {
		this.ciEnd = ciEnd;
	}

	public float getEffectSize() {
		return effectSize;
	}

	public void setEffectSize(float effectSize) {
		this.effectSize = effectSize;
	}

	public float getLogCiStart() {
		return logCiStart;
	}

	public void setLogCiStart(float logCiStart) {
		this.logCiStart = logCiStart;
	}

	public float getLogCiEnd() {
		return logCiEnd;
	}

	public void setLogCiEnd(float logCiEnd) {
		this.logCiEnd = logCiEnd;
	}

	public float getLogEffectSize() {
		return logEffectSize;
	}

	public void setLogEffectSize(float logEffectSize) {
		this.logEffectSize = logEffectSize;
	}

	public int getControlTotalN() {
		return controlTotalN;
	}

	public void setControlTotalN(int controlTotalN) {
		this.controlTotalN = controlTotalN;
	}

	public int getInterventionTotalN() {
		return interventionTotalN;
	}

	public void setInterventionTotalN(int interventionTotalN) {
		this.interventionTotalN = interventionTotalN;
	}
	
	@XmlAttribute
	protected final String outcomeType = "Generic Inverse Variance";
	protected String outcomeName = "";
	protected String reviewTitle = "";
	protected String subgroupName = "";
	
	protected String comparisonName = "";
	protected String interventionGroupName = ""; 	// e.g. Vitamin E -> Group 1. Check if intervention is always group 1!!!!!!!!!!! Also: Vitamin e combination instead of drug name -> verify with prose
	protected String interventionProse = ""; // if additional prose cleaning happens this String contains the prose to double check cleaning manually
	protected String controlGroupName = "";	//e.g. Placebo
	protected String controlProse = "";
	
	protected float standardError;
	protected float logOddsRatio;
	protected float ciStart;
	protected float ciEnd;
	protected float effectSize;
	protected float logCiStart;
	protected float logCiEnd;
	protected float logEffectSize;
	protected int controlTotalN;
	protected int interventionTotalN;
	
	
	private Pattern invalidIC = Pattern.compile("(\\b[Ee]xperiment(al)?)|(\\b[Cc]ontrol(s)?)|(\\b[Cc]ombination(s)?|([Cc]omparison(s)?))");//checks the first string extracted for I/C 
	private Pattern addedInfoCombinationsIC = Pattern.compile("(\\b[Cc]ombination(s)?)[:-]\\s([\\w\\d+][.])?"); //if String includes "combination" in different variations, then either : or - followed by white space followed by either single letter or one or more digits followed by full stop
	private Pattern addedInfoOtherAntipsyIC = Pattern.compile("(\\b[Oo]ther\\s((a)?typic(al)?\\s)?(antipsychotic)(s)?)[:-]\\s([\\w\\d+][.])?");
	private Pattern splitPattern = Pattern.compile("(\\b[Vv]ersus)+|(\\b[Vv]s[.]?)+|(\\b[Cc]ompared\\s(with|to)+)");	//splits comparison name at word boundary plus Versus, versus, Vs., Vs, ...
	
	private Matcher m;
	private String[] splitComparison = new String[2];
	private String[] splitIC = new String[2];
	
	protected String revManID = "";
	
	public String getRevManID() {
		return revManID;
	}

	public void setRevManID(String revManID) {
		this.revManID = revManID;
	}


	public GenericInverseOutcome() {
		super();
	}
	
public GenericInverseOutcome(Element oeDataElement, Element comparisonNameElement, Element oeOutcomeNameElement, Element oeOutcomeElement, Element oeSubgroupElement, String review, String revManID) {
	this.revManID = revManID;
	this.reviewTitle = review;
		outcomeName = oeOutcomeNameElement.getTextContent();
		comparisonName = comparisonNameElement.getTextContent();
		
		
		if (oeSubgroupElement != null) {//because it is possible that the outcome is stored directly under the outcome node and not in a subgroup
			NodeList oeNameOfSubgroupList = oeSubgroupElement.getElementsByTagName("NAME");
			Element oeNameOfSubgroup = (Element) oeNameOfSubgroupList.item(0);//for subgroup name
			subgroupName = oeNameOfSubgroup.getTextContent();
			//System.out.println(outcomeName);
			//System.out.println(subgroupName);
		}
		
		standardError = Float.parseFloat(oeDataElement.getAttribute("SE")); //intervention and control mean
		//System.out.println("SE: " + standardError);
		logOddsRatio = Float.parseFloat(oeDataElement.getAttribute("ESTIMATE"));
		//System.out.println("LOR: " + logOddsRatio);
		ciStart = Float.parseFloat(oeDataElement.getAttribute("CI_START"));
		ciEnd = Float.parseFloat(oeDataElement.getAttribute("CI_END"));
		effectSize = Float.parseFloat(oeDataElement.getAttribute("EFFECT_SIZE"));
		logCiStart = Float.parseFloat(oeDataElement.getAttribute("LOG_CI_START"));
		logCiEnd = Float.parseFloat(oeDataElement.getAttribute("LOG_CI_END"));
		logEffectSize = Float.parseFloat(oeDataElement.getAttribute("LOG_EFFECT_SIZE"));
		controlTotalN = Integer.parseInt(oeDataElement.getAttribute("TOTAL_2"));
		interventionTotalN = Integer.parseInt(oeDataElement.getAttribute("TOTAL_1"));
		
		NodeList groupLabel1List = oeOutcomeElement.getElementsByTagName("GROUP_LABEL_1");	//for intervention name
		Node groupLabel1Node = groupLabel1List.item(0);
		Element groupLabel1Element = (Element) groupLabel1Node;
		interventionGroupName = groupLabel1Element.getTextContent().toLowerCase();
		
		NodeList groupLabel2List = oeOutcomeElement.getElementsByTagName("GROUP_LABEL_2");	//for control name
		Node groupLabel2Node = groupLabel2List.item(0);
		Element groupLabel2Element = (Element) groupLabel2Node;
		controlGroupName = groupLabel2Element.getTextContent().toLowerCase();
		
		m = invalidIC.matcher(interventionGroupName);	//to see if intervention has invalid name, see pattern for more info
		if (m.find()){
			interventionCleaner();	//intervention has to be filled and cleaned via comparison name
		}
		interventionGroupName = interventionGroupName.replaceAll("(\\s[+]\\s)|((\\b)plus(\\b)|(\\b)and(\\b)|(\\s[Aa]dded\\sto\\s))", "//");//easier to make a list later
//		System.out.println("Intervention: " + interventionGroupName); //+ " Total: " + group1Total + ". Events: " + group1Events );
	
		m = invalidIC.matcher(controlGroupName);
		if (m.find()){
			controlCleaner();
		}
		controlGroupName = controlGroupName.replaceAll("(\\s[+]\\s)|((\\b)plus(\\b)|(\\b)and(\\b)|(\\s[Aa]dded\\sto\\s))", "//");
//		System.out.println("Control: " +controlGroupName); //+ " Total: " + group2Total + ". Events: " + group2Events );
		controlGroupName = controlGroupName.toLowerCase();
		interventionGroupName = interventionGroupName.toLowerCase();
	}

private void interventionCleaner(){
	
	try {
		splitComparison = splitPattern.split(comparisonName);	//splits comparison name so that its relevant part can be cleaned
		if (splitComparison.length >= 2) {
			interventionGroupName = splitComparison[0]; //first String in this array describes intervention
			//group1Name= group1Name.substring(0, 1).toUpperCase() + group1Name.substring(1);//optional capitalisation of first character
			interventionProse = interventionGroupName; //in case the cleaning below produces sketchy output. This string is the prose
			m = addedInfoCombinationsIC.matcher(interventionGroupName); //if the intervention was labeled "combination" or similar . 
			if (m.find()) {
				splitIC = addedInfoCombinationsIC.split(interventionGroupName);
				interventionGroupName = splitIC[1].trim(); //irrelevant info according to the pattern is removed by taking the second String in this array
				interventionGroupName = interventionGroupName
						.replaceAll("(\\s[+]\\s)|((\\b)plus(\\b)|(\\b)and(\\b)|(\\s[Aa]dded\\sto\\s))", "//"); //makes a standardised list with "//" separating interventions
			} else {
				m = addedInfoOtherAntipsyIC.matcher(interventionGroupName); // if some prose about "other antipsychotic" in various ways comes up it is cleaned away similar to above
				if (m.find()) {
					splitIC = addedInfoOtherAntipsyIC.split(interventionGroupName);
					interventionGroupName = splitIC[1].trim();
					interventionGroupName = interventionGroupName
							.replaceAll("(\\s[+]\\s)|((\\b)plus(\\b)|(\\b)and(\\b)|(\\s[Aa]dded\\sto))", "//");
				}
			} 
		} else {
			interventionGroupName = "experimental";
		}
	} catch (Exception e) {
		// TODO Auto-generated catch block
		
	}
	
}
private void controlCleaner(){	//cleans names of control strings of pattern above matched. Very similar to interventionCleaner
	try {
		m = splitPattern.matcher(comparisonName);
		
		splitComparison = splitPattern.split(comparisonName);
		
		if (splitComparison.length >= 2) {
			controlGroupName = splitComparison[1];
			controlGroupName = controlGroupName.toLowerCase();
			//group2Name= group2Name.substring(0, 1).toUpperCase() + group2Name.substring(1);
			controlProse = controlGroupName; // in case the String becomes sketchy during further cleaning this saves the prose
			m = addedInfoCombinationsIC.matcher(controlGroupName);
			if (m.find()) {
				splitIC = addedInfoCombinationsIC.split(controlGroupName);
				controlGroupName = splitIC[1].trim();
				controlGroupName = controlGroupName
						.replaceAll("(\\s[+]\\s)|((\\b)plus(\\b)|(\\b)and(\\b)|(\\s[Aa]dded\\sto\\s))", "//");
			} else {
				m = addedInfoOtherAntipsyIC.matcher(controlGroupName);
				if (m.find()) {
					splitIC = addedInfoOtherAntipsyIC.split(controlGroupName);
					controlGroupName = splitIC[1].trim();
					controlGroupName = controlGroupName
							.replaceAll("(\\s[+]\\s)|((\\b)plus(\\b)|(\\b)and(\\b)|(\\s[Aa]dded\\sto))", "//");
				}
			} 
		} else {
			controlGroupName = "control";
		}
	} catch (Exception e) {
		// this is needed with review about antipsychotic switching
		e.printStackTrace();
		
	}
	
	
			
}


}
