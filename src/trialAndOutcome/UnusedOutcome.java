package trialAndOutcome;

public class UnusedOutcome extends OutcomeCharTable{
	protected String revManID;
	protected String reviewTitle;
	protected String value;//the String that represents the unused outcome
	protected String status = "Unused";//indicated that this outcome was not used in the review

	

	public UnusedOutcome() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UnusedOutcome(String thisItem, String revManID, String reviewTitle) {
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
