package trialAndOutcome;

public class ProbablyIntervention extends Interventions{
	protected String revManID;
	protected String reviewTitle;
	protected String value;
	protected String isIntervention = "Probably";

	public ProbablyIntervention() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ProbablyIntervention(String thisItem, String revManID, String reviewTitle) {
		this.reviewTitle = reviewTitle;
		this.revManID = revManID;
		this.value = thisItem;
	}

	public String getRevManID() {
		return revManID;
	}

	public void setRevManID(String revManID) {
		this.revManID = revManID;
	}

	public String getReviewTitle() {
		return reviewTitle;
	}

	public void setReviewTitle(String reviewTitle) {
		this.reviewTitle = reviewTitle;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getIsIntervention() {
		return isIntervention;
	}

	public void setIsIntervention(String isIntervention) {
		this.isIntervention = isIntervention;
	}

}
