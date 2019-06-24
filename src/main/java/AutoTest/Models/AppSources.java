package AutoTest.Models;

import java.util.ArrayList;

public class AppSources {
	//存储已经遍历到的PageSource
	private ArrayList<PageSource> sources;

	public AppSources() {
		this.sources = new ArrayList<PageSource>();
	}

	public ArrayList<PageSource> getSources() {
		return sources;
	}

	public void setSources(ArrayList<PageSource> sources) {
		this.sources = sources;
	}
	
}
