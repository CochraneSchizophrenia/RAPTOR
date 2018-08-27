package references;

public class JournalReference extends Reference{
	
	//0th, 9th ..index -> Type of publication, eg. Journal ->"TYPE"
	//1st..., 10th..index -> Primary attribute: Yes or No ->"PRIMARY"
	//2nd... ->Names of all authors of this publication ->"AU"
	//3rd...->Title of publication->"TI"
	//4th...->Name of Journal->Journal->"SO"
	//5...-> Year published->"YR"
	//6...->Volume ->"VL" 
	//7...-> Issue ->"NO"
	//8...-> Pages ->"PG"
	//empty fields will just stay empty
	
	protected String type = "";
	protected String allAuthors = "";
	protected String title = "";
	protected String journalBookSource = "";
	protected String date = "";
	protected String volume = "";
	protected String issue = "";
	protected String pages = "";
	protected String originalTitle = "";
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
	
	

	public JournalReference(String[] referencesArray, int i, String revManID, String reviewTitle){
		this.reviewTitle = reviewTitle;
		this.revManID = revManID;
		if (referencesArray[i + 1].equals("YES")) {
			primaryReference = true;
		}
		
		if (primaryReference == true) {
			//regex author extraction
		}
		
		type = "Journal article";
		allAuthors = referencesArray[i + 2];
		title = referencesArray[i + 3];
		journalBookSource = referencesArray [i + 4];
		date = referencesArray[i + 5];
		volume = referencesArray[i + 6];
		issue = referencesArray[i + 7];
		pages = referencesArray[i + 8];
		originalTitle = referencesArray[i + 9];
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

	public String getJournalName() {
		return journalBookSource;
	}

	public void setJournalName(String journalName) {
		this.journalBookSource = journalName;
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

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public String getPages() {
		return pages;
	}

	public void setPages(String pages) {
		this.pages = pages;
	}

	public String getOriginalTitle() {
		return originalTitle;
	}

	public void setOriginalTitle(String originalTitle) {
		this.originalTitle = originalTitle;
	}
	public boolean isPrimaryReference() {
		return primaryReference;
	}

	public void setPrimaryReference(boolean primaryReference) {
		this.primaryReference = primaryReference;
	}
}
