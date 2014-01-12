package com.bernard.perceptron;


import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;



public class perceptronmain {
	
	//learning rate of perceptrons
	static double learningRate = .5;
	//location of ocr input data
	static String inputFile = "/Users/bernard/Documents/workspace/perceptron/ocr_data.txt";
	//name of results file to be written
	static String filenum = "30result10.csv";
	 	
	public static void main(String[] args){
		
		boolean	learning = false;
		//storage for training data from file
		ArrayList<int[][]> training_data;
		//get training data from file
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			System.out.println("Please enter the location of the OCR Data ie training data");
			System.out.println("Please note that the full file location and filename is required");
			inputFile = br.readLine();
		} catch (IOException e) {
			System.out.println("An IO Exception Occurred");
			e.printStackTrace();
			System.exit(0);
		}
		training_data = getFileInfo();
		
		
		//create a random weights matrix including the bias term
		myMatrix weightMatrix = new myMatrix(7,64);
		
		//counter used to set max iterations (1000)
		
		int counter = 0;
		//Storage for results
		int[] results = new int[7];
		
		//continue looping while the algorithm is still learning and the number of iterations is < 1000
		while(!learning && counter < 10000){ // 
			//reset learning boolean at beginning of each loop
			learning = true;
			//Counter used to count actual number of iterations
			counter++;
			
			
			
			//for each training data example	
			for(int[][] single_sample :training_data){
			
				//this is the output the product of the weight matrix and input vector should be
				int[] correctoutput = single_sample[1];
				
				//this is the training input for a specific letter
				int[] sample_input = single_sample[0];
				
				//calculate the product (ie the output of each perceptron
				results = weightMatrix.computeOutput(sample_input);
				
		
				for(int i = 0;i < weightMatrix.getNumRows(); i++){
					//compare the calculated output with the expected output
					switch (myCompare(results[i],correctoutput[i])){
					
					//false positive or negative
					case 1:
						weightMatrix.incrementWeights(i,correctoutput[i],learningRate,sample_input);
						learning = false;
						break;
					//same
					case 0:
						//Correct output detected
						break;
						
					default:
						//occurs when the myCompare function fails to work correctly
						System.out.println("Error Detected");
						
					}
					
					
					
				}
			
				
	
				
			}
		}
		
		
			
		//Display final weights matrix	
		System.out.println("Here is the final weights matrix");

		weightMatrix.display();
		
		//Setup to store result file
		PrintWriter resultOutput = createFile("/Users/bernard/Documents/workspace/perceptron/"+filenum);
		
		System.out.println("Outputting Final Result checks");
		
		//recalculating results using final weight matrix
		for(int l = 0; l < 21; l++){
		int[][] test_sample = training_data.get(l);
		int[] testinput = test_sample[0];
		int[] myresults = weightMatrix.computeOutput(testinput);
		
		System.out.println("Here are the results for sampleset " + l);
		for(int i = 0; i < 7; i++){
			if(i < 6){
			System.out.print(myresults[i]+",");
			resultOutput.print(myresults[i]+",");
			}else{
			System.out.print(myresults[i]);
			resultOutput.print(myresults[i]);
			}
		}
		System.out.print("\n");
		resultOutput.print("\n");
		}	
		
		resultOutput.print("\n");
		resultOutput.println("Test Pattern Output");
		resultOutput.print("\n");
		
		//Sample Patterns From Assignment
		String testa_s = "..##.....##......#.....#.##...#.#...#####..#.#.#..#...#.#.#.#.#";
		String testb_s = "######.......#.#....#.##...#.#####..#.#..#.##...#.#....#######.";
		String testc_s = "..#######..#.##......##............#......#.......#..#.#..####.";
		
		
		//using convert function to convert them to form needed
		int[] testa = convert(testa_s);
		int[] testb = convert(testb_s);
		int[] testc = convert(testc_s);
		
		//compute output
		int[] myresults = weightMatrix.computeOutput(testa);
		
		System.out.println("Here are the results for sampleset a which has the following pattern");
		printLetter(testa_s);
		for(int i = 0; i < 7; i++){
			if(i < 6){
			System.out.print(myresults[i] + ",");
			resultOutput.print(myresults[i] + ",");
			}else{
			System.out.print(myresults[i]);	
			resultOutput.print(myresults[i]);
			}
		}

		System.out.print("\n");
		outputLetter(myresults);

		resultOutput.print("\n");
		
		int[] myresults2 = weightMatrix.computeOutput(testb);
		
		System.out.println("Here are the results for sampleset b which has the following pattern");
		printLetter(testb_s);

