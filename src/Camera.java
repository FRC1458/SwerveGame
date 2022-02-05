import java.lang.Math;
public class Camera {
    SwerveGui Coords = new SwerveGui();
    public double distanceX(double ballX, double robotX) {
        return (ballX - robotX);
    }
    public double distanceY(double ballY, double robotY) {
        return (ballY - robotY);
    }
    double ballToRobotX = distanceX(Coords.randomInt1, Coords.getRobotX());
    double ballToRobotY = distanceY(Coords.randomInt2, Coords.getRobotY());
    public double angle(double vectorX, double vectorY) {
        /*double angle = Math.atan(ballToRobotY/ballToRobotX);
        System.out.println(angle*180/Math.PI);
        return angle;*/
        /*double angle = Math.acos((vectorX*vectorY)/(Math.abs(vectorX)*Math.abs(vectorY)));
        System.out.println(angle);
        return angle;*/
        return 0;


    }
    public double hypotenuse() {
        double hypotenuse = Math.sqrt(Math.pow(ballToRobotX, 2) + Math.pow(ballToRobotY, 2));
        return hypotenuse;
    }
}