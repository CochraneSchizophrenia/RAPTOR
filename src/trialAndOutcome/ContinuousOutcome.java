package trialAndOutcome;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
//@XmlRootElement(name = "ContOutcome")
public class ContinuousOutcome extends Outcome{

	@XmlAttribute
    protected final String outcomeType ="Continuous";
	
	protected String comparisonName = ""; 	// e.g. VITAMIN E versus PLACEBO
	protected String outcomeName = ""; 	// e.g. Tardive dyskinesia: 1. Not improved to a clinically important extent
	protected String interventionGroupName = ""; 	// e.g. Vitamin E -> Group 1. Check if intervention is always group 1!!!!!!!!!!! Also: Vitamin e combination instead of drug name -> verify with prose
	protected String interventionProse = ""; // if additional prose cleaning happens this String contains the prose to double check cleaning manually
	protected String controlGroupName = "";	//e.g. Placebo
	protected String controlProse = "";
	//protected String graphLabel1 = "";
	//protected String graphLabel2 = "";
	protected String subgroupName ="";	//e.g. short-term
	
	protected float interventionMean;
	protected float controlMean;
	protected float interventionSD;
	protected float controlSD;
	protected int interventionTotalN;
	protected int controlTotalN;
	protected String reviewTitle = "";
	
	
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
	
	//What does "ORDER" attribute mean?
	//extract nothing to do with CI, weight, fixed/ random effects, 
	
