package com.bernard.perceptron;

public class myMatrix {
	
	int numRows;
	int numCols;
	double[][] matrix;
	
	//generates a matrix with the dimensions specified and populates with random values
	public myMatrix(int numRows, int numCols){
		this.numRows = numRows;
		this.numCols = numCols;
		
		matrix = new double[numRows][numCols];
		
		for(int i = 0; i < numRows; i++){
			for(int j = 0; j < numCols-1; j++){
				
				//generate a random number between 0 and 1, multiply this by 0.5 then subtract 0.25.
				//this means that all weights are randomly chosen to be between -0.25 and 0.25
				matrix[i][j] = (Math.random()*.5)-.25;
			}
		}
		//sets the initial weight of the bias == 1
		for(int i = 0; i < numRows; i++){
			matrix[i][numCols-1] = 1;
		}
		
		
	}
	
	
	

	//function for displaying matrix to the screen correctly formatted
	public void display(){
		
		for(int i = 0; i < numRows; i++){
			for(int j = 0; j < numCols; j++){
				//display matrix elements to 4 decimal places (actual precision is greater)
				System.out.printf("%.4f",matrix[i][j]);
				System.out.print(" ");
			}
			System.out.println("\n");
		}
		
	}
	
	
	//standard getters for number of rows, columns and value at particular coordinates
	public int getNumRows(){
		return numRows;
		
	}
	
	public int getNumCols(){
		return numCols;
	}
	
	public double getVal(int i,int j){
		return matrix[i][j];
	}
	
	
	//multiply the weight matrix by input vector
	public int[] computeOutput(int[] input) {
		
		//create temporary storage
		double[] results = new double[numRows];
				
		//go through each row of matrix
		for(int i = 0; i < numRows; i++){
			//go through each column of matrix
			for(int j = 0; j < numCols; j++){
				//find the result of weight*input
				results[i] += (matrix[i][j])*(input[j]);
			}
		}
		
		
		//cleaning the result to be +-1 depending on current value (Implementation of Transfer Function)
		int[]cleaned_results = new int[numRows];
		for(int i = 0; i < numRows; i++){
			if(results[i] >= 0){
				cleaned_results[i] = 1; 
			}else{
				cleaned_results[i] = -1;
			}
		}
		
		return cleaned_results;
	}
	
	//alter weights of a single row given by i
	public void incrementWeights(int rownum, int correctOutput, double learningRate, int[] trainingval) {
		//count adjusted to avoid bias entry
		for(int i = 0; i < numCols-1; i++){
			
			//adjust weight according to equation given in notes
			matrix[rownum][i] = matrix[rownum][i] + learningRate*trainingval[i]*correctOutput;
		}
		//bias has a different calculation so it is handled separately
		
		matrix[rownum][63] = matrix[rownum][63] + learningRate*correctOutput;
				
	}
	

}
