Steps to execute the Indexing and Retrieval assignment in Java:
==============================================================
1. Open Eclipse Luna on CCIS machine. If not present please install Eclipse Mars (Link to download: http://www.eclipse.org/downloads/)
2. Unzip/Extract IndexingRetrieval.zip file.
3. Go to File --> Import... --> General and finally select "Existing Projects into Workspace" and then click Next.
4. On the next dialog box, hit Browse and select folder "IndexingRetrieval" (Note: It may be present inside a folder called "IndexingRetrieval"  
   which consists of 'IndexingRetrieval' and 'MACOSX' Please select the inner folder.  
5. Check 'Copy projects into workspace' and then hit Finish.
6. You could see several errors in the project folder. (This is due to difference in Java versions). 
   If no errors then you can proceed to step 7.
7. To solve the errors:
	a. Right click on the IndexingRetrieval project -->'Properties' --> 'Java Build Path' --> 'Libraries'.
	b. You could see errors in JRE System Library as it would be using [JavaSE-1.8] (displayed as unbound).
	c. Next select JRE System Library and then hit 'Edit' and select the Execution environment as (JavaSE-1.7 or the java version 
	   the machine is running on) and then hit 'Finish'. 
	d. Press OK and then we are good to run the program.
8. Expand the IndexingRetrieval project in Project Explorer. Then --> src --> edu.neu.cs6200.indexing

For building an inverted index and writing it into a file called index.out and also writing the final result into result.eval
=============================================================================================================================
       a. Make sure the input files tccorpus.txt and queries.txt are present in the directory otherwise exception might be raised.
       b. Open the Indexing.java file and hit Run (Green button with an arrow at the top).
       c. Once the program runs it will generate a file called “index.out” which contains the inverted index list for each word.
          i.e. each word and the corresponding list of all documents with their document id containing that particular 
          word along with it’s term frequency.
       d. Subsequently, a result.eval file would be generated which contains the BM25 scores of all the documents containing the 
          query term within them for each query and are sorted and ranked based on their score values. The number of documents 
          displayed depends on the input provided by the user. For now the numOfDocs displayed is 100 (default).

Contents of IndexingRetrieval.zip :
=================================
1. IndexingRetrieval project folder which contains the src folder having all the source files.
2. IndexingRetrieval project folder additionally consist of:
   a. tccorpus.txt : input file containing the document collection.
   b. queries.txt : is the input file containing the already stemmed queries.
   c. index.out : is the file generated after running the indexer on tccorpus.txt i.e. it contains an inverted index list
                  i.e. each word and the corresponding list of all the documents containing that particular word along
                  with it’s term frequency.
   d. results.eval : contains the BM25 scores of all the documents in which the given query term exists and the document is 
                     ranked and sorted based on the score_list for each documents.
   h. Readme.txt: instructions to run the program.
   i. IndexingRetrievalAnalysis.txt: contains a short report of my implementation.
