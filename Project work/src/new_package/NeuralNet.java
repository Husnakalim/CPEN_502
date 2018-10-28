package new_package;

//imported utilities
import java.io.File;
import java.io.IOException;
import java.util.Random;




public class NeuralNet implements NeuralNetInterface {
	
	//size of Network; neurons and inputs
	static int NUM_HIDDEN_NEURONS = 16;
	static int NUM_INPUTS = 16;
	static int NUM_OUTPUTS = 1;
	
	//threshold constants
	static int MAX_EPOCH = 10000;
	static double MAX_ERROR = 0.05;
	
	
	//Private variables
	private int argNumInputs = 3; // +1 for bias term
	public int argNumHidden = 5; // +1 for bias term
	private double argLearningRate = 0.2;
	private double argMomentumTerm = 0.0;
	private double argA = -0.5;
	private double argB = 0.5;
	
	//variables for determining interval for random initialization of weights
	private double minInitWeight = argA;
	private double maxInitWeight = argB;
	
	//variables to store constants 
	private int numHiddenNeurons = NUM_HIDDEN_NEURONS; /*Added to clean out code*/
	private int numInputs = NUM_INPUTS;
	private int numOutputs = NUM_OUTPUTS;
	
	//arrays to store inputs and weights
	
	public double[][] weightsHiddenIn = new double[argNumInputs][argNumHidden];
	public double[][] weightsHiddenOut = new double[argNumHidden][numOutputs]; /*consider one 3d matrix for all weights to generalize*/
	
	
	//delta arrays
	public double[] deltaOutput = new double[numOutputs];
	public double[] deltaHiddenOutput = new double[argNumHidden];
	
	public double[][] deltaWeightsHiddenIn = new double[argNumInputs][argNumHidden];
	public double[][] deltaWeightsHiddenOut = new double[argNumHidden][numOutputs];
	
	//sum of product of weights and input, S
	
	public double[] hiddenS = new double[argNumHidden]; 
	public double[] outputs = new double[numOutputs];
	
	//training set variables (true output)
	public int numTrainingSets = 4; //SET THIS MANUALLY WHILE UNFINISHED CODE
	public double[][] inputValues = new double[numTrainingSets][argNumInputs];
	public double[] C = new double[numTrainingSets]; //fix
	public int currTrainingSet = 0; 
	
	//error data
	public double[] totalError = new double[MAX_EPOCH];
	public double[] singleError = new double[MAX_EPOCH];
	
	
	///////////////////
	//constructor
	
	/**
	* Constructor. (Cannot be declared in an interface, but your implementation will need one)
	* * @param argNumInputs The number of inputs in your input vector
	* @param argNumHidden The number of hidden neurons in your hidden layer. Only a single hidden layer is supported
	* @param argLearningRate The learning rate coefficient
	* @param argMomentumTerm The momentum coefficient
	* @param argA Integer lower bound of sigmoid used by the output neuron only.
	* @param arbB Integer upper bound of sigmoid used by the output neuron only.

	public abstract NeuralNet (
	int argNumInputs,
	int argNumHidden,
	double argLearningRate,
	double argMomentumTerm,
	double argA,
	double argB );
	*/
	public NeuralNet(int argNumInputs, int argNumHidden,
			double argLearningRate, double argMomentumTerm,
			double argA, double argB) 
	{
		this.argNumInputs = argNumInputs;
		this.argNumHidden = argNumHidden;
		this.argLearningRate = argLearningRate;
		this.argMomentumTerm = argMomentumTerm;
		this.argA = argA;
		this.argB = argB;
		
	}
	
	// standard bipolar sigmoid function
	public double sigmoid(double x) {
		return 2 / (1 + Math.exp(-x)) - 1; /*why 2? bipolar*/
	}
	
	//custom sigmoid function
	public double customSigmoid(double x) {
		double a = 0;
		double b = 1;
		
		return (b - a) / (1 + Math.pow(Math.E, -x)) + a; /*Check this*/
	}
	
	
	//initializing both weight sets to random values
	public void initializeWeights() {
		
		for (int i = 0; i < argNumInputs; i++) {
			for (int j = 1; j < argNumHidden; j++) { //unchanged bias node
				double r = new Random().nextDouble();
				
				weightsHiddenIn[i][j] = minInitWeight + (r * (maxInitWeight - minInitWeight));
				//weightsHiddenIn[i][j] = Math.random() - 0.5;
				deltaWeightsHiddenIn[i][j] = 0.0;
			}
		}
		
		for (int i = 0; i < argNumHidden; i++) {
			for (int j = 0; j < numOutputs; j++) {
				double r = new Random().nextDouble();
				
				weightsHiddenOut[i][j] = minInitWeight + (r * (maxInitWeight - minInitWeight));
				//weightsHiddenOut[i][j] = Math.random() - 0.5;
				deltaWeightsHiddenOut[i][j] = 0.0;
			}
		}
		
	}
	
	//initializing both weight sets to zero
	public void zeroWeights() {
		
		for (int i = 0; i < numHiddenNeurons; i++) {
			for (int j = 0; j < numInputs; j++) {
				weightsHiddenIn[i][j] = 0.0;
				
			}
	}
		for (int i = 0; i < numOutputs; i++) {
			for (int j = 0; j < numHiddenNeurons; j++) {
				weightsHiddenOut[i][j] = 0.0;
				
			}
		}
	}


