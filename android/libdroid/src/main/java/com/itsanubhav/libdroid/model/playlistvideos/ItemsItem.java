package com.itsanubhav.libdroid.model.playlistvideos;

public class ItemsItem {
	private Snippet snippet;
	private String kind;
	private String etag;
	private String id;
	private ContentDetails contentDetails;

	public void setSnippet(Snippet snippet){
		this.snippet = snippet;
	}

	public Snippet getSnippet(){
		return snippet;
	}

	public void setKind(String kind){
		this.kind = kind;
	}

	public String getKind(){
		return kind;
	}

	public void setEtag(String etag){
		this.etag = etag;
	}

	public String getEtag(){
		return etag;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setContentDetails(ContentDetails contentDetails){
		this.contentDetails = contentDetails;
	}

	public ContentDetails getContentDetails(){
		return contentDetails;
	}

	@Override
 	public String toString(){
		return 
			"ItemsItem{" + 
			"snippet = '" + snippet + '\'' + 
			",kind = '" + kind + '\'' + 
			",etag = '" + etag + '\'' + 
			",id = '" + id + '\'' + 
			",contentDetails = '" + contentDetails + '\'' + 
			"}";
		}
}
