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
    public double angle() {
        double angle = Math.atan(ballToRobotY/ballToRobotX);
        System.out.println(angle*180/Math.PI);
        return angle;
    }
    public double hypotenuse() {
        double hypotenuse1 = Math.pow(ballToRobotX, 2);
        double hypotenuse2 = Math.pow(ballToRobotY, 2);
        double hypotenuse = Math.pow((hypotenuse1 + hypotenuse2), 0.5);
        return hypotenuse;
    }
}