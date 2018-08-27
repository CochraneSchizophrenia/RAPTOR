package trialAndOutcome;

public enum BLINDING {
	
	ASSESSOR("Blinding: assessor."), SINGLE("Blinding: single."), DOUBLE("Blinding: double."), TRIPLE("Blinding: triple."), QUADRUPLE("Blinding: quadruple."), 
	OPENTRIAL("Blinding: open."), NOTREPORTED("Blinding: not reported."), OTHER("Please see prose for more info."), 
	NOTAVAILABLE("Blinding: information on blindness was not extracted."),
	BIASEDASSESSOR("Blinding: assessor."), BIASEDSINGLE("Blinding: single."), BIASEDDOUBLE("Blinding: double."), BIASEDTRIPLE("Blinding: triple."), 
	BIASEDQUADRUPLE("Blinding: quadruple."), BIASEDOPEN("Blinding: open."), UNCLEAR("Blinding: unclear.");
	
	private final String description;
	
	public String getDescription() {
		return description;
	}

	private BLINDING(String description) {
		this.description = description;
	}
	
	public String getContent() {
        return description;
    }

}

