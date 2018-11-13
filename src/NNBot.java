



import robocode.*;
import java.awt.*;
import java.awt.color.*;
import robocode.util.Utils;



public class NNBot extends AdvancedRobot{

    Qlearning learning;
    LUT table;



    double reward;


    public void run(){
        //set colors of robot
        setColors(Color.red, Color.yellow, Color.pink);




        //play game
        while(true){
            robotMovement();
            radarMovement();

        }
    }

    public void robotMovement(){
        RobotActions availableActions = new RobotActions(100, 6); //object to specify avail. actions
        int state = getState();
        int action = learning.selectActionEGreedy(state); //choose between greedy and softmax
        learning.Qlearn(state, action, reward);

        switch(action){
            case availableActions.moveAhead:
                ahead(availableActions.moveDistance);
                break;
            case (availableActions.moveBack):
                back(availableActions.moveDistance);
                break;
            case (availableActions.aheadTurnLeft):
                turnLeft(90);
                ahead(availableActions.moveDistance);
                break;
            case (availableActions.aheadTurnRight):
                turnRight(90);
                ahead(availableActions.moveDistance);
                break;
            case (availableActions.backTurnLeft):
                turnLeft(90);
                back(availableActions.moveDistance);
                break;
            case (availableActions.backTurnRight):
                turnRight(90);
                back(availableActions.moveDistance);
                break;


        }
    }
    //Code extracted from Robocode wiki: Implementation of WidthLock radar.
    public void radarMovement() {
        // Turn the radar if we have no more turn, starts it if it stops and at the start of round
        if (getRadarTurnRemaining() == 0.0)
            setTurnRadarRightRadians(Double.POSITIVE_INFINITY);



    }


    public void onScannedRobot(ScannedRobotEvent e) {
        // IMPLEMENT MORE STUFF HERE



        // Absolute angle towards target
        double angleToEnemy = getHeadingRadians() + e.getBearingRadians();

        // Subtract current radar heading to get the turn required to face the enemy, be sure it is normalized
        double radarTurn = Utils.normalRelativeAngle( angleToEnemy - getRadarHeadingRadians() );

        // Distance we want to scan from middle of enemy to either side
        // The 36.0 is how many units from the center of the enemy robot it scans.
        double extraTurn = Math.min( Math.atan( 36.0 / e.getDistance() ), Rules.RADAR_TURN_RATE_RADIANS );

        // Adjust the radar turn so it goes that much further in the direction it is going to turn
        // Basically if we were going to turn it left, turn it even more left, if right, turn more right.
        // This allows us to overshoot our enemy so that we get a good sweep that will not slip.
        if (radarTurn < 0)
            radarTurn -= extraTurn;
        else
            radarTurn += extraTurn;

        //Turn the radar
        setTurnRadarRightRadians(radarTurn);

        // ...
    }
    /*public void radarMovement(){
        double threshhold = 0.5;
        double rand = Math.random();

        if (rand < 0.5)
            turnRadarLeft(180);
        else
            turnRadarRight(180);


    }*/


    int getState(){

    }
}
