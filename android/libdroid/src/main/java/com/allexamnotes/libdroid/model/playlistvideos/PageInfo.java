package com.allexamnotes.libdroid.model.playlistvideos;

public class PageInfo{
	private int totalResults;
	private int resultsPerPage;

	public void setTotalResults(int totalResults){
		this.totalResults = totalResults;
	}

	public int getTotalResults(){
		return totalResults;
	}

	public void setResultsPerPage(int resultsPerPage){
		this.resultsPerPage = resultsPerPage;
	}

	public int getResultsPerPage(){
		return resultsPerPage;
	}

	@Override
 	public String toString(){
		return 
			"PageInfo{" + 
			"totalResults = '" + totalResults + '\'' + 
			",resultsPerPage = '" + resultsPerPage + '\'' + 
			"}";
		}
}
