package references;

public class CochraneReviewReference extends Reference{
	protected String type = "";
	protected String allAuthors = "";
	protected String title = "";
	protected String originalTitle = "";
	protected String journalBookSource = "";
	protected String date = "";
	protected String issue = "";
	protected boolean primaryReference = false;
	protected String revManID = "";
	protected String reviewTitle = "";
	
	
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
	
	public boolean isPrimaryReference() {
		return primaryReference;
	}

	public void setPrimaryReference(boolean primaryReference) {
		this.primaryReference = primaryReference;
	}

	public CochraneReviewReference(String[] referencesArray, int i, String revManID, String reviewTitle){
		this.reviewTitle = reviewTitle;
		this.revManID = revManID;
		type = "Cochrane Review";
		if (referencesArray[i + 1].equals("YES")) {
			primaryReference = true;
		}
		allAuthors = referencesArray[i + 2];
		title = referencesArray [i + 3];
		date = referencesArray[i + 5];
		originalTitle = referencesArray[i + 9];
		journalBookSource = referencesArray [i + 4];
		issue = referencesArray[i + 7];
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAllAuthors() {
		return allAuthors;
	}

	public void setAllAuthors(String allAuthors) {
		this.allAuthors = allAuthors;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOriginalTitle() {
		return originalTitle;
	}

	public void setOriginalTitle(String originalTitle) {
		this.originalTitle = originalTitle;
	}

	public String getSource() {
		return journalBookSource;
	}

	public void setSource(String source) {
		this.journalBookSource = source;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getVolume() {
		return issue;
	}

	public void setVolume(String volume) {
		this.issue = volume;
	}
	
}
