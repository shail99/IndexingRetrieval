package edu.neu.cs6200.indexing;
import java.io.*;
import java.util.*;

import edu.neu.cs6200.info.DocInfo;

public class Indexing {
	
	private static void indexer(LinkedHashMap<String, ArrayList<DocInfo>> ind, LinkedHashMap<Integer, Integer> dl, String ip_file, String out_file) {
		
		// open the input file tccorpus.txt for creating the inverted index list
		BufferedReader input = openInputFileRead(ip_file);
		
		String sCurrentLine;
		try {
			String docId = "0";
			DocInfo dinf = null;
			while ((sCurrentLine = input.readLine()) != null) {
				
				// check the line for retrieving the document id of the document
				if(sCurrentLine.startsWith("#"))
				{
					String[] str = sCurrentLine.split(" ");
				    docId = str[1];
				    dl.put(Integer.parseInt(docId), 0);
				}
				// check for all the subsequent lines for the corresponding document id
				else
				{
					String[] str = sCurrentLine.split(" ");
					for(int i = 0 ; i < str.length ; i++)
					{
						// check for a word which consists of only digits
						if (!str[i].matches("\\d+"))
						{	
							// if the word already present in our inverted index
							if(ind.containsKey(str[i]))
							{
								// check for multiple copies of the word within the document
								if(ind.get(str[i]).get(ind.get(str[i]).size() - 1).getDocId() == Integer.parseInt(docId))
								{
									ind.get(str[i]).get(ind.get(str[i]).size() - 1).incrementTermFreq();
									
								}
								// create a document with new document id 
								// and initial value of term_freq as 1
								else
								{
									dinf = new DocInfo(Integer.parseInt(docId),1);
									ind.get(str[i]).add(dinf);
								}
								
							}
							// if the word is not present in the inverted index list
							// then initialize the array list and add the new document 
							// object with initial values
							else
							{
								ArrayList<DocInfo> dlist = new ArrayList<DocInfo>();
								dinf = new DocInfo(Integer.parseInt(docId),1);
								dlist.add(dinf);
								ind.put(str[i], dlist);
								
							}
							dl.put(Integer.parseInt(docId),dl.get(Integer.parseInt(docId))+1);
						} 
						//end of if for numeric check
					}
					// end of for	
				}
				// end of else part
			}
			// end of for
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// close Input file after read operation for corpus file
		closeInputFileRead(ip_file,input); 
		// creating a buffered writer object for writing to index.out
		BufferedWriter output = openOutputFile(out_file);
		// call a function which writes the inverted index list into the file index.out
		writeIndexer(ind,output);
		// close output file after writing to index.out
		closeOutputFileWrite(out_file,output);
	}
	
	/*
	 * method to close the output file after completion of writing
	 */
	private static void closeOutputFileWrite(String out_file, BufferedWriter output) {
		try {
			if (output != null) output.close();
		} 
		catch (IOException ex) {
			ex.printStackTrace();
		}
		
	}

	/*
	 * method to write the final results to index.out file
	 */
	private static void writeIndexer(LinkedHashMap<String, ArrayList<DocInfo>> ind, BufferedWriter output) {
		
		for(Map.Entry<String, ArrayList<DocInfo>> entry : ind.entrySet())
 		{
			 String txt="";
			 txt+= entry.getKey()+" ";
			 for(DocInfo d : entry.getValue())
			 {
				txt+=d+" ";
			 }
			 txt+="\n";
			 try {
				output.write(txt);
			} catch (IOException e) {
				e.printStackTrace();
			}
 		}
		
	}

	/* method to open the output file 
	 * for write operation
	 */
	private static BufferedWriter openOutputFile(String out_file) {
		BufferedWriter output  = null;
		try {
			 
			 File opfile = new File(out_file);
	         output = new BufferedWriter(new FileWriter(opfile));
		}
		 catch (IOException e) {
			e.printStackTrace();
		}
		return output;
	}
	
	/*
	 * method to close the input file after read operation
	 */
	private static void closeInputFileRead(String ip_file, BufferedReader input) {
		try {
			if (input != null) input.close();
		} 
		catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/*
	 * method to open the input file for read operation
	 */
	private static BufferedReader openInputFileRead(String ip_file) {
		
		BufferedReader input  = null;
		try {
			 
			 File ipfile = new File(ip_file);
	         input = new BufferedReader(new FileReader(ipfile));
		}
		 catch (IOException e) {
			e.printStackTrace();
		}
		return input;
	}
	
	/*
	 * method to get the average document length i.e avdl
	 */
	private static double get_avdl(LinkedHashMap<Integer, Integer> dl) {
		double sum = 0;
		for(Map.Entry<Integer, Integer> entry : dl.entrySet())
 		{
			 sum+=entry.getValue();	 
 		}
		
		return sum/dl.size();
	}

	/*
	 * method to calculate the BM25 value for the queries and writing 
	 * the final result to result.eval file
	 */
	private static void calculateBM25(LinkedHashMap<String, ArrayList<DocInfo>> ind, LinkedHashMap<Integer, Integer> dl,
			String query_file, String result_file, int numOfDocs) 
	{
		//System.out.println(ind.get("system").size());
		BufferedReader input = openInputFileRead(query_file);
		String txtLine="";
		
		BufferedWriter output = openOutputFile(result_file);
		
		try {
			int query_id = 1;
			double k1 = 1.2;
			double k2 = 100;
			double b = 0.75;
			while ((txtLine = input.readLine()) != null) {
				/*
				 * LinkedHashMap<String,Double> to store the query word
				 * and its corresponding occurence in the query text
				 */
				LinkedHashMap<String,Double> qfreq = new LinkedHashMap<String,Double>();
				build_frequency_for_query(txtLine,qfreq);
				/*
				 * LinkedHashMap<Integer,Double> to store the score of each Document in
				 * which the query word is present
				 * i.e. document id is kept as key and it's score as the value in the map.
				 */
				LinkedHashMap<Integer,Double> score_list = new LinkedHashMap<Integer,Double>();
				String query[] = txtLine.split(" ");
				
				LinkedHashSet<String> unique_list = new LinkedHashSet<String>(Arrays.asList(query));
				
				double result = 0;
				for(String q : unique_list)
				{
					// iterate on all the query terms
					
					ArrayList<DocInfo> ilist = ind.get(q);
					for(int j=0; j < ilist.size(); j++)
					{
						double term1 = (1.0/(((double) ilist.size() + 0.5)/((double) dl.size() - (double) ilist.size() + 0.5)));
						double K_term = (k1 * ((1.0 - b) + (b * (double) dl.get(ilist.get(j).getDocId()) / get_avdl(dl))));
						double term2 = ((k1 + 1.0) * (double) ilist.get(j).getTerm_freq())/(K_term + (double) ilist.get(j).getTerm_freq());
						double term3 = ((k2 + 1.0) * qfreq.get(q))/(k2 + qfreq.get(q));
						
						// result for a single query term
						result = Math.log(term1 * term2 * term3);
						
						/*
						 * check if the document id is already added in the hash map
						 * then we update i.e. add the result to the value already 
						 * stored in the hash map
						 * else we simply put the new value
						 */
						if(score_list.containsKey(ilist.get(j).getDocId()))
						{
							score_list.put(ilist.get(j).getDocId(), score_list.get(ilist.get(j).getDocId()) + result);
						}
						else
						{
							score_list.put(ilist.get(j).getDocId(), result);
						}
					}
					// end inner for	
				}
				// end outer for
				write_query_to_file(txtLine,query_id,score_list,numOfDocs,output);
				query_id++;
			}
			// end while
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		/*
		 * close the queries.txt and result.eval file after 
		 * the processing is completed
		 */
		closeInputFileRead(query_file,input);
		closeOutputFileWrite(result_file, output);
		
	}

	/*
	 * method to get the frequency of the query term in 
	 * the particular query contained in queries.txt
	 */
	private static void build_frequency_for_query(String txtLine, LinkedHashMap<String, Double> qfreq) {
		String query[] = txtLine.split(" ");
		for(String str : query)
		{
			if(qfreq.containsKey(str))
				qfreq.put(str, qfreq.get(str)+1.0);
			else
				qfreq.put(str, 1.0);
		}
	}

	/*
	 * method to write the final query result consisting of 
	 * all the documents ranked by their scores in descending order
	 * alongwith their score result and rank
	 */
	private static void write_query_to_file(String query,int query_id, LinkedHashMap<Integer, Double> score_list, int numOfDocs, BufferedWriter output) {
		
		String txtLine = "Document ranking for query: "+query+",No of Hits: "+score_list.size()+"\n";
		/*
		 * using comparator on Entry set of the LinkedHashMap in order to
		 * sort the HashMap based on the score results
		 */
		List<Map.Entry<Integer, Double>> entries = new ArrayList<Map.Entry<Integer, Double>>(score_list.entrySet());
		Collections.sort(entries, new Comparator<Map.Entry<Integer, Double>>()
				 {
					  public int compare(Map.Entry<Integer, Double> a, Map.Entry<Integer, Double> b){
						  
						  if (a.getValue() < (b.getValue()))
								  return 1;
						  else if (a.getValue() > (b.getValue()))
							  return -1;
						  else
							  return 0;
					  }
					});
		
		int count = 1;
		/*
		 * writing the final result to resul.eval file
		 */
		for(Map.Entry<Integer, Double> e : entries)
		{
			if(count > numOfDocs)
				break;
				
			txtLine+= query_id+" "+"Q"+(query_id-1)+" "+e.getKey()+" "+count+" "+e.getValue()+" "+"my_system_name\n";
			count++;
		}
		
		txtLine+="-------------------------------------------------------\n";
		try {
			output.write(txtLine);
		} catch (IOException e1) {
			
			e1.printStackTrace();
		}
		
	}
	
	/*
	 * method to load the index.out file into LinkedHashMap 
	 * i.e. creating an inverted index list data structure
	 */
	private static LinkedHashMap<String, ArrayList<DocInfo>> loadDsFromFile(String index_file) {
		
		BufferedReader input = openInputFileRead(index_file);
		String index_line="";
		LinkedHashMap<String,ArrayList<DocInfo>> inv_ind = new LinkedHashMap<String,ArrayList<DocInfo>>();
		try {
			while((index_line = input.readLine()) != null)
			{
				ArrayList<DocInfo> docs = new ArrayList<DocInfo>();
				String index[]= index_line.split(" ");
				/*
				 * scan through all the list of the documents for the particular key word
				 */
				for(int i=1 ; i< index.length; i++)
				{
					/*
					 * parse to get the document details of a single document
					 */
					String doc_det[]=index[i].split(",");
					/*
					 * parse to get the document id 
					 * and the document term frequency
					 */
					String doc_id[] = doc_det[0].split("\\[");
					String doc_term_freq[] = doc_det[1].split("\\]");
					
					/*
					 * create a document object and adding it to the arraylist of documents
					 */
					DocInfo dinf = new DocInfo(Integer.parseInt(doc_id[1]),Integer.parseInt(doc_term_freq[0]));
					docs.add(dinf);
				}
				
				/* storing the word as the key and the arraylist of all the docs 
				 * corresponding to the particular word
				 */
				inv_ind.put(index[0], docs);
				
			}
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		// close input file after finishing reading of the index.out file
		closeInputFileRead(index_file, input);
		// return the linked hash map containing the inverted index list
		return inv_ind;
	}

	public static void main (String args[])
	{
		/* Using a linked hash map to store the inverted index list 
		 * which consist of word as a key and an ArrayList of DocInfo for the 
		 * corresponding word which stores the document id and the term frequency 
		 * of the particular word
		 * DocInfo class stores the document id of the document and its the 
		 * corresponding words term frequency in it.
		 */
		LinkedHashMap<String, ArrayList<DocInfo>> ind = new LinkedHashMap<String, ArrayList<DocInfo>>();
		
		/*
		 *  LinkedHashMap for storing the number of tokens in document 
		 *  i.e. corresponding to a particular document id as the key
		 */
		LinkedHashMap<Integer,Integer> dl = new LinkedHashMap<Integer,Integer>();
		
		// call function indexer to create the inverted index list
		// and write the result to index.out file
		indexer(ind,dl,"tccorpus.txt","index.out");
		
		// load the inverted index file into a data structure for further processing
		LinkedHashMap<String, ArrayList<DocInfo>> inverted_index = loadDsFromFile("index.out");
		
		/* calculates the BM25 values for the queries present in queries.txt file
		 * and writes the final result in results.eval file sorted by the score_list
		 * for each document which contains the query */
		calculateBM25(inverted_index,dl,"queries.txt","results.eval",100);
		
	}
	// end of main
}
//end of class