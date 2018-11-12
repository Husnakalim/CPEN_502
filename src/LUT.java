

public class LUT {

    int argNumInputs;
    int[] argVariableFloor;
    int[] argVariableCeiling;

    int numActions;
    int numStates;

    public double[][] table;

    LUT(int argNumInputs, int[] argVariableFloor, int[] argVariableCeiling) {

        this.argNumInputs = argNumInputs;
        this.argVariableFloor = argVariableFloor;
        this.argVariableCeiling = argVariableCeiling;

        //initializing table when constructor is called
        initialiseLUT();
    }


    //initialize entries to zero
    void initialiseLUT() {
        table = new double[numStates][numActions];
        for (int i = 0; i < numStates; i++) {
            for (int j = 0; j < numActions; j++) {
                table[i][j] = 0.0;
            }
        }
    }
    //return specified table value
    public double getValue(int state, int action){
        return table[state][action];
    }

    public void setValue(int state, int action, double QVal){
        table[state][action] = QVal;
    }


    double getMaxValue(int state){

        double maxValue = -100000;
        for (int j = 0; j < numActions; j++){
            if (table[state][j] > maxValue){
                maxValue = table[state][j];
                }
            }
        return maxValue;
    }

    int getBestAction(int state){
        double maxValue = -100000;
        int bestAction = 0;             //return this state by default
        for (int i = 0; i < table[state].length; i ++){
            if (getValue(state, i) > maxValue){
                maxValue = getValue(state, i);
                bestAction = i;
            }
        }
        return bestAction;
    }





}