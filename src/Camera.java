import java.lang.Math;
public class Camera {
    SwerveGui gui;
    public Camera(SwerveGui gui) {
        this.gui = gui;
        //this.Robot = Robot;
    }
    // FrontX = X coordinate of tip of triangle on robot
    // FrontY = Y coordinate of tip of triangle on robot

    public double dotProduct(double v1X, double v1Y, double v2X, double v2Y) {
        double product = (v1X*v2X) + (v1Y*v2Y);
        return product;
    }
    public double angle() {
        double vectorBX = -(gui.getTrueRobotX() - gui.ballX);
        double vectorBY = -(gui.getTrueRobotY() - gui.ballY);
        

        double vectorAX = -(gui.getTrueRobotX() - gui.getFrontX());
        double vectorAY = -(gui.getTrueRobotY() - gui.getFrontY());

        double magVectorA = Math.sqrt(dotProduct(vectorAX, vectorAY, vectorAX, vectorAY));
        double magVectorB = Math.sqrt(dotProduct(vectorBX, vectorBY, vectorBX, vectorBY));
        if ((magVectorA*magVectorB) != 0) {
            double angle2 = Math.acos((dotProduct(vectorAX, vectorAY, vectorBX, vectorBY))/(magVectorA*magVectorB));
            return angle2 - Math.PI/2;
        }
        return 0.8;
    }
    public double[] ballVector() {
        double vectorBX1 = -(gui.getTrueRobotX() - gui.ballX);
        double vectorBY1 = -(gui.getTrueRobotY() - gui.ballY);

        double magVectorB = Math.sqrt(dotProduct(vectorBX1, vectorBY1, vectorBX1, vectorBY1));
        double[] ballVectorArray = {vectorBX1, vectorBY1, magVectorB};   
        return ballVectorArray; 
    }
    // hubVector = vector from robot to hub for robot to drive on
    public double[] hubVector() {
        double hubVectorX = -(gui.getTrueRobotX() - 1000);
        double hubVectorY = -(gui.getTrueRobotY() - 600);

        double magHubVector = Math.sqrt(dotProduct(hubVectorX, hubVectorY, hubVectorX, hubVectorY));
        double[] hubVectorArray = {hubVectorX, hubVectorY, magHubVector};
        return hubVectorArray;
    }
    // hubAngle = Angle for robot to turn
    public double hubAngle() {
        double hubVectorX = -(gui.getTrueRobotX() - 1000);
        double hubVectorY = -(gui.getTrueRobotY() - 600);

        double vectorAX = -(gui.getTrueRobotX() - gui.getFrontX());
        double vectorAY = -(gui.getTrueRobotY() - gui.getFrontY());

        double magVectorA = Math.sqrt(dotProduct(vectorAX, vectorAY, vectorAX, vectorAY));
        double magHubVector = Math.sqrt(dotProduct(hubVectorX, hubVectorY, hubVectorX, hubVectorY));
        if ((magVectorA*magHubVector) != 0) {
            double angleToHub = Math.acos((dotProduct(vectorAX, vectorAY, hubVectorX, hubVectorY))/(magVectorA*magHubVector));
            return angleToHub;
        }
        return 0.8;
    }
}