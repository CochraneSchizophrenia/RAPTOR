import xml.etree.ElementTree as xml
from typing import TypeVar
from statistics import median, mean
import pandas as pd
import copy
import os

#####!!!!!!!!!!CHANGE TO YOUR OWN LOCAL PARENT DIRECTORY WHERE REVMAN FILES ARE!!!!!!!!######
DATA_PATH="C:\\Users\\lena\\Documents\\RAPTOR files"
#############################

def getAttribute(element: TypeVar("Element"), path: str, default: str = None) -> str:
    result = element.get(path)
    if result == None or len(result) == 0:
        return default
    else:
        return result


def getChildTexts(element: TypeVar("Element"), path: str, default: str = None, separator: str = "\n") -> str:
    # Find the path in the element
    result = element.findall(path)

    # Return the default if there is no such element
    if result is None or len(result) == 0:
        return default

    # Extract the text and return it
    else:
        all_text = []
        for item in result:
            all_text.extend(item.itertext())
        my_t = " ".join(all_text).replace("\n", "")
        # print(my_t)
        return my_t


# def getContent(#this method is not so good because it fails to extract nested text within tags such as <SUP>
#     element: TypeVar("Element"), path: str, default: str = None, separator: str = "\n"
# ) -> str:
#     """ Internal helper method that retrieves the text content of an
#         XML element.

#         Parameters:
#             - element   Element, the XML element to parse.
#             - path      Str, Nested path in the XML element.
#             - default   Str, default value to return when no text is found.

#         Returns:
#             - text      Str, text in the XML node.
#     """

#     # Find the path in the element
#     result = element.findall(path)

#     # Return the default if there is no such element
#     if result is None or len(result) == 0:
#         return default

#     # Extract the text and return it
#     else:
#         # print("more than 1")
#         # print(separator.join([sub.text for sub in result if sub.text is not None]))
#         return separator.join([sub.text for sub in result if sub.text is not None])


def get_basic_data(root, default="Data not found"):
    #############################
    # Function to retrieve 'basic' metadata from a rm5, such as DOI, title, but also data of publication, date of last search, and year of studies published
    #
    # Guide to extension: Any new data item can be added to the dictionary 'review_data'. By defining an xml path from the XML root element, data can be extracted from any place of the .rm5
    #
    #############################
    review_data = {}

    review_data["IS_QUADAS_2"] = getAttribute(root, "QUADAS2", default=default)
    review_data["REVIEW_TYPE"] = getAttribute(root, "TYPE", default=default)
    review_data["REVIEW_DOI"] = getAttribute(root, "DOI", default=default)
    review_data["REVIEW_GROUP"] = getAttribute(root, "GROUP_ID", default=default)

    year = getAttribute(root.find(".//COVER_SHEET//DATES//LAST_SEARCH//DATE"), "YEAR", default=default)
    month = getAttribute(root.find(".//COVER_SHEET//DATES//LAST_SEARCH//DATE"), "MONTH", default=default)
    review_data["LAST_SEARCH"] = "{}/{} last searched".format(month, year)

    review_data["REVIEW_PUBLISHED_YEAR"] = getAttribute(root.find(".//COVER_SHEET//DATES//LAST_CITATION_ISSUE"), "YEAR",
                                                        default=default)

    review_data["REVIEW_TITLE"] = getChildTexts(root.find(".//COVER_SHEET"), "TITLE", default=default)

    included_studies = root.findall(".//STUDIES_AND_REFERENCES//STUDIES//INCLUDED_STUDIES//STUDY")
    review_data["NUM_INCLUDED_STUDIES"] = len(included_studies)
    years = []
    ids = []

    for elem in included_studies:
        ids.append(getAttribute(elem, "ID", default=default))
        years.append(getAttribute(elem, "YEAR", default=default))

    int_years = []
    for y in years:
        if y != default:
            try:
                int_years.append(int(y))
            except:
                pass

    review_data["MEAN_INCL_STUDIES_PUBLISHED"] = int(mean(int_years))
    review_data["MEDIAN_INCL_STUDIES_PUBLISHED"] = int(median(int_years))
    review_data["INCL_STUDY_YEARS"] = "; ".join(years)
    review_data["INCL_STUDY_IDs"] = "; ".join(ids)

    # print(review_data)
    return review_data, ids


