package identifiers;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class IsrctnIdentifier extends Identifier {

	protected String type = "";
	protected String link = "";
	protected String value = "";
	protected String review = "";


	public IsrctnIdentifier(Element identifierElement, String link, String review) {
		type = "ISRCTN";
		this.link = link;
		this.review = review;
		try {
			value = identifierElement.getAttribute("VALUE");
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	
	}
	
	
	
public IsrctnIdentifier() {
		
	}

public String getReview() {
	return review;
}



public void setReview(String review) {
	this.review = review;
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






}