	//one iteration of forward feed for one hidden layer
	public void forwardPropagation() {
		
		for (int i = 1; i < argNumHidden; i ++) { //from 1 to prevent changing bias node
			hiddenS[i] = 0.0;
			
			for (int j = 0; j < argNumInputs; j ++) {
				hiddenS[i] = hiddenS[i] + weightsHiddenIn[j][i] * inputValues[currTrainingSet][j];
			}
			hiddenS[i] = customSigmoid(hiddenS[i]); /*choose betweeen regular sigmoid and custom? Consider RELU? */
		}
		
		//from hidden neurons and to output
		for (int k = 0; k < numOutputs; k ++) {
			outputs[k] = 0.0;
			for (int l = 0; l < argNumHidden; l ++) {
				outputs[k] = outputs[k] + weightsHiddenOut[l][k] * hiddenS[l];
	}
			outputs[k] = customSigmoid(outputs[k]);
	
		}
		
		singleError[0] = C[currTrainingSet] - outputs[0];
		//System.out.println(singleError[0]);
	}
	
	public void backPropagation() {
		
		//calculating delta from output layer
		for (int i = 0; i < numOutputs; i ++) {
			//deltaOutput[i] = 0;
			deltaOutput[i] = singleError[i] * outputs[i] * (1 - outputs[i]); //sat manual num of training set
			
		}
		//updating weights from hidden to output
				//double[][] tempWeights = weightsHiddenOut.clone();
				for (int i = 0; i < numOutputs; i ++) {
					
					for (int j = 0; j < argNumHidden; j ++) {
						//weightsHiddenOut[j][i] = weightsHiddenOut[j][i] + argMomentumTerm * deltaWeightsHiddenOut[j][i] + argLearningRate * deltaOutput[i] * hiddenS[j];
						//deltaWeightsHiddenOut[j][i] = weightsHiddenOut[j][i] - tempWeights[j][i]; //CHECK THIS
						deltaWeightsHiddenOut[j][i] = argMomentumTerm * deltaWeightsHiddenOut[j][i] + argLearningRate * deltaOutput[i];
						weightsHiddenOut[j][i] = weightsHiddenOut[j][i] + deltaWeightsHiddenOut[j][i];
					}
				}
		
		//calculating delta from hidden layer
		for (int j = 0; j < argNumHidden; j ++) {
			deltaHiddenOutput[j] = 0;
			for (int k = 0; k < numOutputs; k ++) {
				deltaHiddenOutput[j] = deltaHiddenOutput[j] + weightsHiddenOut[j][k] * deltaOutput[k];
			}
			double fPrime =  hiddenS[j] * (1 - hiddenS[j]);
			deltaHiddenOutput[j] = deltaHiddenOutput[j] * fPrime;
		}
		
		
		//tempWeights = weightsHiddenIn.clone();
		for (int j = 1; j < argNumHidden; j ++) { //bias node unchanged
			
			for (int i = 0; i < argNumInputs; i ++) { //sat num of training set manually
				//weightsHiddenIn[i][j] = weightsHiddenIn[i][j] + argMomentumTerm * deltaWeightsHiddenIn[i][j] + argLearningRate * deltaHiddenOutput[j] * inputValues[0][i];
				//deltaWeightsHiddenIn[i][j] = weightsHiddenIn[i][j] - tempWeights[i][j]; //CHECK THIS
				deltaWeightsHiddenIn[i][j] = argMomentumTerm * deltaWeightsHiddenIn[i][j] + argLearningRate * deltaHiddenOutput[j] * inputValues[currTrainingSet][i];
				weightsHiddenIn[i][j] = weightsHiddenIn[i][j] + deltaWeightsHiddenIn[i][j];
			}
		}
		//update to new training set
		currTrainingSet = (currTrainingSet + 1) % numTrainingSets;
		
	}
	public double train() {
		int epoch = 0;
		
		totalError[0] = 10;
		while (totalError[0] > MAX_ERROR) {
			totalError[0] = 0;
			currTrainingSet = 0;
			
			for (int i = 0; i < numTrainingSets; i ++) {
				forwardPropagation();
				backPropagation();
				
				totalError[0] = totalError[0] + Math.pow(singleError[0], 2);
				
				
			}
		totalError[0] = totalError[0] / 2;
		epoch = epoch + 1;
		System.out.println(totalError[0]);
		}
		System.out.print("heI");
		return epoch;
	}
	
	public void initializeTrainingSet() {
		double bias = 1;
		//for (int i = 0; i < )
		C[0] = 0;
		C[1] = 1;
		C[2] = 1;
		C[3] = 0;
		
		inputValues[0][0] = bias;
		inputValues[0][1] = 0;
		inputValues[0][2] = 0;
		
		inputValues[1][0] = bias;
		inputValues[1][1] = 0;
		inputValues[1][2] = 1;
		
		inputValues[2][0] = bias;
		inputValues[2][1] = 1;
		inputValues[2][2] = 0;
		
		inputValues[3][0] = bias;
		inputValues[3][1] = 1;
		inputValues[3][2] = 1;
		
		hiddenS[0] = bias;
	}


public static void main(String[] args) {
	int NumInputs = 2;
	int NumHidden = 4;
	double LearningRate = 0.2;
	double MomentumTerm = 0.0;
	double A = -0.5;
	double B = 0.5;
	
	NeuralNet XOR = new NeuralNet(NumInputs, NumHidden, LearningRate, MomentumTerm, A, B);
	XOR.initializeWeights();
	XOR.initializeTrainingSet();
	XOR.train();
	
	
	return;
}

@Override
public double outputFor(double[] X) {
	// TODO Auto-generated method stub
	return 0;
}

@Override
public double train(double[] X, double argValue) {
	// TODO Auto-generated method stub
	return 0;
}

@Override
public void save(File argFile) {
	// TODO Auto-generated method stub
	
}

@Override
public void load(String argFileName) throws IOException {
	// TODO Auto-generated method stub
	
}


}