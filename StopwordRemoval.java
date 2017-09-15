//package org.package1;
import java.util.ArrayList;
import java.util.List;

public class StopwordRemoval {
	
	List<String> stopWordList = new ArrayList<String>();
	
	List<String> stopwordList(){
	stopWordList.add("i");
	stopWordList.add("a");
	stopWordList.add("about");
	stopWordList.add("an");
	stopWordList.add("are");
	stopWordList.add("as");
	stopWordList.add("at");
	stopWordList.add("be");
	stopWordList.add("by");
	stopWordList.add("com");
	stopWordList.add("de");
	stopWordList.add("en");
	stopWordList.add("for");
	stopWordList.add("from");
	stopWordList.add("how");
	stopWordList.add("in");
	stopWordList.add("is");
	stopWordList.add("it");
	stopWordList.add("la");
	stopWordList.add("of");
	stopWordList.add("on");
	stopWordList.add("or");
	stopWordList.add("that");
	stopWordList.add("the");
	stopWordList.add("this");
	stopWordList.add("to");
	stopWordList.add("was");
	stopWordList.add("what");
	stopWordList.add("when");
	stopWordList.add("where");
	stopWordList.add("who");
	stopWordList.add("will");
	stopWordList.add("with");
	stopWordList.add("and");
	stopWordList.add("www");
	
	return stopWordList;
	}
}
