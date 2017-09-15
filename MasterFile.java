//package org.package1;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
//import com.google.common.collect;
import java.util.TreeMap;

public class MasterFile {

	static long startingTime = 0;
	static long endTime = 0;
	static int  fileCount = 0;

	public File TokenizeInput(File fileName, String outputFileName, int documentID) throws IOException {

		File outputFile = new File(outputFileName);
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
		LineNumberReader lr = null;
		long tokenCount = 0l;
		// ArrayList for removing stopwords
		List<String> stopWordList = new ArrayList<String>();
		StopwordRemoval stopWordRemoval = new StopwordRemoval();
		stopWordList = stopWordRemoval.stopwordList();
				
		String nextLine = null;
		try {
			
			fileCount = fileCount + 1;
			
			lr = new LineNumberReader(new FileReader(fileName));
			while (((nextLine = lr.readLine()) != null) ) {
				if(!nextLine.contains("<!--")){
				
				if((nextLine.contains("<script>")) && (!nextLine.contains("</script>"))){				
					 while(!((nextLine = lr.readLine()).contains("</script>"))){
						
						 continue;
					}
					
				}
				
				String tokenizedLine = null;
				
						
				tokenizedLine = nextLine.replaceAll("\\<script>.*</script>","").replaceAll("\\<.*?>", "").replaceAll("[{},)(;/]", "");
			
				if ((!tokenizedLine.equalsIgnoreCase("")) && (tokenizedLine!=null) && (!tokenizedLine.isEmpty())  && (tokenizedLine.length() > 0)) {
					
					String lineSplit[] = tokenizedLine.split(" ");
					for (String token : lineSplit) {
						if (!token.isEmpty() && token.length() != 0) {
							String newToken = token.trim().toLowerCase();
							if (!stopWordList.contains(newToken)) {
								writer.write(newToken);
								writer.write(" ");

								tokenCount++;
							}
						}

					}writer.newLine();
				}
							//}
			}
			} // while eof check bracket
			

			
			writer.flush();
		} catch (FileNotFoundException e) {
			System.out.println("File not found exception " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO Exception because " + e.getMessage());
		} finally {
			lr.close();
			writer.close();
		}
			return outputFile;
	}
    
	/*
	 * Method to retrieve Title and return it back
	 */
	public String TrimTitle(File fileName,int documentID ){
		LineNumberReader lrt = null;
		String nextLine = null;
		String title[]=null;
		String title1[]=null;
		HashMap<Integer, String> titleHashMap = new HashMap<>();		
		try {
			lrt = new LineNumberReader(new FileReader(fileName));
			try{
			while ((nextLine = lrt.readLine()) != null) {
				
				if(nextLine.contains("<title>")){
					title = nextLine.split("<title>");					
					title1 = title[1].split("</title>");
					break;
				}
			}
			
		} catch (IOException e) {
			//e.printStackTrace();
		}
		
		return title1[0];	
		}catch(Exception e){
			System.out.println("Inside Exception :" +  e.getMessage());
			return "BBC News";
		}
	}//
	public String trimURL(File fileNamem, int documentID){
		
		LineNumberReader lrtu = null;
		String nextLine = null;
		String urll = null;
		
		try{
			lrtu = new LineNumberReader(new FileReader(fileNamem));
			while((nextLine = lrtu.readLine()) !=null){
				if(nextLine.contains("canonical")){
				urll = nextLine.substring(28, nextLine.length()-4);
				
				}
			}
		}catch (IOException e) {
			System.out.println("Inside catch");
			return "http://www.bbcnews.com";
			}
		return urll;
		
	}
	//
	public static ArrayList<HashMap<String, String>> main(String[] args, String index, String relevanceFB, String functiond) throws IOException {
		
		
		String buildIndex =null;	
		String inputQuery = null;
		inputQuery = args[0];
		String function = functiond;
		String relevanceFB1 = relevanceFB;	
		buildIndex = index ;
		
		System.out.println("Point 1");
		startingTime = System.currentTimeMillis();
		String folderName = "C:\\Users\\shaha\\Desktop\\IR1";
		String outputFileName = "C:\\Users\\shaha\\Desktop\\outputFile.txt";
		File folder = new File(folderName);
		File[] filesArray = folder.listFiles();
		MasterFile mf = new MasterFile();
		VectorSpaceModel vsm = new VectorSpaceModel();
		Stemmer1 s1 = new Stemmer1();
		int documentID = 1;
		File outputFile = null;
		HashMap<Integer, ArrayList<Integer>> termDocumentMap = null;
		HashMap<String, HashMap<Integer, ArrayList<Integer>>> hashMap = new HashMap<String, HashMap<Integer, ArrayList<Integer>>>();
		HashMap<Integer, String> titleHashmap = new HashMap<>();
		HashMap<Integer, String> urlHashMap = new HashMap<>();
		HashMap<String, String> titleFileDataMap = new HashMap<>();
		LinkedHashMap<String, Double> TitleValueHashMap = new LinkedHashMap<>();
		LinkedHashMap<String, ArrayList<String>> TitleFinalData = new LinkedHashMap<>();
		
		
		if(relevanceFB1.equalsIgnoreCase("false"))
		{
		
		if(buildIndex.equalsIgnoreCase("yes"))
		{
			for (File file : filesArray) {
			StringBuffer sb = new StringBuffer();
			
			String title = mf.TrimTitle(file, documentID);
			String url = mf.trimURL(file, documentID);
			urlHashMap.put(documentID, url);
			titleHashmap.put(documentID, title);
			outputFile = mf.TokenizeInput(file, outputFileName, documentID);	
			FileReader in = new FileReader(outputFile);
			LineNumberReader lnr3 = new LineNumberReader(in);
			String line1 = null;
			
			while ((line1 = lnr3.readLine())!= null){
				sb.append(line1);
				sb.append(" ");
			}
			
			String fileData1 = sb.toString();
			titleFileDataMap.put(title, fileData1);
			
			ArrayList<String> afterStemData = s1.stemmerMethod(outputFile);
			
			
			termDocumentMap = new HashMap<Integer, ArrayList<Integer>>();
			int count = 1;
			for (String term : afterStemData) {
				
				if (hashMap.containsKey(term)) {
					
					termDocumentMap = hashMap.get(term);
					
					if (termDocumentMap.keySet().contains(documentID)) {
						ArrayList<Integer> al = termDocumentMap.get(documentID);
						al.add(count);
						termDocumentMap.remove(documentID);
						termDocumentMap.put(documentID, al);
						hashMap.remove(term);
						hashMap.put(term, termDocumentMap);
						
					} else {
						
						int j = 1;
						ArrayList<Integer> al1 = new ArrayList<Integer>();
						al1.add(count);
						termDocumentMap.put(documentID, al1);
						hashMap.remove(term);
						hashMap.put(term, termDocumentMap);
					}
				} else {
					
					ArrayList<Integer> alInt = new ArrayList<Integer>();
					alInt.add(count);
					termDocumentMap = new HashMap<Integer, ArrayList<Integer>>();
					termDocumentMap.put(documentID, alInt);
					hashMap.put(term, termDocumentMap);
					}
				count = count + 1;
			}
			documentID++;
		}   //Inverted Index Building End
		
				
		//Saving Title File Data Hashmap
		FileOutputStream fosFileData = new FileOutputStream("C:\\Users\\shaha\\Desktop\\fileData.txt");
		ObjectOutputStream oosFileData = new ObjectOutputStream(fosFileData);
		oosFileData.writeObject(titleFileDataMap);
		oosFileData.flush();
		oosFileData.close();
		//Saving Inverted Index
		FileOutputStream fos = new FileOutputStream("C:\\Users\\shaha\\Desktop\\hashMap.txt");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(hashMap);
		oos.flush();
		oos.close();
		FileOutputStream fosTitle = new FileOutputStream("C:\\Users\\shaha\\Desktop\\titleHashMap.txt");
		ObjectOutputStream oosTitle = new ObjectOutputStream(fosTitle);
		oosTitle.writeObject(titleHashmap);
		oosTitle.flush();
		oosTitle.close();
		FileOutputStream fosUrl = new FileOutputStream("C:\\Users\\shaha\\Desktop\\urlHashMap.txt");
		ObjectOutputStream oosUrl = new ObjectOutputStream(fosUrl);
		oosUrl.writeObject(urlHashMap);
		oosUrl.flush();
		oosUrl.close();
		
	    }
		else{
        String hashMapfileName = "C:\\Users\\shaha\\Desktop\\hashMap.txt";
        String titleHashMapFileName = "C:\\Users\\shaha\\Desktop\\titleHashMap.txt";
        String titleFileData = "C:\\Users\\shaha\\Desktop\\fileData.txt";
        String urlHashMapName =  "C:\\Users\\shaha\\Desktop\\urlHashMap.txt";
        //
        String folderName3 = "C:\\Users\\shaha\\Desktop\\IR1";
        File folder3 = new File(folderName3);
        fileCount = folder3.list().length;
		FileInputStream fis = new FileInputStream(hashMapfileName);
		ObjectInputStream ois = new ObjectInputStream(fis);	
		FileInputStream fisTitle = new FileInputStream(titleHashMapFileName);
		ObjectInputStream oisTitle = new ObjectInputStream(fisTitle);
		FileInputStream fisFileData = new FileInputStream(titleFileData);
		ObjectInputStream oisFileData = new ObjectInputStream(fisFileData);
		FileInputStream fisUrlData = new FileInputStream(urlHashMapName);
		ObjectInputStream oisUrlData = new ObjectInputStream(fisUrlData);
		try {
			hashMap = (HashMap<String, HashMap<Integer, ArrayList<Integer>>>) ois.readObject();
			titleHashmap =  (HashMap<Integer, String>)oisTitle.readObject();
			titleFileDataMap = (HashMap<String, String>)oisFileData.readObject();
			urlHashMap = (HashMap<Integer, String>)oisUrlData.readObject();
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Class Not found Exception ::" + e.getMessage());
			//e.printStackTrace();
		}
		}
	    
		
		vsm.termDocumentMatrix(hashMap, fileCount,buildIndex);
	
		}  // Relevance Fb Check
		else if(relevanceFB.equalsIgnoreCase("yes")){
			
			 String hashMapfileName = "C:\\Users\\shaha\\Desktop\\hashMap.txt";
		     String titleHashMapFileName = "C:\\Users\\shaha\\Desktop\\titleHashMap.txt";
		     String fileDataMapFname = "C:\\Users\\shaha\\Desktop\\fileData.txt";
		     String urlHashMapName =  "C:\\Users\\shaha\\Desktop\\urlHashMap.txt";
		     
		     //fileCount = 91;
		     String folderName3 = "C:\\Users\\shaha\\Desktop\\IR1";
		     File folder3 = new File(folderName3);
		     fileCount = folder3.list().length;
			 FileInputStream fis = new FileInputStream(hashMapfileName);
			 ObjectInputStream ois = new ObjectInputStream(fis);	
			 FileInputStream fisTitle = new FileInputStream(titleHashMapFileName);
			 ObjectInputStream oisTitle = new ObjectInputStream(fisTitle);
			 FileInputStream fisFileData = new FileInputStream(fileDataMapFname);
			 ObjectInputStream oisFileData = new ObjectInputStream(fisFileData);
			 FileInputStream fisUrlData = new FileInputStream(urlHashMapName);
			 ObjectInputStream oisUrlData = new ObjectInputStream(fisUrlData);
				try {
					hashMap = (HashMap<String, HashMap<Integer, ArrayList<Integer>>>) ois.readObject();
					titleHashmap =  (HashMap<Integer, String>)oisTitle.readObject();
					titleFileDataMap = (HashMap<String, String>) oisFileData.readObject();
					urlHashMap = (HashMap<Integer, String>)oisUrlData.readObject();
					System.out.println("Hashmap size 1 " + hashMap.size());
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					System.out.println("Class Not found Exception ::" + e.getMessage());
					//e.printStackTrace();
				}
				
				StringBuffer sb1 = new StringBuffer();
				sb1.append(args[0]);
				sb1.append(" ");
				for(int i =1; i <args.length; i++){
					sb1.append(titleFileDataMap.get(args[i]));
					sb1.append(" ");
				}
			    inputQuery = sb1.toString();
			    }
				
				
		ArrayList<HashMap<String, String>> jsonList1 = new ArrayList<>();
		HashMap<String, String> jsonMap1 ;
		System.out.println("Inside Vecotor Space check");
		HashMap<Integer, Double> resultScoreMap = vsm.executeQuery(hashMap,inputQuery,fileCount);
		
		MapSort ms = new MapSort();
		HashMap<Integer, Double> sortedHashMap = ms.MapSort1(resultScoreMap);
		
			
		for( int value1 : sortedHashMap.keySet()){
			ArrayList<String> arrayList = new ArrayList<>();
			arrayList.add(sortedHashMap.get(value1).toString());
			try{
			if(!urlHashMap.get(value1).toString().isEmpty()){
			arrayList.add(urlHashMap.get(value1).toString());
			}else{
				arrayList.add(" ");
			}
			}catch(Exception e){
				arrayList.add("http://www.bbcnews.com");
			}
			String key1 = titleHashmap.get(value1);
			String data = titleFileDataMap.get(key1).trim();
			String data1 = null;
			StringBuffer sbf = new StringBuffer();
			int cnt =1;
			for(String d1 : data.split(" ")){
				cnt++;
				if(!d1.equalsIgnoreCase(" ") && !d1.isEmpty() && d1!=null && cnt  < 100 ){
					cnt++;
				sbf.append(d1.trim().replaceAll(".,", ""));
				sbf.append(" ");
				}			
			}
			
			arrayList.add(sbf.toString());
					
			TitleValueHashMap.put(titleHashmap.get(value1), sortedHashMap.get(value1));
			TitleFinalData.put(titleHashmap.get(value1), arrayList);
			
			//
			//Json stuff
			jsonMap1 = new HashMap<String,String>();
			jsonMap1.put("Score", sortedHashMap.get(value1).toString());
			jsonMap1.put("title", titleHashmap.get(value1).toString());
			try{
			jsonMap1.put("url", urlHashMap.get(value1).toString());
			}catch(Exception e){
				jsonMap1.put("url", "http\\www.bbcnews.com");
			}
			jsonMap1.put("data", sbf.toString());
			jsonList1.add(jsonMap1);
			
						
		}
		System.out.println(" json list 1" + jsonList1);
			
		/////////
		ArrayList<HashMap<String, String>> jsonList2 = new ArrayList<>();
		HashMap<String, String> jsonMap2 ;
		//Term Proximity Check
		if(!relevanceFB.equalsIgnoreCase("yes")){
			System.out.println("Inside Proximity Check");
			HashMap<Integer, Double> hashMapTermProx = mf.termProximity(hashMap,inputQuery,fileCount);
			
			MapSort ms1 = new MapSort();
			HashMap<Integer, Double> sortedHashMapT = ms1.MapSort1(hashMapTermProx);
			for( int value1 : sortedHashMapT.keySet()){
				ArrayList<String> arrayList = new ArrayList<>();
				arrayList.add(sortedHashMapT.get(value1).toString());
				String key1 = titleHashmap.get(value1);
				String data = titleFileDataMap.get(key1).trim();
				String data1 = null;
				StringBuffer sbf = new StringBuffer();
				int cnt =1;
				for(String d1 : data.split(" ")){
					cnt++;
					if(!d1.equalsIgnoreCase(" ") && !d1.isEmpty() && d1!=null && cnt  < 100 ){
						cnt++;
					sbf.append(d1.trim().replaceAll(".,", ""));
					sbf.append(" ");
					}			
				}
				
				arrayList.add(sbf.toString());
						
				TitleValueHashMap.put(titleHashmap.get(value1), sortedHashMapT.get(value1));
				TitleFinalData.put(titleHashmap.get(value1), arrayList);
				
				//Json stuff
				jsonMap2 = new HashMap<String,String>();
				jsonMap2.put("Score", sortedHashMapT.get(value1).toString());
				jsonMap2.put("title", titleHashmap.get(value1).toString());
				try{
				jsonMap2.put("url", urlHashMap.get(value1).toString());
				}catch(Exception e){
				jsonMap2.put("url", "http://www.bbcnews.com");
				}
				jsonMap2.put("data", sbf.toString());
				jsonList2.add(jsonMap2);
				
				///
					
			}
			
			
			/// json stuff
			
	//	}// term Proximity check
		//Summing both term proximity n cosine similarity
		
		ArrayList<HashMap<String, String>> finalArrayList = new ArrayList<>();
		System.out.println("Summinig up...");
		HashMap<String, String> finalHashMap = new HashMap<>();
		for(HashMap<String, String> hsmp : jsonList1){
		String ttle = hsmp.get("title");
		System.out.println("Title is " + ttle);
		double valuee1 = Double.parseDouble(hsmp.get("Score"));
		System.out.println("Double value 1 " + valuee1);
		double valuee2=0;
		for(HashMap<String, String> hsmp1 : jsonList2){
			if(hsmp1.get("title").equalsIgnoreCase(ttle))
			{
				valuee2 = Double.parseDouble(hsmp1.get("Score")); 
				System.out.println("value 2 is " + valuee2);
			}
		}
		//double valuee2 = Double.parseDouble(jsonMap2.get("Score"));
		double combScore = (valuee1 + valuee2);
		System.out.println("Comb score " + combScore);
		String combscore = String.valueOf(combScore);
		finalHashMap = new HashMap<String,String>();
		finalHashMap.put("Score", combscore);
		finalHashMap.put("title", ttle);
		finalHashMap.put("url",hsmp.get("url"));
		finalHashMap.put("data", hsmp.get("data"));
		finalArrayList.add(finalHashMap);
		
		System.out.println("Final json list" + finalArrayList);
		
		}
		return finalArrayList;
	}else{
		return jsonList1;
	}
		
	}
	HashMap<Integer, Double> termProximity(HashMap<String, HashMap<Integer, ArrayList<Integer>>> hashMap, String inputQuery, int fileCount){
		  String[] Query = inputQuery.split(" ");
			String outputFileName1 = "C:\\Users\\shaha\\Desktop\\outputFile1.txt";
			File oFile = new File(outputFileName1);
			FileWriter fw;
			try {
				fw = new FileWriter(outputFileName1);
				BufferedWriter writer = new BufferedWriter(fw);
				List<String> stopWordList1 = new ArrayList<String>();
				StopwordRemoval stopWordRemoval = new StopwordRemoval();
				stopWordList1 = stopWordRemoval.stopwordList();
				for(String queryTokens : Query){
					if(!stopWordList1.contains(queryTokens)){
					writer.write(queryTokens);
					writer.write(" ");
					}
				}
				writer.flush();
				} catch (IOException e) {
				
				}
			Stemmer1 sm1 = new Stemmer1();
			HashMap<Integer, Double> hhMap = new HashMap<>();
			ArrayList<String> afterStemData1 = sm1.stemmerMethod(oFile);
			  
			ArrayList <String> QuerySet = new ArrayList<String>();
			for(String data: afterStemData1){
				QuerySet.add(data);
			}
			if(QuerySet.size()> 1){
			
				double finalValue3 = 0;
				double value1 =0;
				double value2 =0;
				double minValue;
				double newValue =0;
						for(int i=1;i < fileCount+1 ; i++){
							
							for(int q=0;q<QuerySet.size()-1;q++)
							{
							
							if((hashMap.get(QuerySet.get(q)).keySet().contains(i)) && (hashMap.get(QuerySet.get(q+1)).keySet().contains(i))){
								
								for(int j=0; j < hashMap.get(QuerySet.get(q)).get(i).size(); j++){
									
								value1 = hashMap.get(QuerySet.get(q)).get(i).get(j);
								for(int k=0; k < hashMap.get(QuerySet.get(q+1)).get(i).size(); k++){
									value2 = hashMap.get(QuerySet.get(q+1)).get(i).get(k);
									if(value1 > value2){
										finalValue3 = 1/(value1-value2);
										}else if(value2 > value1){
										finalValue3 = 1/(value2 - value1);
										
										}
									    if(k == 0 && j==0){
									  
									    	newValue = finalValue3;
									    }
							      		
								        newValue = newValue + finalValue3 ;
																        	
								}
								
								}
								 
								hhMap.put(i, newValue);
								
							}
							
							}//query set loop
							
						}// Document Loop
						
				
			}else{
				System.out.println("Query Set is less than 2");
			}
		
			return hhMap;
	  }

}
