
//imported utilities
import java.util.Random;



public class NeuralNet implements NeuralNetInterface {
	
	//size of Network; neurons and inputs
	static int NUM_HIDDEN_NEURONS;
	static int NUM_INPUTS;
	
	
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
	
	//arrays to store inputs and weights
	public double[] inputValues = new double[NUM_INPUTS];
	public double[][] weights = new double[NUM_HIDDEN_NEURONS][NUM_INPUTS];
	
	
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
		return 2 / (1 + Math.exp(-x)) - 1;
	}
	
	//custom sigmoid function
	public double sigmoid(double x) {
		double a = argA;
		double b = argB;
		
		return (b - a) / (1 + Math.exp(-x)) + a; /*Check this*/
	}
	//initializing weights to random values
	public void initializeWeights() {
		
		for (int i = 0; i < numHiddenNeurons; i++) {
			for (int j = 0; j < numInputs; j++) {
				double r = new Random().nextDouble();
				
				weights[i][j] = minInitWeight + (r * (maxInitWeight - minInitWeight));
			}
		}
	}
	
	public void zeroWeights() {
		
		for (int i = 0; i < numHiddenNeurons; i++) {
			for (int j = 0; j < numInputs; j++) {
				weights[i][j] = 0.0;
			}
	}
	

}