	ContinuousOutcome(Element contDataElement, Element comparisonNameElement, Element contOutcomeNameElement, Element contOutcomeElement, Element contSubgroupElement, String review, String revManID) {
		this.revManID = revManID;
		
		this.reviewTitle = review;
		
		NodeList groupLabel1List = contOutcomeElement.getElementsByTagName("GROUP_LABEL_1");	//for intervention name
		Node groupLabel1Node = groupLabel1List.item(0);
		Element groupLabel1Element = (Element) groupLabel1Node;
		interventionGroupName = groupLabel1Element.getTextContent().toLowerCase();	//gets intervention name and puts all in lower case
		//group1Name= group1Name.substring(0, 1).toUpperCase() + group1Name.substring(1);	//optional: capitalises first letter of intervention name
		
		NodeList groupLabel2List = contOutcomeElement.getElementsByTagName("GROUP_LABEL_2");	//for control name
		Node groupLabel2Node = groupLabel2List.item(0);
		Element groupLabel2Element = (Element) groupLabel2Node;
		controlGroupName = groupLabel2Element.getTextContent().toLowerCase();
		//group2Name = group2Name.substring(0, 1).toUpperCase() + group2Name.substring(1);
		
		//NodeList graphLabel1List = contOutcomeElement.getElementsByTagName("GRAPH_LABEL_1");
		//Element graphLabel1Element = (Element) graphLabel1List.item(0);
		//graphLabel1 = graphLabel1Element.getTextContent();
		
		//NodeList graphLabel2List = contOutcomeElement.getElementsByTagName("GRAPH_LABEL_2");
		//Element graphLabel2Element = (Element) graphLabel2List.item(0);
		//graphLabel2 = graphLabel2Element.getTextContent();
		
		if (contSubgroupElement != null) {//because it is possible that the outcome is stored directly under the outcome node and not in a subgroup
			NodeList contNameOfSubgroupList = contSubgroupElement.getElementsByTagName("NAME");
			Element contNameOfSubgroup = (Element) contNameOfSubgroupList.item(0);//for subgroup name
			subgroupName = contNameOfSubgroup.getTextContent();
		}
		interventionMean = Float.parseFloat(contDataElement.getAttribute("MEAN_1")); //intervention and control mean
		controlMean = Float.parseFloat(contDataElement.getAttribute("MEAN_2"));
		
		interventionSD = Float.parseFloat(contDataElement.getAttribute("SD_1"));	//intervention and control standard deviation
		controlSD = Float.parseFloat(contDataElement.getAttribute("SD_2"));
		
		interventionTotalN = Integer.parseInt(contDataElement.getAttribute("TOTAL_1"));	//intervention and control total nr of partcipants
		controlTotalN = Integer.parseInt(contDataElement.getAttribute("TOTAL_2"));
		comparisonName = comparisonNameElement.getTextContent();//comparison and outcome names to be stored with this outcome, if later the reviewer chooses to re-import only part of the trial the outcome name is important as it describes the numbers best
		outcomeName = contOutcomeNameElement.getTextContent();
		
//		System.out.println(comparisonName);
//		System.out.println(outcomeName );
//		System.out.println(subgroupName);

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
//		System.out.println(graphLabel1);
//		System.out.println(graphLabel2);
		
//		System.out.println(group1Events);
//		System.out.println(group2Events);
//		System.out.println(group1Total);
//		System.out.println(group2Total);
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
	
	public ContinuousOutcome() {
		super();
	}
	
	
@Override
	public String toString() {
		return "ContinuousOutcomeObject [comparisonName=" + comparisonName + ", outcomeName=" + outcomeName
				+ ", interventionGroupName=" + interventionGroupName + ", interventionProse=" + interventionProse
				+ ", controlGroupName=" + controlGroupName + ", controlProse=" + controlProse + ", graphLabel1="
				 + ", graphLabel2="  + ", isGroup1Intervention=" 
				+ ", subgroupName=" + subgroupName + ", interventionMean=" + interventionMean + ", controlMean="
				+ controlMean + ", interventionSD=" + interventionSD + ", controlSD=" + controlSD
				+ ", interventionTotalN=" + interventionTotalN + ", controlTotalN=" + controlTotalN + ", invalidIC="
				+ invalidIC + ", addedInfoCombinationsIC=" + addedInfoCombinationsIC + ", addedInfoOtherAntipsyIC="
				+ addedInfoOtherAntipsyIC + ", splitPattern=" + splitPattern + ", m=" + m + ", splitComparison="
				+ Arrays.toString(splitComparison) + ", splitIC=" + Arrays.toString(splitIC) + "]";
	}

@XmlElement(name = "Comparison")
	public String getComparisonName() {
		return comparisonName;
	}
	//@XmlElement
	public void setComparisonName(String comparisonName) {
		this.comparisonName = comparisonName;
	}
	@XmlElement(name = "Outcome")
	public String getOutcomeName() {
		return outcomeName;
	}
	//@XmlElement
	public void setOutcomeName(String outcomeName) {
		this.outcomeName = outcomeName;
	}
	@XmlElement(name = "Intervention")
	public String getGroup1Name() {
		return interventionGroupName;
	}

	public void setGroup1Name(String group1Name) {
		this.interventionGroupName = group1Name;
	}
	//@XmlElement
	public String getGroup1NameProse() {
		return interventionProse;
	}

	public void setGroup1NameProse(String group1NameProse) {
		this.interventionProse = group1NameProse;
	}
	@XmlElement(name = "Control")
	public String getGroup2Name() {
		return controlGroupName;
	}

	public void setGroup2Name(String group2Name) {
		this.controlGroupName = group2Name;
	}
	//@XmlElement
	public String getGroup2NameProse() {
		return controlProse;
	}

	public void setGroup2NameProse(String group2NameProse) {
		this.controlProse = group2NameProse;
	}

	
	@XmlElement(name = "Subgroup")
	public String getSubgroupName() {
		return subgroupName;
	}

	public void setSubgroupName(String subgroupName) {
		this.subgroupName = subgroupName;
	}
	
	@XmlElement(name = "Intervention_Total")
	public int getGroup1Total() {
		return interventionTotalN;
	}

	public void setGroup1Total(int group1Total) {
		this.interventionTotalN = group1Total;
	}
	@XmlElement(name = "Control_Total")
	public int getGroup2Total() {
		return controlTotalN;
	}

	public void setGroup2Total(int group2Total) {
		this.controlTotalN = group2Total;
	}
	@XmlElement(name = "Intervention_Mean")
	public float getInterventionMean() {
		return interventionMean;
	}

	public void setInterventionMean(float interventionMean) {
		this.interventionMean = interventionMean;
	}
	@XmlElement(name = "Control_Mean")
	public float getControlMean() {
		return controlMean;
	}

	public void setControlMean(float controlMean) {
		this.controlMean = controlMean;
	}
	@XmlElement(name = "Intervention_SD")
	public float getInterventionSD() {
		return interventionSD;
	}

	public void setInterventionSD(float interventionSD) {
		this.interventionSD = interventionSD;
	}
	@XmlElement(name = "Control_SD")
	public float getControlSD() {
		return controlSD;
	}

	public void setControlSD(float controlSD) {
		this.controlSD = controlSD;
	}

	public String getOutcomeType() {
		return outcomeType;
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

	

	public int getInterventionTotalN() {
		return interventionTotalN;
	}

	public void setInterventionTotalN(int interventionTotalN) {
		this.interventionTotalN = interventionTotalN;
	}

	public int getControlTotalN() {
		return controlTotalN;
	}

	public void setControlTotalN(int controlTotalN) {
		this.controlTotalN = controlTotalN;
	}

	public String getReviewTitle() {
		return reviewTitle;
	}

	public void setReviewTitle(String reviewTitle) {
		this.reviewTitle = reviewTitle;
	}
	


}