def get_bias_data(root, review_data, default="Data not found"):
    bias_items = root.findall(".//QUALITY_ITEMS//QUALITY_ITEM")
    all_data = []

    for elem in bias_items:
        basic = {}  # add some data for each bias domain
        basic["BIAS_ELEMENT_ID"] = getAttribute(elem, "ID", default=default)

        basic["BIAS_LEVEL_OF_ASSESSMENT"] = getAttribute(elem, "LEVEL", default=default)
        basic["CHARACTERISTIC"] = getAttribute(elem, "CHARACTERISTIC", default=default)
        basic["IS_CORE_ITEM"] = getAttribute(elem, "CORE_ITEM", default=default)
        basic["DOMAIN_NR"] = getAttribute(elem, "DOMAIN", default=default)
        basic["DOMAIN_NAME"] = getAttribute(elem, "DOMAIN_NAME", default=default)
        basic["SIGNALLING_QUESTION"] = getChildTexts(elem, "NAME", default=default)
        basic["SIGNALLING_QUESTION_DESCRIPTION"] = getChildTexts(elem, "DESCRIPTION", separator=" ", default=default)

        bias_data = elem.findall(".//QUALITY_ITEM_DATA")
        for d in bias_data:
            bias_entries = d.findall(".//QUALITY_ITEM_DATA_ENTRY")
            for entry in bias_entries:
                this_result = copy.deepcopy(basic)  # add some data for each bias item
                this_result["RESULT"] = getAttribute(entry, "RESULT", default=default)
                this_result["JUDGEMENT_TEXT"] = getChildTexts(entry, "DESCRIPTION", separator=" ", default=default)

                this_result["STUDY_ID"] = getAttribute(entry, "STUDY_ID", default=default)

                this_result["REVIEW_TITLE"] = review_data["REVIEW_TITLE"]  # add some review metadata
                this_result["REVIEW_PUBLISHED_YEAR"] = review_data["REVIEW_PUBLISHED_YEAR"]
                this_result["REVIEW_DOI"] = review_data["REVIEW_DOI"]
                this_result["IS_QUADAS_2"] = review_data["IS_QUADAS_2"]

                all_data.append(this_result)
    # print(all_data)
    return all_data


def get_stats_data(root, review_data, default="Data not found"):
    stats_items = root.findall(".//ANALYSES_AND_DATA//TESTS//TEST")
    all_data = []

    for elem in stats_items:
        basic = {}  # add some data for each bias domain
        basic["STATS_ELEMENT_ID"] = getAttribute(elem, "ID", default=default)

        basic["SUB_NAME"] = getChildTexts(elem, "NAME", default=default)
        basic["FULL_NAME"] = getChildTexts(elem, "FULL_NAME", default=default)
        basic["DESCRIPTION"] = getChildTexts(elem, "DESCRIPTION", separator=" ", default=default)

        test_data = elem.findall(".//TEST_DATA")
        for d in test_data:
            test_entries = d.findall(".//TEST_DATA_ENTRY")
            for entry in test_entries:
                this_result = copy.deepcopy(basic)  # add some data for each bias item

                this_result["FN"] = getAttribute(entry, "FN", default=default)
                this_result["FP"] = getAttribute(entry, "FP", default=default)
                this_result["TN"] = getAttribute(entry, "TN", default=default)
                this_result["TP"] = getAttribute(entry, "TP", default=default)

                this_result["STUDY_ID"] = getAttribute(entry, "STUDY_ID", default=default)

                this_result["REVIEW_TITLE"] = review_data["REVIEW_TITLE"]  # add some review metadata
                this_result["REVIEW_PUBLISHED_YEAR"] = review_data["REVIEW_PUBLISHED_YEAR"]
                this_result["REVIEW_DOI"] = review_data["REVIEW_DOI"]

                all_data.append(this_result)
    # print(all_data)
    return all_data


def get_elements(xml_path: str, path_bias: str = "", path_data: str = ""):
    # RETURNS a dict for basic review data, and list of dict for all bias and result items (one dict per study per item)
    root = xml.parse(xml_path).getroot()
    review_data, ids = get_basic_data(root)
    bias_items = get_bias_data(root, review_data)
    stats_items = get_stats_data(root, review_data)

    # print(getAttribute(root.find(".//COVER_SHEET"), "MODIFIED_BY"))
    return review_data, bias_items, stats_items


##############################################################################################

rm5_files=[os.path.join(DATA_PATH,f) for f in os.listdir(DATA_PATH) if ".rm5" in f]#grab all files
# uploaded = files.upload()
# rm5_files = [f for f in uploaded.keys() if ".rm5" in f]  # grab all files
################################################################################################

basic_data = []
basic_data_colnames = set()

bias_data = []
bias_data_colnames = set()

stats_data = []
stats_data_colnames = set()

# rm5_files=[f for f in files if ".rm5" in f]#use only .rm5 files
counter = 0
for f in rm5_files:
    print("\nExtracting data from: {} ...".format(f))
    print("   Extracting basic review info, bias data, stats data...")
    review_data, bias_items, stats_items = get_elements(f)

    basic_data.append(review_data)  # append this single dict
    basic_data_colnames.update(list(review_data.keys()))  # amake sure every entry has a column name

    bias_data.extend(bias_items)

    for i in bias_items:
        bias_data_colnames.update(list(i.keys()))

    stats_data.extend(stats_items)

    for i in stats_items:
        stats_data_colnames.update(list(i.keys()))

    print("   Extracted: review metadata n={}; Study bias data n={} items; Study result data n={} results".format(1,
                                                                                                                  len(
                                                                                                                      bias_items),
                                                                                                                  len(
                                                                                                                      stats_data)))

    counter += 1

basic_df = pd.DataFrame(basic_data, columns=list(basic_data_colnames))
basic_df.to_csv("basic_review_data.csv", index=False, encoding='utf-8')

bias_df = pd.DataFrame(bias_data, columns=list(bias_data_colnames))
bias_df.to_csv("bias_review_data.csv", index=False, encoding='utf-8')

stats_df = pd.DataFrame(stats_data, columns=list(stats_data_colnames))
stats_df.to_csv("stats_review_data.csv", index=False, encoding='utf-8')

# files.download("basic_review_data.csv")
# files.download("bias_review_data.csv")
# files.download("stats_review_data.csv")

print("------Extracted data from {} reviews and saved files in working directory----".format(counter))
