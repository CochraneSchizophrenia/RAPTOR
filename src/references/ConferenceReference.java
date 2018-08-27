package references;

public class ConferenceReference extends Reference{
	//if the information in the references section points to conference proceedings
	protected String type = "";
	protected String allAuthors = "";
	protected String title = "";
	protected String journalBookSource = "";
	protected String date = "";
	protected String originalTitle = "";
	protected String volume = "";
	protected String pages = "";
	protected String edition = "";
	protected String editor = "";
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




	public ConferenceReference(String[] referencesArray, int i, String revManID, String reviewTitle){
		this.reviewTitle = reviewTitle;
		this.revManID = revManID;
		
		type = "Conference Proceedings";
		if (referencesArray[i + 1].equals("YES")) {
			primaryReference = true;
		}
		
		allAuthors = referencesArray[i + 2];
		title = referencesArray[i + 3];
		journalBookSource = referencesArray [i + 4];
		date = referencesArray[i + 5];
		originalTitle = referencesArray[i + 9];
		volume = referencesArray[i + 6];
		pages = referencesArray[i + 8];
		edition = referencesArray[i + 10];
		editor= referencesArray[i + 11];
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
		return title;
	}




	public void setTitle(String title) {
		this.title = title;
	}




	public String getConferenceName() {
		return journalBookSource;
	}




	public void setConferenceName(String conferenceName) {
		this.journalBookSource = conferenceName;
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




	public String getVolume() {
		return volume;
	}




	public void setVolume(String volume) {
		this.volume = volume;
	}




	public String getPages() {
		return pages;
	}




	public void setPages(String pages) {
		this.pages = pages;
	}




	public String getEdition() {
		return edition;
	}




	public void setEdition(String edition) {
		this.edition = edition;
	}




	public String getEditor() {
		return editor;
	}




	public void setEditor(String editor) {
		this.editor = editor;
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
