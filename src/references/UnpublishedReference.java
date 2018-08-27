package references;

public class UnpublishedReference extends Reference{

	//if the information in the references section points to an unpublished reference
		protected String type = "";
		protected String allAuthors = "";
		protected String title = "";
		protected String journalBookSource = "";
		protected String date = "";
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

		public boolean isPrimaryReference() {
			return primaryReference;
		}

		public void setPrimaryReference(boolean primaryReference) {
			this.primaryReference = primaryReference;
		}

		public UnpublishedReference(String[] referencesArray, int i, String revManID, String reviewTitle){
			this.reviewTitle = reviewTitle;
			this.revManID = revManID;
			type = "Unpublished Reference";

			if (referencesArray[i + 1].equals("YES")) {
				primaryReference = true;
			}
			allAuthors = referencesArray[i + 2];
			title = referencesArray[i + 3];
			journalBookSource = referencesArray [i + 4];
			date = referencesArray[i + 5];
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

		public String getOriginalTitle() {
			return originalTitle;
		}

		public void setOriginalTitle(String originalTitle) {
			this.originalTitle = originalTitle;
		}
		
}
