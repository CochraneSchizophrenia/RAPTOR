package references;

public class SoftwareReference extends Reference{
	protected String type = "";
	protected String allAuthors = "";
	protected String title = "";
	protected String date = "";
	protected String originalTitle = "";
	protected String edition = "";
	protected String publisher = "";
	protected String city = "";
	protected String medium = "";
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

	public SoftwareReference(String[] referencesArray, int i, String revManID, String reviewTitle){
		this.reviewTitle = reviewTitle;
		this.revManID = revManID;
		type = "Software";
		if (referencesArray[i + 1].equals("YES")) {
			primaryReference = true;
		}
		allAuthors = referencesArray[i + 2];
		title = referencesArray[i + 3];
		date = referencesArray[i + 5];
		originalTitle = referencesArray[i + 9];
		edition = referencesArray[i + 10];
		publisher= referencesArray[i + 12];
		city= referencesArray[i + 13];
		medium = referencesArray[i + 14];
		
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getOriginalTitle() {
		return originalTitle;
	}

	public void setOriginalTitle(String originalTitle) {
		this.originalTitle = originalTitle;
	}

	public String getEdition() {
		return edition;
	}

	public void setEdition(String edition) {
		this.edition = edition;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getMedium() {
		return medium;
	}

	public void setMedium(String medium) {
		this.medium = medium;
	}

}
