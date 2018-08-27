package trialAndOutcome;

public enum ALLOCATION {
	
	RANDOM("Allocation: randomised."), QUASI("Allocation: randomised, quasi."), UNCLEAR("Allocation: unclear.");
	
	private final String description;
	
	public String getContent() {
        return description;
    }
	
	private ALLOCATION(String description) {
		this.description = description;
	}
	
	



}
