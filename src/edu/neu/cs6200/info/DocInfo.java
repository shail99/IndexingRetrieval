package edu.neu.cs6200.info;

public class DocInfo {
	private int docId;
	private int term_freq;
	
	public DocInfo()
	{
		docId = 0;
		term_freq = 0;
	}
	
	public DocInfo(int docId)
	{
		this.docId = docId;
		this.term_freq = 0;
	}
	
	public DocInfo(int docId, int term_freq)
	{
		this.docId = docId;
		this.term_freq = term_freq;
	}
	
	public int getDocId() {
		return docId;
	}
	public void setDocId(int docId) {
		this.docId = docId;
	}
	public int getTerm_freq() {
		return term_freq;
	}
	public void setTerm_freq(int term_freq) {
		this.term_freq = term_freq;
	}
	public void incrementTermFreq()
	{
		this.term_freq++ ;
	}
	
	public String toString()
	{
		return "["+docId+","+term_freq+"]";
	}

}
