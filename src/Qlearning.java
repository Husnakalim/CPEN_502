



public class Qlearning {

    //hyperparameters
    private double learningRate;
    private double discountFactor;

    private double epsilon; //exploration rate
    private double tau; //exploitation rate

    //reward variable
    double reward;


    //Look-up table
    LUT table;

    //Updated action and state variables
    int prevState;
    int prevAction;





    //FUNCTIONS
    void setTable(LUT table){
        this.table = table;
    }

    void Qlearn(int state, int action, double reward){

        double oldQVal = table.getValue(prevState, prevAction);
        double learnedValue = reward + discountFactor * table.getMaxValue(state);
        double newQVal = (1 - learningRate) * oldQVal + learningRate * learnedValue;

        table.setValue(state, action, newQVal);

        //update state and action
        prevAction = action;
        prevState = state;




    }
    //implement both Softmax and epsilon-greedy action selection
    int selectActionEGreedy(int state){
        int action;
        double rand = Math.random();

        if (epsilon < rand){
            action = (int) Math.random() * table.numActions; //verify this
        }
        else{
            action = table.getBestAction(state);

        }

        return action;
    }

    int selectActionSoftMax(int state){
        int action = 0;
        double Qsum = 0;
        double[] Qprob = new double[table.numActions];

        for (int i = 0; i < table.numActions; i ++){
            Qprob[i] = Math.exp(table.getValue(state, i) / tau);
            Qsum += Qprob[i];
        }
        if (Qsum != 0){
            for (int i = 0; i < table.numActions; i ++) {
                Qprob[i] /= Qsum;
            }

        }else{
            action = table.getBestAction(state);
            return action;
        }

        //Look into this
        double cumulativeProb = 0.0;
        double randomNum = Math.random();
        while (randomNum > cumulativeProb && action < table.numActions)
        {
            cumulativeProb += Qprob[action];
            action ++;
        }
        return action - 1;



    }







}
