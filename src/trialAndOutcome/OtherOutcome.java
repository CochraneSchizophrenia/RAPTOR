package trialAndOutcome;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class OtherOutcome extends Outcome{

	@XmlAttribute
	protected final String outcomeType ="Other";
	
	protected String outcomeName = "";
	protected String subgroupName = "";
	protected String reviewTitle = "";
	protected String comparisonName = "";
	protected String revManID = "";
	
	public String getRevManID() {
		return revManID;
	}

	public void setRevManID(String revManID) {
		this.revManID = revManID;
	}

	@XmlElement(name = "Comparison")
	public String getComparisonName() {
		return comparisonName;
	}

	public void setComparisonName(String comparisonName) {
		this.comparisonName = comparisonName;
	}

	public String getReviewTitle() {
		return reviewTitle;
	}

	public void setReviewTitle(String reviewTitle) {
		this.reviewTitle = reviewTitle;
	}

	@XmlElement(name = "Subgroup")
	public String getSubgroupName() {
		return subgroupName;
	}

	public void setSubgroupName(String subgroupName) {
		this.subgroupName = subgroupName;
	}
	
	@XmlElement(name = "Outcome")
	public String getOutcomeName() {
		return outcomeName;
	}



	public void setOutcomeName(String outcomeName) {
		this.outcomeName = outcomeName;
	}


	
	public String getOutcomeType() {
		return outcomeType;
	}



	public OtherOutcome() {
		super();
	}
	
public OtherOutcome(Element oeDataElement, Element comparisonNameElement, Element oeOutcomeNameElement, Element oeOutcomeElement, Element oeSubgroupElement, String review, String revManID) {
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
		
	}




}
