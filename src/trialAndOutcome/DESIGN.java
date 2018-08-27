package trialAndOutcome;

public enum DESIGN {
	
	PARALLEL("Design: parallel."), CROSSOVER("Design: crossover."), FACTORIAL("Design: factorial."), PRAGMATIC("Design: pragmatic."), 
	DOSAGE("Design: dosage study."), LONGITUDINAL("Design: longitudinal study."), CLUSTER("Design: cluster randomised trial."), 
	OTHER("Design: see prose for trial design."), NOTAVAILABLE("Design: trial design not extracted."),
	UNCLEAR("Design: unclear."), NOTREPORTED("Design: not reported.");
	
	private final String description;
	
	public String getContent() {
        return description;
    }

	private DESIGN(String description) {
		this.description = description;
	}
	
	

}
