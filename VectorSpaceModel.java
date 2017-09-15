//package org.package1;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class VectorSpaceModel {
	
	ArrayList<String> allElements = new ArrayList<String>();
	Double[][] array = null;
	Stemmer1 sm1 = new Stemmer1();
	ArrayList<String> stopWords = new ArrayList<String>();
	StopwordRemoval stopWordRemoval1 = new StopwordRemoval();
	
	public void termDocumentMatrix(HashMap<String, HashMap<Integer, ArrayList<Integer>>> hashMap,int fileCount1, String buildIndex){
				
		//Copying Hashmap key terms into arraylist which helps as the row value in the term document Matrix
		for(String term : hashMap.keySet()){
			allElements.add(term);
			}
		
		//If Inverted Index build value is True
		if(buildIndex.equalsIgnoreCase("yes"))
		{				
		array = new Double[hashMap.size()][fileCount1 + 1];
		double fileCount = fileCount1;				
		Iterator<String> iterator = allElements.iterator();
		
		//Reading each term in (Hashmap or arraylist)
		while(iterator.hasNext()){	
		String term = iterator.next();
		hashMap.get(term).keySet();
		for(int i : hashMap.get(term).keySet()){
			// Reading Document  Number for each term
			int row = allElements.indexOf(term);
			int column = i;
			int value = hashMap.get(term).get(i).size();
			double ndf = (fileCount / (hashMap.get(term).size()));
			double tfIDfValue = value * Math.log10(ndf);
			array[row][column] = tfIDfValue;
			
		}
		}
		
		try {
			FileOutputStream fosMatrix = new FileOutputStream("C:\\Users\\shaha\\Desktop\\vsmMatrix.txt");
			ObjectOutputStream oosMatrix = new ObjectOutputStream(fosMatrix);
			oosMatrix.writeObject(array);
			oosMatrix.flush();
			oosMatrix.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Inside Writing Matrix to file :: " + e.getMessage());
			e.printStackTrace();
		}
		
	}else{
		    FileInputStream fisMatrix;
			try {
				fisMatrix = new FileInputStream("C:\\Users\\shaha\\Desktop\\vsmMatrix.txt");
				  ObjectInputStream oisMatrix = new ObjectInputStream(fisMatrix);	
				  array = (Double[][])oisMatrix.readObject();
				  } catch (FileNotFoundException e) {
				System.out.println("FileNotFoundException  " + e.getMessage());
				//e.printStackTrace();
			} catch (IOException e) {
				System.out.println("IOException  " + e.getMessage());
				//e.printStackTrace();
			} catch (ClassNotFoundException e) {
				//e.printStackTrace();
				System.out.println("ClassNotFoundException  " + e.getMessage());
			}
		  
		
	}	
	}
	/*
	 * Execute Query Method 
	 * Input Parameters : HashMap : Hashmap of all terms and their tf values, 
	 *                    Input Query
	 *                    File Count 
	 *                    
	 * Returns          : Hashmap which contains Document Number and its Cosine Similarity Value
	 */
	public HashMap<Integer, Double> executeQuery(HashMap<String, HashMap<Integer, ArrayList<Integer>>> hashMap,String inputQuery, int fileCount1){
		//System.out.println("Input Query is  " + inputQuery);
		HashMap<Integer, Double> scoreHashMap = new HashMap<Integer,Double>();
		HashMap<String, Long> queryTokensMap = new HashMap<>();
		HashMap<String, Double> queryTokensTFIDFMap = new HashMap<>();
		for(String term : hashMap.keySet()){
			allElements.add(term);
			}
		
		try {
			FileInputStream fisMatrix;
			fisMatrix = new FileInputStream("C:\\Users\\shaha\\Desktop\\vsmMatrix.txt");
			  ObjectInputStream oisMatrix = new ObjectInputStream(fisMatrix);	
			  array = (Double[][])oisMatrix.readObject();
			 } catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException  " + e.getMessage());
			//e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOException  " + e.getMessage());
			//e.printStackTrace();
		} catch (ClassNotFoundException e) {
			//e.printStackTrace();
			System.out.println("ClassNotFoundException  " + e.getMessage());
		}
		
		double fileCount = fileCount1;
	
		//Splittin the Input Query on Space and writing the query into a file which will be sent to the stemmer.
		String[] Query = inputQuery.split(" ");
		String outputFileName1 = "C:\\Users\\shaha\\Desktop\\outputFile1.txt";
		File oFile = new File(outputFileName1);
		FileWriter fw;
		try {
			fw = new FileWriter(outputFileName1);
			BufferedWriter writer = new BufferedWriter(fw);
			for(String queryTokens : Query){
				writer.write(queryTokens);
				writer.write(" ");
			}
			writer.flush();
			} catch (IOException e) {
			System.out.println("Exception in VSM execute query :: " + e.getMessage());
			//e.printStackTrace();
		}
		
		ArrayList<String> afterStemData1 = sm1.stemmerMethod(oFile);
		
		
		//Constructing query token hashmap
		for(String queryToken : afterStemData1){
			if(queryTokensMap.containsKey(queryToken)){
				long queryTokenCount = queryTokensMap.get(queryToken);
				queryTokenCount++;
				queryTokensMap.remove(queryToken);
				queryTokensMap.put(queryToken, queryTokenCount);
			}else{
				queryTokensMap.put(queryToken,1l);
			}
		}
			
		for(String queryToken : queryTokensMap.keySet()){
			
			long tfValue = queryTokensMap.get(queryToken);
			//System.out.println("TF value is " + tfValue);
			if(hashMap.containsKey(queryToken)){
			double tfIdf = tfValue * Math.log10(fileCount / hashMap.get(queryToken).size());
				queryTokensTFIDFMap.put(queryToken, tfIdf );	
			}
		}
			
		for(int i=1;i < fileCount+1 ; i++){
			double modQuery = 0l;
			double modDoc = 0l;
		double value = 0;	
		for(String queryToken : queryTokensTFIDFMap.keySet()){
			
			if(hashMap.get(queryToken).keySet().contains(i))
			{
			 modQuery = 0l;
			 modDoc =0l;
		
			if(hashMap.containsKey(queryToken)){
				int row = allElements.indexOf(queryToken);	
				
				if(array[row][i] != null){
					
					 for(String queryToken3 : queryTokensTFIDFMap.keySet() ){
						modQuery = modQuery +  queryTokensTFIDFMap.get(queryToken3) * queryTokensTFIDFMap.get(queryToken3);
					
					 }
					 modQuery  = Math.sqrt((double)modQuery);
					 
					 for(int row1 = 0 ; row1 < hashMap.size(); row1++){
						 if(array[row1][i] != null){
						 modDoc = modDoc + (array[row1][i] * array[row1][i]);
						 
						 }
					 }
					 modDoc = Math.sqrt(modDoc);			 
					 value = value + (array[row][i]* (queryTokensTFIDFMap.get(queryToken)));	
				 }else{
					 for(String queryToken3 : queryTokensTFIDFMap.keySet() ){
							modQuery = modQuery +  queryTokensTFIDFMap.get(queryToken3) * queryTokensTFIDFMap.get(queryToken3);
							
						 }
						 modQuery  = Math.sqrt((double)modQuery);
						 
						 for(int row1 = 0 ; row1 < hashMap.size(); row1++){
							 if(array[row1][i] != null){
							 modDoc = modDoc + (array[row1][i] * array[row1][i]);
							
							 }
						 }
						 modDoc = Math.sqrt(modDoc);
						 value = value + ( 0 * (queryTokensTFIDFMap.get(queryToken)));	
					 
				 }
				
			}
			}// Checking candidate Documents if
		} //calculating cos similarity
		if(value > 0){				  
		value = value / (modQuery * modDoc);
		}else{
			value = 0;
		}
		          
		if(value > 0){
		scoreHashMap.put(i, value);
		}
			
	}
		return scoreHashMap;
		}

}
