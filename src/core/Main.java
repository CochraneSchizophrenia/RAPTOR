package core;
import java.io.File;

import javax.swing.JOptionPane;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import trialAndOutcome.Trial;


//test123

public class Main {
	

	
	public static void main(String[] args)  throws Exception{
		JOptionPane.showMessageDialog(null, "Instructions:\nPlease select one or more ReviewManager5 (.rm5) files in the next step. \n\nThe new file, called \"RAPTOR.xml\" will be saved there.\nYour original review file remains untouched.\nIf this folder already contains an XML created by RAPTOR, it will be overwritten.");
		
		
		Database d = new Database();
		d.makeList();//in Database class a list of trials in this review will be created
		
		if (d.reviewName == "") {//if reviewName is empty it indicates that only the default constructor for trial objects was called in the database class. Therefore, there are no trials included in the review that was analysed.
			System.out.println("No trials included");
		} else {
			try {
				JAXBContext jaxbContext = JAXBContext.newInstance(
						//this makes it possible to marshal instances of these classes to XML
						Database.class,
						
						references.JournalReference.class,
						references.BookReference.class,
						references.BookSectionReference.class,
						references.OtherReference.class,
						references.ConferenceReference.class,
						references.CorrespondenceReference.class,
						references.CochraneProtocolReference.class,
						references.CochraneReviewReference.class,
						references.SoftwareReference.class,
						references.UnpublishedReference.class,
						
						trialAndOutcome.ContinuousOutcome.class,
						trialAndOutcome.DichotomousOutcome.class,
						trialAndOutcome.GenericInverseOutcome.class,
						trialAndOutcome.OEandVarianceOutcome.class,
						trialAndOutcome.OtherOutcome.class,
						trialAndOutcome.Outcome.class,
						trialAndOutcome.Interventions.class,
						trialAndOutcome.ProbablyIntervention.class,
						trialAndOutcome.UnlikelyIntervention.class,
						trialAndOutcome.UnclearIntervention.class,
						trialAndOutcome.OutcomeCharTable.class,
						trialAndOutcome.UnusedOutcome.class,
						trialAndOutcome.UsedOutcome.class,
						
						identifiers.OtherIdentifier.class,
						identifiers.ClinTrialGovIdentifier.class,
						identifiers.DoiIdentifier.class,
						identifiers.IsrctnIdentifier.class,
						identifiers.Identifier.class, 
						
						identifiers.MedlineIdentifier.class,
						identifiers.PubMedIdentifier.class,
						identifiers.CentralIdentifier.class,
						identifiers.EmbaseIdentifier.class
						
						
						
						
						
						
						
						);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				
				
				
				String path = d.path + "\\RAPTOR.xml";
				jaxbMarshaller.marshal(d, new File(path));	//puts database into a xml file that is saved according to path String
				
				System.out.println(d.path);
				System.out.println("File created successfully");
				System.out.println(Trial.counter);
				JOptionPane.showMessageDialog(null, "Extraction finished successfully");
				
				
			} catch (JAXBException e) {
				JOptionPane.showMessageDialog(null, "Something went wrong. Please contact Clive.Adams@nottingham.ac.uk or lena.schmidt.0493@gmail.com");
				e.printStackTrace();
			}
		}
		
	
		

	}
	
	
	
	
}

