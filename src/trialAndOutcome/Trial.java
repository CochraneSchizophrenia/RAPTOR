package trialAndOutcome;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import references.BookSectionReference;

import references.BookReference;
import references.CochraneProtocolReference;
import references.CochraneReviewReference;
import references.ConferenceReference;
import references.CorrespondenceReference;
import references.JournalReference;
import references.OtherReference;
import references.Reference;
import references.SoftwareReference;
import references.UnpublishedReference;

import identifiers.Identifier;
import identifiers.IsrctnIdentifier;
import identifiers.MedlineIdentifier;
import identifiers.OtherIdentifier;
import identifiers.PubMedIdentifier;
import identifiers.CentralIdentifier;
import identifiers.ClinTrialGovIdentifier;
import identifiers.DoiIdentifier;
import identifiers.EmbaseIdentifier;


public class Trial{
	///////////////////////////////////////////////////empty constructor for xml creation only
	public Trial() {
		super();
	}
//comment
//////////////////////////////////////////////////////////////////attributes. the protected ones have getters and setters because they will end up in the finished XML
	public static int counter = 0;
	protected String mainAuthor = ""; //check
	protected String year;//will contain year of publication
	protected String firstPublicationYear = "";
	protected String authorYearLetter = ""; //for comparison with MeerKatBE
	protected String extractedAuthor = "";
	
	
	protected String reviewTitle = "";
	

	protected String doi = "";
	
	protected String revManID;//check
	protected String[] references; //to contain all references to this trial
	
	protected List<Outcome> outcomeList = new ArrayList<>();///array list that will contain all outcomes and their data
	
	
	private DichotomousOutcome dobj;//object that contains data of one outcome. It will be immediately dumped in the outcome list and re-filled with the next outcome
	private ContinuousOutcome cobj;//object that contains data of one outcome. It will be immediately dumped in the outcome list and re-filled with the next outcome
	private OEandVarianceOutcome oeobj;//same description as above
	private GenericInverseOutcome givobj;//same as above
	private OtherOutcome oobj;//same as above
	
	protected Characteristics charObject;//this object contains extracted prose info from the characteristics of included studies table of this trial
	
	private Reference refObject;/// for one reference, can be of different types. Procedure similar to outcomeObject. 
	protected List<Reference> referenceList = new ArrayList<>(); ///////List that will contain all referenceObjects

	private Identifier trialIdObject; //////////holds values of identifiers for this trial -> trial level, not single reference level
	protected List<Identifier> trialIdList = new ArrayList<>();//identifier objects get stored in here
	
	private Identifier referenceIdObj;
	protected List<Identifier> referenceIdList = new ArrayList<>();
	
	

	/////////////////////////////////////////////////////////////xml related attributes that are used between different methods in the big constructor
	private Element qualityItemsElement;
	Element studyElement;
	private NodeList qualityItemList;
	private NodeList referencesList;
	
	protected Element characteristicsOfIncludedStudiesElement;
	
