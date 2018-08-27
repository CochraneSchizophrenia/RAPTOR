package references;

public class BookReference extends Reference{
	protected String type = "";
	protected String allAuthors = "";
	protected String journalBookSource = "";
	protected String date = "";
	protected String volume = "";
	protected String edition = "";
	protected String publisher = "";
	protected String city = "";
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

	public BookReference(String[] referencesArray, int i, String revManID, String reviewTitle){
		this.reviewTitle = reviewTitle;
		this.revManID = revManID;
		type = "Book";
		if (referencesArray[i + 1].equals("YES")) {
			primaryReference = true;
		}
		allAuthors = referencesArray[i + 2];
		journalBookSource = referencesArray [i + 4];
		date = referencesArray[i + 5];
		volume = referencesArray[i + 6];
		edition = referencesArray[i + 10];
		publisher= referencesArray[i + 12];
		city= referencesArray[i + 13];
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
		return journalBookSource;
	}

	public void setTitle(String title) {
		this.journalBookSource = title;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
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

}