		for(int i = 0; i < 7; i++){
			if(i < 6){
			System.out.print(myresults2[i] + ",");
			resultOutput.print(myresults2[i] + ",");
			}else{
			System.out.print(myresults2[i]);	
			resultOutput.print(myresults2[i]);
			}
		}
		System.out.print("\n");
		outputLetter(myresults2);
		resultOutput.print("\n");
		int[] myresults3 = weightMatrix.computeOutput(testc);
		
		System.out.println("Here are the results for sampleset c which has the following pattern");
		printLetter(testc_s);

		for(int i = 0; i < 7; i++){
			if(i < 6){
			System.out.print(myresults3[i] + ",");
			resultOutput.print(myresults3[i] + ",");
			}else{
			System.out.print(myresults3[i]);	
			resultOutput.print(myresults3[i]);
			}
		}
		System.out.print("\n");
		outputLetter(myresults3);
		resultOutput.print("\n");
		
		System.out.println("Total Number of Iterations was: " + counter);
		resultOutput.println("Total Number of Iterations was: " + counter);
		
		resultOutput.close();
	
	}
		
	
	
	private static ArrayList<int[][]> getFileInfo(){
		
		//file io code adapted from http://www.newthinktank.com/2012/04/java-video-tutorial-32/
		
		ArrayList<int[][]> file_data = new ArrayList();
		
		File letters = new File(inputFile);
		
		try{
			BufferedReader getInfo = new BufferedReader(new FileReader(letters));
			
			//Reads only one line at a time
			//End of file reached when a null is read
			String ocr_data = getInfo.readLine();
			
			//read in first 21 lines of file ie training data
			for(int i = 0; i < 21; i++){
				
				//get training data from string
				String Sample = ocr_data.substring(0,63);
				int[] Sample_data = new int[64];
				//conver to correct form
				Sample_data = convert(Sample);
				//bias set by default to be positive 1
				Sample_data[63] = 1;
				//get result data from string
				String Remainder = ocr_data.substring(63);
				//convert to correct form
				int[] Correct_Output = convert(Remainder);
				
				//store training data and results in single interger array
				int[][] single_samples = {Sample_data,Correct_Output};
				
				
				//add data to the arraylist
				file_data.add(single_samples);
				ocr_data = getInfo.readLine();
				
			}
		}
		catch(FileNotFoundException e){
			System.out.println("The Training Data Could Not Be Found at the location Specified");
			System.exit(0);
		}
		catch(IOException e){
			System.out.println("A File IO Exception occured");
				System.exit(0);
			
		}
		return file_data;
				
	}
	
	//converts the string to the correct form ie -1 or +1
	private static int[] convert(String sample) {
		int len = sample.length();
		int[] converted = new int[64];
		for(int i = 0; i < len; i++){
			if(sample.charAt(i) == '.'){
				converted[i] = -1;
			}else{
				converted[i] = 1;
			}
		}
		
		return converted;
	}

	
	//comparison function: returns +1  different otherwise returns 0
	public static int myCompare(int computed,int actual){
		if(computed == actual){
			return 0;
		}else{
			return 1;
		}
		
	}
	
	//new printwriter for writing output to file
	private static PrintWriter createFile(String fileName){
		try{
			File outputFile = new File(fileName);
			
			PrintWriter infoToWrite = new PrintWriter(
					new BufferedWriter(
							new FileWriter(outputFile)));
			return infoToWrite;
		}
		catch(IOException e){
			System.out.println("An IO Error Occurred");
			System.exit(0);
		}
		return null;
	}
	
	private static void printLetter(String pattern){
		for(int i = 0; i < 63; i++){
			System.out.print(pattern.charAt(i));
			if((i+ 1) % 7 == 0){
				//makes sure the putput is formatted correctly
				System.out.print("\n");
			}
		}
	}
	
	private static void outputLetter(int[] results){
		for(int i = 0; i < 7; i++){
		
			if(results[i] == 1){
		switch (i){
		
		//false positive or negative
		case 0:
			System.out.println("The pattern input is the letter A");
			break;
		
		case 1:
			System.out.println("The pattern input is the letter B");
			break;
			
		case 2:
			System.out.println("The pattern input is the letter C");
			break;
			
		case 3:
			System.out.println("The pattern input is the letter D");
			break;
			
		case 4:
			System.out.println("The pattern input is the letter E");
			break;
			
		case 5:
			System.out.println("The pattern input is the letter J");
			break;
			
		case 6:
			System.out.println("The pattern input is the letter K");
			break;
		
			
		default:
			//occurs when the function fails to work correctly 
			System.out.println("Error Detected");
			
		}
			}
		}
		
	}

}//end of class
