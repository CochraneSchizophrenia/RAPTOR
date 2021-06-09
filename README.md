# RAPTOR
RAPTOR is a proof of concept. It shows that all data can be extracted from batches of ReviewManager files. Partly, it also shows that these data can be cleaned. Its output is a XML file which can be queried e.g. in Excel.

A newer, lightweight version of RAPTOR which does not rely on Java and extracts data from diagnostic reviews is available in the list of files above: DR-RAPTOR (Diagnostic Review - RevmAn Parsing TOol for Reviewers). Its here: https://colab.research.google.com/github/CochraneSchizophrenia/RAPTOR/blob/master/Diagnostic_RAPTOR.ipynb and alternatively a Python script for running the code offline is given in the repository above.

For the old RAPTOR which looks at interventional review's data there are currently some issues for people who use newer versions of Java. This application works on Java 1.7.

Above, you find a spreadsheet with all queried bias data, extracted from 224 systematic reviews using the old RAPTOR.

There are two .mp4 files with screencasts that demonstrate using the old RAPTOR and carrying out some basic queries in Excel. You might need to activate Excel's developer tab beforehand (https://support.office.com/en-ie/article/show-the-developer-tab-e1192344-5e56-4d45-931b-e5fd9bea2d45). The easiest way of querying data is shown: dragging a parent node over to the spreadsheet area to create a sortable, formatted excel table with all it's childrens data. It works best if only data from the same parent node are queried per spreadsheet. 

This is a proof of concept, so please feel free to give any feedback or to ask questions about the implementation/ problems that arise when using RAPTOR!

Please download the executable .jar file from the 'releases' tab for the old RAPTOR, or from this link: https://github.com/CochraneSchizophrenia/RAPTOR/releases

This is a Java application, written and tested in an Java 1.7 environment. 

Contact:
lena.schmidt@bristol.ac.uk for any questions/ideas or bug reports.

Please cite as: 

*Schmidt, L., Shokraneh, F., Steinhausen, K., Adams, CE. Introducing RAPTOR: RevMan Parsing Tool for Reviewers. Syst Rev 8, 151 (2019). https://doi.org/10.1186/s13643-019-1070-0*

