
public class RobotStates {


    static int numDirections = 4;      //number of direction states
    static int numEnemyDistance = 10;   //number of enemy distance states
    static int numEnemyBearing = 4;     //number of enemy bearing states
    static int numEnergy = 5;          //energy levels of robot
    static int hitWall = 2;            //hit wall or not
    static int hitByBullet = 2;        //hit by bullet or not

    static int numOfStates = numDirections * numEnemyDistance * numEnergy * hitWall * hitByBullet; //total num of states

    static int state[][][][][][] = new int[numDirections][numEnemyDistance][numEnemyBearing][numEnergy][hitWall][hitByBullet];





    //converting functions
    public static int convertDirection(double dir){
        double angles = 360 / numDirections;
        double newDirection = dir + angles / 2;
        if (newDirection > 360) newDirection -= 360;
        return (int)(newDirection / angles);


    }

    public int convertEnemyDistance(double dist){
        int distance = (int)(dist / 30.0); //look this up
        if (distance > numEnemyDistance -1) distance = numEnemyDistance -1;
        return distance;
    }


    public int convertEnemyBearing(double bearing)
    {
        double PIx2 = Math.PI * 2;
        if (bearing < 0)
            bearing = PIx2 + bearing;
        double angle = PIx2 / numEnemyBearing;
        double newBearing = bearing + angle / 2;
        if (newBearing > PIx2)
            newBearing -= PIx2;
        return (int)(newBearing / angle);
    }

}