	public Trial(Document review, int studyNumber){//////////the constructor that fills all attributes
		////study number tells, which study has to be extracted
					
					//Creates rootElement
					
					Element rootElement = null;
					try {
						NodeList rootList = review.getElementsByTagName("COCHRANE_REVIEW");
						Node rootNode = rootList.item(0);
						rootElement = (Element) rootNode;
						doi = rootElement.getAttribute("DOI");
						
					} catch (Exception e11) {
						e11.printStackTrace();
					}
					
					try {
						NodeList coverSheetList = rootElement.getElementsByTagName("COVER_SHEET");
						Element coverSheetElement = (Element) coverSheetList.item(0);
						
						NodeList titleList = coverSheetElement.getElementsByTagName("TITLE");
						Element titleElement = (Element) titleList.item(0);
						reviewTitle = titleElement.getTextContent().trim();
						reviewTitle = reviewTitle.replace("\\", " ").replaceAll("/", " ");//in case review title is used as filename the slasheds need to go.
						//System.out.println(reviewTitle);
					} catch (DOMException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					
					
					//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					//Traverses the characteristics of included studies part of the revman file. This is where bias tables and prose information are located
					Element studyToExtractElement = null;
					Element characteristicsOfStudiesElement = null;
					try {
						NodeList characteristicsOfStudiesList = rootElement.getElementsByTagName("CHARACTERISTICS_OF_STUDIES");
						Node characteristicsOfStudiesNode = characteristicsOfStudiesList.item(0);
						characteristicsOfStudiesElement = (Element) characteristicsOfStudiesNode;
						
						NodeList characteristicsOfIncludedStudiesList = characteristicsOfStudiesElement.getElementsByTagName("CHARACTERISTICS_OF_INCLUDED_STUDIES");
						Node characteristicsOfIncludedStudiesNode = characteristicsOfIncludedStudiesList.item(0);
						characteristicsOfIncludedStudiesElement = (Element) characteristicsOfIncludedStudiesNode;
						
						NodeList includedStudiesList = characteristicsOfIncludedStudiesElement.getElementsByTagName("INCLUDED_CHAR");
						Node studyToExtractNode = includedStudiesList.item(studyNumber);
						studyToExtractElement = (Element) studyToExtractNode;
					} catch (Exception e10) {
						e10.printStackTrace();
					}
					
					//Extracts information into variables
					//Gets revman ID for study
					revManID = studyToExtractElement.getAttribute("STUDY_ID"); 
					
					String cache = revManID.replaceAll("STD-", "").replaceAll("(_x002d_)", "-").replaceAll("(_x0026_)", "&");// replaces space with hyphen and at first puts & back 
					String[] cacheArray = cache.split("-\\d+");	//Splits at "-"+ digit
					mainAuthor = cacheArray[0]; //Take name of author from ID
					
					String yearLetter;
					try {
						yearLetter = cacheArray[1];
					} catch (Exception e) {
						// if this trialname/year is unique in the review
						yearLetter = "";
					}
					
					
				
					
					
					
					
					
					////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					//Traverses the revman file's Studies and references section
					
					try {
						NodeList studiesAndReferencesList = rootElement.getElementsByTagName("STUDIES_AND_REFERENCES");
						Node studiesAndReferencesNode = studiesAndReferencesList.item(0);
						Element studiesAndReferencesElement = (Element) studiesAndReferencesNode;
						
						NodeList studiesList = studiesAndReferencesElement.getElementsByTagName("STUDIES");
						Node studiesNode = studiesList.item(0);
						Element studiesElement = (Element) studiesNode;
						
						
						NodeList includedStudiesBList = studiesElement.getElementsByTagName("INCLUDED_STUDIES");
						Node includedStudiesNode = includedStudiesBList.item(0);
						Element includedStudiesElement = (Element) includedStudiesNode;
						
						
						NodeList studyList = includedStudiesElement.getElementsByTagName("STUDY");
						Node studyNode = studyList.item(studyNumber);
						studyElement = (Element) studyNode;
					} catch (Exception e9) {
						e9.printStackTrace();
					}
					
					NodeList qualityItemsList = rootElement.getElementsByTagName("QUALITY_ITEMS");
					Node qualityItemsNode = qualityItemsList.item(0);
					qualityItemsElement = (Element) qualityItemsNode;
					
					qualityItemList = qualityItemsElement.getElementsByTagName("QUALITY_ITEM");
	
					//////////////extracts characteristics and prose tables
					
					

					/////////////////////////////////////////////////////////////////////////////////////////////////
					////////////////////////////////////////////
					
					
					referenceExtracting(); //Extracts all information on references of this trial. See method referenceExtracting() below for more details. Puts info into array of strings that will be further analysed below
					
					for (int i = 0; i < references.length; i++){
						if (references[i].equals("JOURNAL_ARTICLE")){
							refObject = new JournalReference(references, i, revManID, reviewTitle);
							referenceList.add(refObject);
							i = i + 14; //its plus 14 because +1 is added at the end of the loop and 
							//this array contains new info to check on every 15th index
						} else if (references[i].equals("CONFERENCE_PROC")){
							refObject = new ConferenceReference(references, i, revManID, reviewTitle);
							referenceList.add(refObject);
							i = i + 14;
						} else if (references[i].equals("UNPUBLISHED")){
							refObject = new UnpublishedReference(references, i, revManID, reviewTitle);
							referenceList.add(refObject);
							//System.out.println(refObject.getClass());
							i = i + 14;
						} else if (references[i].equals("OTHER")){
							refObject = new OtherReference(references, i, revManID, reviewTitle);
							referenceList.add(refObject);
							//System.out.println(refObject.getClass());
							i = i + 14;
						} else if (references[i].equals("BOOK_SECTION")){
							refObject = new BookSectionReference(references, i, revManID, reviewTitle);
							referenceList.add(refObject);
							//System.out.println(refObject.getClass());
							i = i + 14;
						} else if (references[i].equals("CORRESPONDENCE")){
							refObject = new CorrespondenceReference(references, i, revManID, reviewTitle);
							referenceList.add(refObject);
							//System.out.println(refObject.getClass());
							i = i + 14;
						} else if (references[i].equals("BOOK")){
							refObject = new BookReference(references, i, revManID, reviewTitle);
							referenceList.add(refObject);
							//System.out.println(refObject.getClass());
							i = i + 14;
						} else if (references[i].equals("COCHRANE_REVIEW")){
							refObject = new CochraneReviewReference(references, i, revManID, reviewTitle);
							referenceList.add(refObject);
							//System.out.println(refObject.getClass());
							i = i + 14;
						} else if (references[i].equals("COCHRANE_PROTOCOL")){
							refObject = new CochraneProtocolReference(references, i, revManID, reviewTitle);
							referenceList.add(refObject);
							//System.out.println(refObject.getClass());
							i = i + 14;
						}else if (references[i].equals("COMPUTER_PROGRAM")){
							refObject = new SoftwareReference(references, i, revManID, reviewTitle);
							referenceList.add(refObject);
							//System.out.println(refObject.getClass());
							i = i + 14;
						}
					}
					
					//tries to extract year when study was conducted
					try {
						year = studyElement.getAttribute("YEAR");
						
						int lowestYear = 2147483647;//highest int possible
							for (Reference ref : referenceList) {
								try {
									int thisYear = Integer.parseInt(ref.getDate());
									if (thisYear < lowestYear) {
										lowestYear = thisYear;//only uses the new year value if it is lower than the previously lowest year
									}
									} catch (Exception e) {//year could not be parsed, so the next reference is tried.
								}
							}
							if (lowestYear != 2147483647) {//if no parsable year was found, this info is discarded and year stays empty
								if (year.equals("")) {//uses the lowest year value if year is empty
									year = Integer.toString(lowestYear);// a lower year was found
									System.out.println(reviewTitle + ", " + revManID + ": new year is " + year);
								} 
								firstPublicationYear = Integer.toString(lowestYear);
								}
					} catch (NumberFormatException e8) {
						year = "";
					}
					
					charObject = new Characteristics(studyToExtractElement, qualityItemList, revManID, reviewTitle, year, firstPublicationYear);
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					//Extracts OutcomeObjects for this trial
					
						//System.out.println("Main author of Study: " + mainAuthor);
						//System.out.println(trialSetting.getContent());
						//System.out.println(countries);
						//System.out.println(blindingMethod.getContent());
						//System.out.println(countries);
					
					//treverses xml to find data section
					NodeList analysesAndDataList = rootElement.getElementsByTagName("ANALYSES_AND_DATA");
					Node analysesAndDataNode = analysesAndDataList.item(0);
					Element analysesAndDataElement = (Element) analysesAndDataNode;
					
					NodeList comparisonList = analysesAndDataElement.getElementsByTagName("COMPARISON");
					for (int i = 0; i < comparisonList.getLength(); i++){
						Node comparisonNode = comparisonList.item(i);
						Element comparisonElement = (Element) comparisonNode;
						
						NodeList comparisonNameList = comparisonElement.getElementsByTagName("NAME");
						Node comparisonNameNode = comparisonNameList.item(0);
						Element comparisonNameElement = (Element) comparisonNameNode; //takeOver for comparisonname and groupnames
						//for readable revManID
						String revManIdParameter = revManID.replaceAll("_x00df_", "ß").replaceAll("(_x002d_)", "-").replaceAll("(_x0026_)", "&").replaceAll("_x00e8_", "è").replaceAll("_x00f6_", "ö").replaceAll("_x00fc_", "ü").replaceAll("_x002b_", "+").replaceAll("_x002f_", "/").replaceAll("_x00a0_", " ").replaceAll("_x002c_", ",").replaceAll("_x0028_", "(").replaceAll("_x0029_", ")").replaceAll("_x00e7_", "ç").replaceAll("_x0027_", "'").replaceAll("_x002a_", "*").replaceAll("_x00e9_", "é").replaceAll("_x00e4_", "ä").replaceAll("_x00b4_", "´");
						
						try {////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
							//looks for dichotomous outcomes by their tag name. Elements are created and used as parameters for object creation. All
							//dichotomous outcome objects are traversed and it is checked if their ID equals the revman ID of the trial which outcomes 
							//are supposed to extracted in this call. All outcomes are added to the object list
							NodeList dichOutcomeList = comparisonElement.getElementsByTagName("DICH_OUTCOME");
							for (int l = 0; l < dichOutcomeList.getLength(); l++){
								Node dichOutcomeNode = dichOutcomeList.item(l);
								Element dichOutcomeElement = (Element) dichOutcomeNode;
								
								NodeList dichOutcomeNameList = dichOutcomeElement.getElementsByTagName("NAME");
								Node dichOutcomeNameNode = dichOutcomeNameList.item(0);
								Element dichOutcomeNameElement = (Element) dichOutcomeNameNode;
								
								NodeList dichSubgroupList = dichOutcomeElement.getElementsByTagName("DICH_SUBGROUP");
								
								if (dichSubgroupList.getLength() == 0) {//there are no subgroups, so the data have to be taken from outcome node directly
									
									Element dichSubgroupElement = null; //has to be given to constructor
									
									NodeList dichDataList = dichOutcomeElement.getElementsByTagName("DICH_DATA");
									for (int k = 0; k < dichDataList.getLength(); k++){
										Node dichDataNode = dichDataList.item(k);
										Element dichDataElement = (Element) dichDataNode;
										
										if (dichDataElement.getAttribute("STUDY_ID").equals(revManID)){
											
											dobj = new DichotomousOutcome(dichDataElement, comparisonNameElement, dichOutcomeNameElement, dichOutcomeElement, dichSubgroupElement, reviewTitle, revManIdParameter);
											outcomeList.add(dobj);
											//System.out.println("Outcome added to list");
										}
									}
								} else {
									for (int j = 0; j <dichSubgroupList.getLength(); j++){ //looks through each subgroup
										Node dichSubgroupNode = dichSubgroupList.item(j); //makes node and element for the specified subgroup
										Element dichSubgroupElement = (Element) dichSubgroupNode;
										
										NodeList dichDataList = dichSubgroupElement.getElementsByTagName("DICH_DATA");
										for (int k = 0; k < dichDataList.getLength(); k++){//looka through each study in this subgroup
											Node dichDataNode = dichDataList.item(k);//makes nodes and elements for specified studies
											Element dichDataElement = (Element) dichDataNode;
											
											if (dichDataElement.getAttribute("STUDY_ID").equals(revManID)){ //looks if this specific study matches
												//the RevManID of the study we want to extract data of
												dobj = new DichotomousOutcome(dichDataElement, comparisonNameElement, dichOutcomeNameElement, 
														dichOutcomeElement, dichSubgroupElement, reviewTitle, revManIdParameter);//if it matched, an object is created
												outcomeList.add(dobj);//object is added to list
											}
										}
									}
								}
								
								
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
						//////continuous outcome objects
						try {
							NodeList contOutcomeList = comparisonElement.getElementsByTagName("CONT_OUTCOME");
							for (int l = 0; l < contOutcomeList.getLength(); l++){
								Node contOutcomeNode = contOutcomeList.item(l);
								Element contOutcomeElement = (Element) contOutcomeNode;
								
								NodeList contOutcomeNameList = contOutcomeElement.getElementsByTagName("NAME");
								Node contOutcomeNameNode = contOutcomeNameList.item(0);
								Element contOutcomeNameElement = (Element) contOutcomeNameNode;
								
								
								NodeList contSubgroupList = contOutcomeElement.getElementsByTagName("CONT_SUBGROUP");
								
								if (contSubgroupList.getLength() == 0) {//there are no subgroups, so the data have to be taken from outcome node directly
									
									Element contSubgroupElement = null;//it does not exist, but is given to the constructor anyway
									
									NodeList contDataList = contOutcomeElement.getElementsByTagName("CONT_DATA");
									for (int k = 0; k < contDataList.getLength(); k++){
										Node contDataNode = contDataList.item(k);
										Element contDataElement = (Element) contDataNode;
										
										if (contDataElement.getAttribute("STUDY_ID").equals(revManID)){
											
											cobj = new ContinuousOutcome(contDataElement, comparisonNameElement, contOutcomeNameElement, contOutcomeElement, contSubgroupElement, reviewTitle, revManIdParameter);
											outcomeList.add(cobj);
											//System.out.println("Outcome added to list");
										}
									}
								} else {
									for (int j = 0; j <contSubgroupList.getLength(); j++){
										Node contSubgroupNode = contSubgroupList.item(j);
										Element contSubgroupElement = (Element) contSubgroupNode;
										
										NodeList contDataList = contSubgroupElement.getElementsByTagName("CONT_DATA");
										for (int k = 0; k < contDataList.getLength(); k++){
											Node contDataNode = contDataList.item(k);
											Element contDataElement = (Element) contDataNode;
											
											if (contDataElement.getAttribute("STUDY_ID").equals(revManID)){
												
												cobj = new ContinuousOutcome(contDataElement, comparisonNameElement, contOutcomeNameElement, contOutcomeElement, contSubgroupElement, reviewTitle, revManIdParameter);
												outcomeList.add(cobj);
												//System.out.println("Outcome added to list");
											}
										}
									}
								}
								
								
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
						////other outcome object
						
						try {
							NodeList oOutcomeList = comparisonElement.getElementsByTagName("OTHER_OUTCOME");
							for (int l = 0; l < oOutcomeList.getLength(); l++){
								Node oOutcomeNode = oOutcomeList.item(l);
								Element oOutcomeElement = (Element) oOutcomeNode;
								
								NodeList oOutcomeNameList = oOutcomeElement.getElementsByTagName("NAME");
								Node oOutcomeNameNode = oOutcomeNameList.item(0);
								Element oOutcomeNameElement = (Element) oOutcomeNameNode;
								
								// data do not always appear in subgroups
								
									NodeList oSubgroupList = oOutcomeElement.getElementsByTagName("OTHER_SUBGROUP");
									
									
									if (oSubgroupList.getLength() == 0) {//there are no subgroups
										
										NodeList oDataList = oOutcomeElement.getElementsByTagName("OTHER_DATA");
										for (int k = 0; k < oDataList.getLength(); k++){
											Node oDataNode = oDataList.item(k);
											Element oDataElement = (Element) oDataNode;
											
											if (oDataElement.getAttribute("STUDY_ID").equals(revManID)){
												
												Element oSubgroupElement = null;
												
												oobj = new OtherOutcome(oDataElement, comparisonNameElement, oOutcomeNameElement, oOutcomeElement, oSubgroupElement, reviewTitle, revManIdParameter);
												outcomeList.add(oobj);
												counter++;
												
											}
										}
										
									} else {//there are subgroups
										for (int j = 0; j <oSubgroupList.getLength(); j++){
											Node oSubgroupNode = oSubgroupList.item(j);
											Element oSubgroupElement = (Element) oSubgroupNode;
											
											NodeList oDataList = oSubgroupElement.getElementsByTagName("IV_DATA");
											for (int k = 0; k < oDataList.getLength(); k++){
												Node oDataNode = oDataList.item(k);
												Element oDataElement = (Element) oDataNode;
												
												if (oDataElement.getAttribute("STUDY_ID").equals(revManID)){
													
													oobj = new OtherOutcome(oDataElement, comparisonNameElement, oOutcomeNameElement, oOutcomeElement, oSubgroupElement, reviewTitle, revManIdParameter);
													outcomeList.add(oobj);
													counter++;
													
												}
											}
										}
									}
										
									
								
								
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
						//generic inverse outcome object
						
						try {
							NodeList ivOutcomeList = comparisonElement.getElementsByTagName("IV_OUTCOME");
							for (int l = 0; l < ivOutcomeList.getLength(); l++){
								Node ivOutcomeNode = ivOutcomeList.item(l);
								Element ivOutcomeElement = (Element) ivOutcomeNode;
								
								NodeList ivOutcomeNameList = ivOutcomeElement.getElementsByTagName("NAME");
								Node ivOutcomeNameNode = ivOutcomeNameList.item(0);
								Element ivOutcomeNameElement = (Element) ivOutcomeNameNode;
								
								// data do not always appear in subgroups
								
									NodeList ivSubgroupList = ivOutcomeElement.getElementsByTagName("IV_SUBGROUP");
									
									
									if (ivSubgroupList.getLength() == 0) {//there are no subgroups
										
										NodeList ivDataList = ivOutcomeElement.getElementsByTagName("IV_DATA");
										for (int k = 0; k < ivDataList.getLength(); k++){
											Node ivDataNode = ivDataList.item(k);
											Element ivDataElement = (Element) ivDataNode;
											
											if (ivDataElement.getAttribute("STUDY_ID").equals(revManID)){
												
												Element ivSubgroupElement = null;
												
												givobj = new GenericInverseOutcome(ivDataElement, comparisonNameElement, ivOutcomeNameElement, ivOutcomeElement, ivSubgroupElement, reviewTitle, revManIdParameter);
												outcomeList.add(givobj);
												
												
											}
										}
										
									} else {//there are subgroups
										for (int j = 0; j <ivSubgroupList.getLength(); j++){
											Node ivSubgroupNode = ivSubgroupList.item(j);
											Element ivSubgroupElement = (Element) ivSubgroupNode;
											
											NodeList ivDataList = ivSubgroupElement.getElementsByTagName("IV_DATA");
											for (int k = 0; k < ivDataList.getLength(); k++){
												Node ivDataNode = ivDataList.item(k);
												Element ivDataElement = (Element) ivDataNode;
												
												if (ivDataElement.getAttribute("STUDY_ID").equals(revManID)){
													
													givobj = new GenericInverseOutcome(ivDataElement, comparisonNameElement, ivOutcomeNameElement, ivOutcomeElement, ivSubgroupElement, reviewTitle, revManIdParameter);
													outcomeList.add(givobj);
													
													
												}
											}
										}
									}
										
									
								
								
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
						///O-E and variance outcome
						try {
							NodeList oeOutcomeList = comparisonElement.getElementsByTagName("IPD_OUTCOME");
							for (int l = 0; l < oeOutcomeList.getLength(); l++){
								Node oeOutcomeNode = oeOutcomeList.item(l);
								Element oeOutcomeElement = (Element) oeOutcomeNode;
								
								NodeList oeOutcomeNameList = oeOutcomeElement.getElementsByTagName("NAME");
								Node oeOutcomeNameNode = oeOutcomeNameList.item(0);
								Element oeOutcomeNameElement = (Element) oeOutcomeNameNode;
								
								// data do not always appear in subgroups
								
									NodeList oeSubgroupList = oeOutcomeElement.getElementsByTagName("IPD_SUBGROUP");
									
									
									if (oeSubgroupList.getLength() == 0) {//there are no subgroups
										
										NodeList oeDataList = oeOutcomeElement.getElementsByTagName("IPD_DATA");
										for (int k = 0; k < oeDataList.getLength(); k++){
											Node oeDataNode = oeDataList.item(k);
											Element oeDataElement = (Element) oeDataNode;
											
											if (oeDataElement.getAttribute("STUDY_ID").equals(revManID)){
												
												Element oeSubgroupElement = null;
												
												oeobj = new OEandVarianceOutcome(oeDataElement, comparisonNameElement, oeOutcomeNameElement, oeOutcomeElement, oeSubgroupElement, reviewTitle, revManIdParameter);
												outcomeList.add(oeobj);
												
												
											}
										}
										
									} else {//there are subgroups
										for (int j = 0; j <oeSubgroupList.getLength(); j++){
											Node oeSubgroupNode = oeSubgroupList.item(j);
											Element oeSubgroupElement = (Element) oeSubgroupNode;
											
											NodeList oeDataList = oeSubgroupElement.getElementsByTagName("IPD_DATA");
											for (int k = 0; k < oeDataList.getLength(); k++){
												Node oeDataNode = oeDataList.item(k);
												Element oeDataElement = (Element) oeDataNode;
												
												if (oeDataElement.getAttribute("STUDY_ID").equals(revManID)){
													
													oeobj = new OEandVarianceOutcome(oeDataElement, comparisonNameElement, oeOutcomeNameElement, oeOutcomeElement, oeSubgroupElement, reviewTitle, revManIdParameter);
													outcomeList.add(oeobj);
													
												}
											}
										}
									}
										
									
								
								
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
						
						
//						NodeList comparisonNameList = comparisonElement.getElementsByTagName("NAME");
//						Node comparisonNameNode = comparisonNameList.item(0);
//						Element comparisonNameElement = (Element) comparisonNameNode;
//						
//						comparisonName = comparisonNameElement.getTextContent();
//						System.out.println(comparisonName);
					}//closes the for loop that starts with the comparison elements
					
					
//					System.out.println(mainAuthor);
//					
//					System.out.println("RandomSequenceBias: " + selectionBiasRandomSequenceBiasRisk + ". " + selectionBiasRandomSequenceJudgement 
//							+ "\n" + "AllocationBias: " + selectionBiasAllocationConcealmentBiasRisk + ". " + selectionBiasAllocationConcealmentJudgement
//							+ "\n" + "PerformanceBias: " + performanceBiasRisk + ". " + performanceBiasJudgement
//							+ "\n" + "DetectionBias: " + detectionBiasRisk + ". " + detectionBiasJudgement
//							+ "\n" + "AttritionBias: " + attritionBiasRisk + ". " + attritionBiasJudgement
//							+ "\n" + "ReportingBias: " + reportingBiasRisk + ". " + reportingBiasJudgement
//							+ "\n" + "OtherBias: " + otherBiasRisk + ". " + otherBiasJudgement
//							);
//				
					
//					for (int j = 0; j<references.length; j++){
//						System.out.println(references[j]);
//					}
					
//								System.out.println(year);
//					
//								System.out.println(revManID);
								authorYearLetter = mainAuthor + year + yearLetter;
								revManID = revManID.replaceAll("_x00df_", "ß").replaceAll("(_x002d_)", "-").replaceAll("(_x0026_)", "&").replaceAll("_x00e8_", "è").replaceAll("_x00f6_", "ö").replaceAll("_x00fc_", "ü").replaceAll("_x002b_", "+").replaceAll("_x002f_", "/").replaceAll("_x00a0_", " ").replaceAll("_x002c_", ",").replaceAll("_x0028_", "(").replaceAll("_x0029_", ")").replaceAll("_x00e7_", "ç").replaceAll("_x0027_", "'").replaceAll("_x002a_", "*").replaceAll("_x00e9_", "é").replaceAll("_x00e4_", "ä").replaceAll("_x00b4_", "´");
								charObject.revManID = revManID;
								
								////////////////////////////////////////////////////////////////
								//attempts to find study author from references
								
////////////////////////////////////////////////////////////////////////////////////////////////////
								/////extracts identifiers

								trialIdentifiersExtracting();
//////////////////////////////////////////////////////////////////////////////////////
								
								
								
								//System.out.println(authorYearLetter);
//					
					//				
//					System.out.println("Country or countries : " + countries);
//					System.out.println(meerKatCountry);
					//aauthorYearLetter = mainAuthor+year+yearLetter;
					//System.out.println("Setting");
					//System.out.println(settingProse);
					//System.out.println();
					
					
					
					
	}
	private void referenceIdentifiersExtracting(int j) {//extracts the identifiers of the reference specified by the integer parameter
		Element referenceElement = (Element) referencesList.item(j);
		
		NodeList identifiersList = referenceElement.getElementsByTagName("IDENTIFIERS");//node list of all identifier nodes
		Element identifiersElement = (Element) identifiersList.item(0);
		
		
		
		NodeList referenceIdentifierList = identifiersElement.getElementsByTagName("IDENTIFIER");
		
		
		for (int i = 0; i < referenceIdentifierList.getLength(); i++) {
			Element referenceIdentifierElement = (Element) referenceIdentifierList.item(i);//crates element for each node in identidiersList
			
			if (referenceIdentifierElement.getParentNode().getNodeName().equals("STUDY") == false) {
			
			
				String idType = referenceIdentifierElement.getAttribute("TYPE");//to test which object type has to be created below
				//System.out.println("Value ist: " + idType);
				if (idType.equals("DOI")) {
					referenceIdObj = new DoiIdentifier(referenceIdentifierElement, revManID, reviewTitle);
					referenceIdList.add(referenceIdObj);
					//System.out.println("DOI added");

				} else if (idType.equals("OTHER")) {
					referenceIdObj = new OtherIdentifier(referenceIdentifierElement, revManID, reviewTitle);
					referenceIdList.add(referenceIdObj);
					//System.out.println("Other added ");

				} else if (idType.equals("EMBASE")) {
					referenceIdObj = new EmbaseIdentifier(referenceIdentifierElement, revManID, reviewTitle);
					referenceIdList.add(referenceIdObj);
					//System.out.println("Embase added ");

				} else if (idType.equals("CENTRAL")) {
					referenceIdObj = new CentralIdentifier(referenceIdentifierElement, revManID, reviewTitle);
					referenceIdList.add(referenceIdObj);
					//System.out.println("Central added ");

				} else if (idType.equals("MEDLINE")) {
					referenceIdObj = new MedlineIdentifier(referenceIdentifierElement, revManID, reviewTitle);
					referenceIdList.add(referenceIdObj);
					//System.out.println("Medline added " + revManID + reviewTitle);

				} else if (idType.equals("PUBMED")) {
					referenceIdObj = new PubMedIdentifier(referenceIdentifierElement, revManID, reviewTitle);
					referenceIdList.add(referenceIdObj);
					//System.out.println("Pubmed added ");

				}
				
			
			}
		}
		
	}
	
	private void trialIdentifiersExtracting() {//in studies and references section, extracts identifiers of this study(such as doi, clinicaltrials.gov, isrctn.....)
		//this is on study level and not single references level
		
		NodeList identifiersList = studyElement.getElementsByTagName("IDENTIFIERS");
		Node identifiersNode = identifiersList.item(1);
		Element identifiersElement = (Element) identifiersNode;
		
		if (identifiersElement.getParentNode().getNodeName().equals("STUDY")) {
			NodeList identifierList = identifiersElement.getElementsByTagName("IDENTIFIER");
			
			
			
			for (int i = 0; i < identifierList.getLength(); i ++) {
				
				Element identifierElement = (Element) identifierList.item(i);
				String idValue = identifierElement.getAttribute("TYPE");
				
				if (idValue.equals("DOI")) {
					trialIdObject = new DoiIdentifier(identifierElement, revManID, reviewTitle);
					trialIdList.add(trialIdObject);
					//System.out.println("DOI added " );
					
				} else if (idValue.equals("CTG")){
					trialIdObject = new ClinTrialGovIdentifier(identifierElement, revManID, reviewTitle);
					trialIdList.add(trialIdObject);
					//System.out.println("CTG added " );
					
				} else if (idValue.equals("ISRCTN")){
					trialIdObject = new IsrctnIdentifier(identifierElement, revManID, reviewTitle);
					trialIdList.add(trialIdObject);
					//System.out.println("ISRCTN added " );
					
				} else if (idValue.equals("OTHER")){
					trialIdObject = new OtherIdentifier(identifierElement, revManID, reviewTitle);
					trialIdList.add(trialIdObject);
					//System.out.println("Other added " );
				}
			}
		}
		
		
	}
	
	private void referenceExtracting(){
		//This following for-loop runs through all references for the included study and saves relevant fields into an array that is created to hold information in the following positions:
		//0th,  ..index -> Type of publication, eg. Journal ->"TYPE"
		//1st..., ..index -> Primary attribute: Yes or No ->"PRIMARY"
		//2nd... ->Names of all authors of this publication ->"AU"
		//3rd...->Title of publication->"TI"
		//4th...->Name of Journal->Journal->"SO"
		//5...-> Year published->"YR"
		//6...->Volume ->"VL" 
		//7...-> Issue ->"NO"
		//8...-> Pages ->"PG"
		
		//9: TO original name
		//10: EN: edition
		//11: ED Editor
		//12: PB Publisher
		//13: CY City
		//14: MD medium
		
		//if a field is not available, eg. if the reference refers to a conference protocol that lacks page numbers, empty "" space is inserted and next position of array is tried to be filled
	
		
		referencesList = studyElement.getElementsByTagName("REFERENCE");
		int numberReferences = referencesList.getLength();
		
		/////////////getting identifiers for the references
		for (int j = 0; j <numberReferences; j++) {
			referenceIdentifiersExtracting(j);
		}
		///////////////////////////////////////
		
		references = new String[numberReferences * 15];
		int arrayCounter = 0;
		for (int i = 0; i < numberReferences; i++){
			Element referenceElement = null;
			try {
				Node referenceNode = referencesList.item(i);
				referenceElement = (Element) referenceNode;
			} catch (Exception e7) {
			}
		
			try {
				if (referenceElement != null){
				references[arrayCounter] = referenceElement.getAttribute("TYPE"); 
				//System.out.println(referenceElement.getAttribute("TYPE"));
				} else {
					references[arrayCounter] = "";
				}
			} catch (Exception e6) {
			}
			arrayCounter++;
			
			try {
				if (referenceElement != null){
				references[arrayCounter] = referenceElement.getAttribute("PRIMARY");
				//System.out.println(referenceElement.getAttribute("PRIMARY"));
				} else {
					references[arrayCounter] = "";
				}
			} catch (Exception e5) {
			}
			arrayCounter++;
			
			
			Element auElement = null;
			try {
				NodeList auList = referenceElement.getElementsByTagName("AU");
				Node auNode = auList.item(0);
				auElement = (Element) auNode;
			} catch (Exception e5) {
				e5.printStackTrace();
			}
			
			try {
				if (auElement != null){
				references[arrayCounter] = auElement.getTextContent().replaceAll("\n", "").trim(); //2nd, 11th... Authors names for this reference
				//System.out.println(auElement.getTextContent().replaceAll("\n", "").trim());
				} else {
					references[arrayCounter] = "";
				}
			} catch (DOMException e4) {
				e4.printStackTrace();
			}
			
			arrayCounter++;
			
			
			Element tiElement = null;
			try {
				NodeList tiList = referenceElement.getElementsByTagName("TI");
				Node tiNode = tiList.item(0);
				tiElement = (Element) tiNode;
			} catch (Exception e4) {
				e4.printStackTrace();
			}
			
			try {
				if (tiElement != null){
				references[arrayCounter] = tiElement.getTextContent().replaceAll("\n", "").trim();
				//System.out.println(tiElement.getTextContent().replaceAll("\n", "").trim());
				} else {
					references[arrayCounter] = "";
				}
			} catch (DOMException e3) {
				e3.printStackTrace();
			}
			
			arrayCounter++;
			
			
			Element soElement = null;
			try {
				NodeList soList = referenceElement.getElementsByTagName("SO");
				Node soNode = soList.item(0);
				soElement = (Element) soNode;
			} catch (Exception e3) {
				e3.printStackTrace();
			}
			
			try {
				if (soElement != null){
				references[arrayCounter] = soElement.getTextContent().replaceAll("\n", "").trim();
				//System.out.println(soElement.getTextContent().replaceAll("\n", "").trim());
				} else {
					references[arrayCounter] = "";
				}
			} catch (DOMException e2) {
				e2.printStackTrace();
			}
			
			arrayCounter++;
			
			
			Element yrElement = null;
			try {
				NodeList yrList = referenceElement.getElementsByTagName("YR");
				Node yrNode = yrList.item(0);
				yrElement = (Element) yrNode;
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			
			try {
				if (yrElement != null){
				references[arrayCounter] = yrElement.getTextContent().replaceAll("\n", "").trim();
				//System.out.println(yrElement.getTextContent().replaceAll("\n", "").trim());
				} else {
					references[arrayCounter] = "";
				}
			} catch (DOMException e1) {
				e1.printStackTrace();
			}
			
			arrayCounter++;
			
			
			Element vlElement = null;
			try {
				NodeList vlList = referenceElement.getElementsByTagName("VL");
				Node vlNode = vlList.item(0);
				vlElement = (Element) vlNode;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			try {
				if (vlElement != null){
				references[arrayCounter] = vlElement.getTextContent().replaceAll("\n", "").trim();
				//System.out.println(vlElement.getTextContent().replaceAll("\n", "").trim());
				} else {
					references[arrayCounter] = "";
				}
			} catch (DOMException e) {
				e.printStackTrace();
			}
			arrayCounter++;
			
			Element noElement = null;
			try {
				NodeList noList = referenceElement.getElementsByTagName("NO");
				Node noNode = noList.item(0);
				noElement = (Element) noNode;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			try {
				if (noElement != null){
				references[arrayCounter] = noElement.getTextContent().replaceAll("\n", "").trim();
				//System.out.println(noElement.getTextContent().replaceAll("\n", "").trim());
				} else {
					references[arrayCounter] = "";
				}
			} catch (DOMException e) {
				e.printStackTrace();
			}
			
			arrayCounter++;
			
			Element pgElement = null;
			try {
				NodeList pgList = referenceElement.getElementsByTagName("PG");
				Node pgNode = pgList.item(0);
				pgElement = (Element) pgNode;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			try {
				if (pgElement != null){
				references[arrayCounter] = pgElement.getTextContent().replaceAll("\n", "").trim();
				//System.out.println(pgElement.getTextContent().replaceAll("\n", "").trim());
				} else {
					references[arrayCounter] = "";
				}
			} catch (DOMException e) {
				e.printStackTrace();
			}
			arrayCounter++;
			
			Element toElement = null;
			try {
				NodeList toList = referenceElement.getElementsByTagName("TO");
				Node toNode = toList.item(0);
				toElement = (Element) toNode;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			try {
				if (toElement != null){
				references[arrayCounter] = toElement.getTextContent().replaceAll("\n", "").trim();
				//System.out.println(pgElement.getTextContent().replaceAll("\n", "").trim());
				} else {
					references[arrayCounter] = "";
				}
			} catch (DOMException e) {
				e.printStackTrace();
			}
			arrayCounter++;
			
			Element enElement = null;
			try {
				NodeList enList = referenceElement.getElementsByTagName("EN");
				Node enNode = enList.item(0);
				enElement = (Element) enNode;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			try {
				if (enElement != null){
				references[arrayCounter] = enElement.getTextContent().replaceAll("\n", "").trim();
				//System.out.println(pgElement.getTextContent().replaceAll("\n", "").trim());
				} else {
					references[arrayCounter] = "";
				}
			} catch (DOMException e) {
				e.printStackTrace();
			}
			arrayCounter++;
			
			Element edElement = null;
			try {
				NodeList edList = referenceElement.getElementsByTagName("ED");
				Node edNode = edList.item(0);
				edElement = (Element) edNode;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			try {
				if (edElement != null){
				references[arrayCounter] = edElement.getTextContent().replaceAll("\n", "").trim();
				//System.out.println(pgElement.getTextContent().replaceAll("\n", "").trim());
				} else {
					references[arrayCounter] = "";
				}
			} catch (DOMException e) {
				e.printStackTrace();
			}
			arrayCounter++;
			
			Element pbElement = null;
			try {
				NodeList pbList = referenceElement.getElementsByTagName("PB");
				Node pbNode = pbList.item(0);
				pbElement = (Element) pbNode;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			try {
				if (pbElement != null){
				references[arrayCounter] = pbElement.getTextContent().replaceAll("\n", "").trim();
				//System.out.println(pgElement.getTextContent().replaceAll("\n", "").trim());
				} else {
					references[arrayCounter] = "";
				}
			} catch (DOMException e) {
				e.printStackTrace();
			}
			arrayCounter++;
			
			Element cyElement = null;
			try {
				NodeList cyList = referenceElement.getElementsByTagName("CY");
				Node cyNode = cyList.item(0);
				cyElement = (Element) cyNode;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			try {
				if (cyElement != null){
				references[arrayCounter] = cyElement.getTextContent().replaceAll("\n", "").trim();
				//System.out.println(pgElement.getTextContent().replaceAll("\n", "").trim());
				} else {
					references[arrayCounter] = "";
				}
			} catch (DOMException e) {
				e.printStackTrace();
			}
			arrayCounter++;
			
			Element mdElement = null;
			try {
				NodeList mdList = referenceElement.getElementsByTagName("MD");
				Node mdNode = mdList.item(0);
				mdElement = (Element) mdNode;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			try {
				if (mdElement != null){
				references[arrayCounter] = mdElement.getTextContent().replaceAll("\n", "").trim();
				//System.out.println(pgElement.getTextContent().replaceAll("\n", "").trim());
				} else {
					references[arrayCounter] = "";
				}
			} catch (DOMException e) {
				e.printStackTrace();
			}
			arrayCounter++;
			
		}
	}
	
	
		

		
	
	
	public String getMainAuthor() {
		return mainAuthor;
	}



	public void setMainAuthor(String mainAuthor) {
		this.mainAuthor = mainAuthor;
	}



	public String getYear() {
		return year;
	}



	public void setYear(String year) {
		this.year = year;
	}



	public String getAuthorYearLetter() {
		return authorYearLetter;
	}



	public void setAuthorYearLetter(String authorYearLetter) {
		this.authorYearLetter = authorYearLetter;
	}



	

	public String getRevManID() {
		return revManID;
	}



	public void setRevManID(String revManID) {
		this.revManID = revManID;
	}


@XmlElement
	public String[] getReferences() {
		return references;
	}



	public void setReferences(String[] references) {
		this.references = references;
	}



	



	

	public void setOutcomeList(List<Outcome> outcomeList) {
		this.outcomeList = outcomeList;
	}
/////////////////////////////////complicated ones

//	public ReferenceObject getRefObject() {
//		return refObject;
//	}
//
//
//	public void setRefObject(ReferenceObject refObject) {
//		this.refObject = refObject;
//	}
	
	
	public List<Reference> getReferenceList() {
		return referenceList;
	}


	public void setReferenceList(List<Reference> referenceList) {
		this.referenceList = referenceList;
	}


//	public ContinuousOutcomeObject getCobj() {
//		return cobj;
//	}
//
//
//	public void setCobj(ContinuousOutcomeObject cobj) {
//		this.cobj = cobj;
//	}
//
//
//	public DichotomousOutcomeObject getDobj() {
//		return dobj;
//	}
//
//	public void setDobj(DichotomousOutcomeObject dobj) {
//		this.dobj = dobj;
//	}

	
	@XmlElement(name = "OUTCOME")
	public List<Outcome> getOutcomeList() {
		return outcomeList;
	}
	
	public String getReviewTitle() {
		return reviewTitle;
	}

	public void setReviewTitle(String reviewTitle) {
		this.reviewTitle = reviewTitle;
	}

	public String getDoi() {
		return doi;
	}

	public void setDoi(String doi) {
		this.doi = doi;
	}
	
//	public OEandVarianceOutcomeObject getOeobj() {
//		return oeobj;
//	}
//
//	public void setOeobj(OEandVarianceOutcomeObject oeobj) {
//		this.oeobj = oeobj;
//	}
//	public GenericInverseOutcomeObject getGivobj() {
//		return givobj;
//	}
//
//	public void setGivobj(GenericInverseOutcomeObject givobj) {
//		this.givobj = givobj;
//	}
//	public OtherOutcomeObject getOobj() {
//		return oobj;
//	}
//
//	public void setOobj(OtherOutcomeObject oobj) {
//		this.oobj = oobj;
//	}
	

	public Characteristics getCharObject() {
		return charObject;
	}


	public void setCharObject(Characteristics charObject) {
		this.charObject = charObject;
	}
	public List<Identifier> getTrialIdentifierObjectsList() {
		return trialIdList;
	}

	public void setTrialIdentifierObjectsList(List<Identifier> trialIdentifierObjectsList) {
		this.trialIdList = trialIdentifierObjectsList;
	}

//	public IdentifierObject getTrialIdentifierObject() {
//		return trialIdObject;
//	}
//
//	public void setTrialIdentifierObject(IdentifierObject trialIdentifierObject) {
//		this.trialIdObject = trialIdentifierObject;
//	}
	
	public String getExtractedAuthor() {
		return extractedAuthor;
	}

	public void setExtractedAuthor(String extractedAuthor) {
		this.extractedAuthor = extractedAuthor;
	}
//	public IdentifierObject getReferenceIdObj() {
//		return referenceIdObj;
//	}
//	public void setReferenceIdObj(IdentifierObject referenceIdObj) {
//		this.referenceIdObj = referenceIdObj;
//	}
	public List<Identifier> getReferenceIdList() {
		return referenceIdList;
	}
	public void setReferenceIdList(List<Identifier> referenceIdList) {
		this.referenceIdList = referenceIdList;
	}

}
