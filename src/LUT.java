
import java.io.*;
import robocode.*;


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
    //set specified table value
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





    /**
     * Will attempt to write only the 'visited' elements of the look up table
     * to the specified file.
     * Format is as follows
     * 1st line is size of LUT
     * nth line is index
     * nth+1 line is Q-value for index on previous line
     * @param argFile of type File.
     */
    void save( String argFile ) {
        PrintStream saveFile = null;

        try {
            saveFile = new PrintStream( new RobocodeFileOutputStream( argFile ));
        }
        catch (IOException e) {
            System.out.println( "*** Could not create output stream for LUT save file.");
        }

        saveFile.println( numStates );
        int numEntriesSaved = 0;
        for (int i=0; i<numStates; i++) {
            for (int j = 0; j < numActions; j++) {
                saveFile.println(table[i][j]);
                numEntriesSaved++;
            }
        }
        saveFile.close();
        System.out.println ( "--+ Number of LUT table entries saved is " + numEntriesSaved );
    }

    /**
     * Loads the LUT from file
     * Expects that the 1st line match the maxIndex otherwise returns
     */
    public void load( String argFileName ) throws IOException {

        FileInputStream inputFile = new FileInputStream(argFileName);
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(inputFile));

        // Check that the maxIndex matches
        int maxIndexFromFile = Integer.valueOf(inputReader.readLine());
        if (maxIndexFromFile != numActions) {
            System.out.println("*** MaxIndex for file does not match LUT.");
            return;
        }

        // Now load the LUT
        int numEntriesLoaded = 0;
        for (int i = 0; i < numStates; i++) {
            for (int j = 0; j < numActions; j++){
            table[i][j] = Double.parseDouble(inputReader.readLine());
            numEntriesLoaded++;
            }
        }
        inputReader.close();
        System.out.println ( "--+ Number of LUT entries loaded was " + numEntriesLoaded );
    }

}