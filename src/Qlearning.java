



public class Qlearning {

    //hyperparameters
    private double learningRate;
    private double discountFactor;

    //reward variable
    double reward;


    //Look-up table
    LUT table;

    //Updated action and state variables
    int prevState;
    int prevAction;





    //FUNCTIONS
    void Qlearn(int state, int action, double reward){

        double oldQVal = table.getValue(prevState, prevAction);
        double learnedValue = reward + discountFactor * table.getMaxValue(state);
        double newQVal = (1 - learningRate) * oldQVal + learningRate * learnedValue;

        table.setValue(state, action, newQVal);

        //update state and action
        prevAction = action;
        prevState = state;




    }
    void setTable(LUT table){
        this.table = table;
    }






}
