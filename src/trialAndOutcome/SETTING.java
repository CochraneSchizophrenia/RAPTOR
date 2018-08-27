package trialAndOutcome;

public enum SETTING {
	
	INPATIENT("Setting: inpatient setting"), OUTPATIENT("Setting: outpatient setting"), INANDOUT("Setting: inpatient and outpatient setting"), EMERGENCY("Setting: emergency room setting"), COMMUNITY("Setting: community setting"), HOSPITAL("Setting: hospital setting"), PSYCHIATRY("Setting: psychiatry setting"), OTHER("Setting: see prose for setting"), NOTAVAILABLE("Setting: setting not extracted");
	
	private final String description;

	private SETTING(String description) {
		this.description = description;
	}
	
	public String getContent() {
        return description;
    }
}
