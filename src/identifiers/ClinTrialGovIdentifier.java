package identifiers;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ClinTrialGovIdentifier extends Identifier {

	protected String type = "";
	
	protected String value = "";
	
	protected String link = "";

	protected String review = "";

	



	public ClinTrialGovIdentifier(Element identifierElement, String link, String review) {
		this.link = link;
		this.review = review;
		
		type = "ClinicalTrials.gov";
		
		try {
			value = identifierElement.getAttribute("VALUE");
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	
	}
	
	
	
	
public ClinTrialGovIdentifier() {
		
	}

public String getLink() {
	return link;
}




public void setLink(String link) {
	this.link = link;
}




public String getType() {
	return type;
}




public void setType(String type) {
	this.type = type;
}


public String getValue() {
	return value;
}




public void setValue(String value) {
	this.value = value;
}




public String getReview() {
	return review;
}




public void setReview(String review) {
	this.review = review;
}

}
