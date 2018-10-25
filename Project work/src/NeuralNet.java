
//imported utilities
import java.util.Random;



public class NeuralNet implements NeuralNetInterface {
	
	//size of Network; neurons and inputs
	static int NUM_HIDDEN_NEURONS;
	static int NUM_INPUTS;
	static int NUM_OUTPUTS;
	static int MAX_EPOCH = 100000;
	static double MAX_ERROR = 0.05;
	
	
	//Private variables
	private int argNumInputs;
	private int argNumHidden;
	private double argLearningRate;
	private double argMomentumTerm;
	private double argA;
	private double argB;
	
	//varaibles for determining interval for random initialization of weights
	private double minInitWeight;
	private double maxInitWeight;
	
	//variables to store constants 
	private int numHiddenNeurons = NUM_HIDDEN_NEURONS; /*Added to clean out code*/
	private int numInputs = NUM_INPUTS;
	private int numOutputs = NUM_OUTPUTS;
	
	//arrays to store inputs and weights
	public double[] inputValues = new double[NUM_INPUTS];
	public double[][] weightsHiddenIn = new double[NUM_INPUTS][NUM_HIDDEN_NEURONS];
	public double[][] weightsHiddenOut = new double[NUM_HIDDEN_NEURONS][NUM_OUTPUTS]; /*consider one 3d matrix for all weights to generalize*/
	
	
	//delta arrays
	public double[] deltaOutput = new double[numOutputs];
	public double[] deltaHiddenOutput = new double[argNumHidden];
	public double[] deltaWeightsHiddenIn = new double[NUM_INPUTS][NUM_HIDDEN_NEURONS];
	public double[] deltaWeightsHiddenOut = new double[NUM_HIDDEN_NEURONS][NUM_OUTPUTS];
	
	//sum of product of weights and input, S
	public double[] hiddenS = new double[argNumHidden]; 
	public double[] outputs = new double[numOutputs];
	
	//training set variables (true output)
	public double[] C = new double[numOutputs]; //fix
	
	//error data
	public double[] totalError = new double[MAX_EPOCH];
	
	
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
	
	// standard sigmoid function
	public double sigmoid(double x) {
		return 2 / (1 + Math.exp(-x)) - 1; /*why 2?*/
	}
	
	//custom sigmoid function
	public double sigmoid(double x) {
		double a = argA;
		double b = argB;
		
		return (b - a) / (1 + Math.exp(-x)) + a; /*Check this*/
	}
	
	
	//initializing both weight sets to random values
	public void initializeWeights() {
		
		for (int i = 0; i < numHiddenNeurons; i++) {
			for (int j = 0; j < numInputs; j++) {
				double r = new Random().nextDouble();
				
				weightsHiddenIn[i][j] = minInitWeight + (r * (maxInitWeight - minInitWeight));
				deltaWeightsHiddenIn[i][j] = 0.0;
			}
		}
		for (int i = 0; i < numOutputs; i++) {
			for (int j = 0; j < numHiddenNeurons; j++) {
				double r = new Random().nextDouble();
				
				weightsHiddenOut[i][j] = minInitWeight + (r * (maxInitWeight - minInitWeight));
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

}
	//one iteration of forward feed for one hidden layer
	public void forwardPropagation() {
		
		//from input and to hidden neuron
		for (int i = 0; i < argNumHidden; i ++) {
			hiddenS[i] = 0.0;
			for (int j = 0; i < argNumInputs; j ++) {
				hiddenS[i] = S[i] + weights[i][j] * inputValues[i];
			}
			S[i] = customSigmoid(S[i]); /*choose betweeen regular sigmoid and custom? Consider RELU? */
		}
		
		//from hidden neurons and to output
		for (int k = 0; k < numOutputs; k ++) {
			outputs[k] = 0.0;
			for (int l = 0; k < argNumHidden; l ++) {
				outputs[k] = outputs[k] + weightsHiddenOut[k][l] * hiddenS[k];
	}
			outputs[k] = customSigmoid(outputs[k]);
	
		}
	}
	
	public void backwardPropagation() {
		
		//calculating delta from output layer
		for (int i = 0; i < numOutputs; i ++) {
			deltaOutput[i] = (C[i] - outputs[i]) * outputs[i] * (1 - outputs[i]);
			
		}
		//calculating delta from hidden layer
		for (int j = 0; j < argNumHidden; j ++) {
			
			for (int k = 0; k < numOutputs; k ++) {
				deltaHiddenOutput[j] = deltaHiddenOutput[j] + weigthsHiddenOut[j][k] * deltaOutput[k];
			}
			double fPrime =  hiddenS[j] * (1 - hiddenS[j]);
			deltaHiddenOutput[j] *= fPrime;
		}
		
		//updating weights
		double[][] tempWeights = weightsHiddenOut.clone();
		for (int i = 0; i < numOutputs; i ++) {
			
			for (int j = 0; j < argNumHidden; j ++) {
				weightsHiddenOut[j][i] = weightsHiddenOut[j][i] + argMomentumTerm * deltaWeightHiddenOut[j][i] + argLearningRate * deltaOut[i] * hiddenS[j];
				deltaWeightsHiddenOut[j][i] = weightsHiddenOut[j][i] - tempWeights[j][i]; //CHECK THIS
				
			}
		}
		double[][] tempWeights = weightsHiddenIn.clone();
		for (int j = 0; j < argNumHidden; j ++) {
			
			for (int i = 0; i < argNumInputs; i ++) {
				weightsHiddenIn[i][j] = weightsHiddenIn[i][j] + argMomentumTerm * deltaWeightHiddenIn[i][j] + argLearningRate * deltaHiddenOutput[j] * inputValues[i];
				deltaWeightsHiddenIn[i][j] = weightsHiddenIn[i][j] - tempWeights[i][j]; //CHECK THIS
				
			}
		}
		
	}
	public void train() {
		double epoch = 0;
		double error = 10000;
		
		while (error > MAX_ERROR) {
			forwardPropagation();
			backwardPropagation();
			
			for ( int i = 0; i < numOutputs; i ++) {
				error = error + math.pow(C[i] - outputs[i],2);  //MAKE AS FUNCTION?
			totalError[epoch] = error / 2;
			}
		epoch = epoch + 1;
		}
		
		
	}
		
