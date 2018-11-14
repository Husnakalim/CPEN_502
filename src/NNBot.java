



import robocode.*;
import java.awt.*;
import robocode.util.Utils;
import java.awt.geom.*;



public class NNBot extends AdvancedRobot{

    //Reinforcement learning objects
    Qlearning learning;
    LUT table;


    //reward variable
    double reward;


    //status variables
    int hitWall;
    int hitByEnemy;


    //###################
    //main robot function
    //###################
    public void run(){
        //set colors of robot
        setColors(Color.red, Color.yellow, Color.pink);

        //independent moving of gun and radar
        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);




        //play game
        while(true){
            robotMovement();
            radarMovement();

            execute();

        }
    }

    public void robotMovement(){
        //RobotActions availableActions = new RobotActions(100, 6); //object to specify avail. actions

        int state = getState();
        int action = learning.selectActionEGreedy(state); //choose between greedy and softmax
        learning.Qlearn(state, action, reward);

        hitByEnemy = 0;
        hitWall = 0;

        switch(action){
            case RobotActions.moveAhead:
                ahead(RobotActions.moveDistance);
                break;
            case (RobotActions.moveBack):
                back(RobotActions.moveDistance);
                break;
            case (RobotActions.aheadTurnLeft):
                turnLeft(90);
                ahead(RobotActions.moveDistance);
                break;
            case (RobotActions.aheadTurnRight):
                turnRight(90);
                ahead(RobotActions.moveDistance);
                break;
            case (RobotActions.backTurnLeft):
                turnLeft(90);
                back(RobotActions.moveDistance);
                break;
            case (RobotActions.backTurnRight):
                turnRight(90);
                back(RobotActions.moveDistance);
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

        //Fire operations -- CircularTargeting method from robowiki:
        circularTargeting(e);

    }
    /*public void radarMovement(){
        double threshhold = 0.5;
        double rand = Math.random();

        if (rand < 0.5)
            turnRadarLeft(180);
        else
            turnRadarRight(180);


    }*/




    //###################
    //action on events
    //###################
    public void onHitWall(HitWallEvent e){
        reward += -2; //decrement reward by some value
        hitWall = 1;
    }

    public void onBulletHit(BulletHitEvent e) {

        //out.println("Bullet Hit: " + change);
        reward += e.getBullet().getPower() * 9;
        }
    public void onBulletMissed(BulletMissedEvent e) {
        //out.println("Bullet Missed: " + change);
        reward += -e.getBullet().getPower();
    }
    public void onHitByBullet(HitByBulletEvent e) {
        double power = e.getBullet().getPower();
        hitByEnemy  = 1;
        //out.println("Hit By Bullet: " + -(4 * power + 2 * (power - 1)));
        reward += -(4 * power + 2 * (power - 1));
        }

    public void onHitRobot(HitRobotEvent e) {
            out.println("Hit Robot: " + -6.0);
            reward += -6.0;
        }


        int getState(){
            ScannedRobotEvent e; //TRY THIS
            int direction = RobotStates.convertDirection(getHeading());
            int enemyDist = (int)e.getDistance();
            int enemyEnergy = (int)e.getEnergy();
            int enemyBearing = (int)e.getBearing();
            int hitW = hitWall;
            int hitByE = hitByEnemy;
            int currState = RobotStates.state[direction][enemyDist][enemyEnergy][enemyBearing][hitW][hitByE]; //CHEKCK THIS
            return currState;
    }

    //implemented from web. TRY THIS
    public void circularTargeting(ScannedRobotEvent e){
        double bulletPower = Math.min(3.0, getEnergy());
        double myX = getX();
        double myY = getY();
        double absoluteBearing = getHeadingRadians() + e.getBearingRadians();
        double enemyX = getX() + e.getDistance() * Math.sin(absoluteBearing);
        double enemyY = getY() + e.getDistance() * Math.cos(absoluteBearing);
        double oldEnemyHeading = 0; //correct this
        double enemyHeading = e.getHeadingRadians();
        double enemyHeadingChange = enemyHeading - oldEnemyHeading;
        double enemyVelocity = e.getVelocity();
        oldEnemyHeading = enemyHeading;

        double deltaTime = 0;
        double battleFieldHeight = getBattleFieldHeight(),
                battleFieldWidth = getBattleFieldWidth();
        double predictedX = enemyX, predictedY = enemyY;
        while((++deltaTime) * (20.0 - 3.0 * bulletPower) <
                Point2D.Double.distance(myX, myY, predictedX, predictedY)){
            predictedX += Math.sin(enemyHeading) * enemyVelocity;
            predictedY += Math.cos(enemyHeading) * enemyVelocity;
            enemyHeading += enemyHeadingChange;
            if(	predictedX < 18.0
                    || predictedY < 18.0
                    || predictedX > battleFieldWidth - 18.0
                    || predictedY > battleFieldHeight - 18.0){

                predictedX = Math.min(Math.max(18.0, predictedX),
                        battleFieldWidth - 18.0);
                predictedY = Math.min(Math.max(18.0, predictedY),
                        battleFieldHeight - 18.0);
                break;
            }
        }
        double theta = Utils.normalAbsoluteAngle(Math.atan2(
                predictedX - getX(), predictedY - getY()));

        setTurnRadarRightRadians(Utils.normalRelativeAngle(
                absoluteBearing - getRadarHeadingRadians()));
        setTurnGunRightRadians(Utils.normalRelativeAngle(
                theta - getGunHeadingRadians()));
        fire(3); //alter firepower with distance?
    }

}
