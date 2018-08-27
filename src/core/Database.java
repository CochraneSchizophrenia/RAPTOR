package core;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

import trialAndOutcome.Trial;
@XmlRootElement
public class Database {
	
	public Database() {
		super();
	}

	List<Trial> trialList = new ArrayList<>();
	protected String reviewName = "";
	protected String path = ""; // for saving extraction file
	
	@XmlElement(name = "TRIAL")
	public List<Trial> getTrialList() {
		return trialList;
	}
	public void setTrialList(List<Trial> trialList) {
		this.trialList = trialList;
	}
	
	public void makeList() throws ParserConfigurationException{
		
		
		ParseReview parser = new ParseReview();
		
		Document[] reviewArray = parser.chooseAndParse(); //contains reviews specified by user via pop up browsing window
		path = parser.path;
		
		for (int i = 0; i < reviewArray.length; i++) {
			int numberOfStudies = parser.numberOfIncludedStudies(reviewArray[i]);//looks at how many trials there are to extract
			Trial trialObj = new Trial(); //makes TrialObject with default empty constructor. If there are trials included in this review, the proper constructer is called in the for-loop.
			for (int j = 0; j < numberOfStudies; j++) {

				trialObj = new Trial(reviewArray[i], j);//extracts all data of the specified trial and dumps it into the trial list
				trialList.add(trialObj);
				//writeBack
				//writeBack.cleanReview(a);
				//System.out.println("Trial " + a.aauthorYearLetter + " added!");

			}
			reviewName = trialObj.getReviewTitle();
			//System.out.println(reviewName);
		}
		
	}
	

}
