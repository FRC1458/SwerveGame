import java.lang.Math;
public class Camera {
    SwerveGui Coords = new SwerveGui();
    public double distanceX(double ballX, double robotX) {
        return Math.abs((ballX - robotX));
    }
    public double distanceY(double ballY, double robotY) {
        return Math.abs((ballY - robotY));
    }
    double vectorX = distanceX(Coords.randomInt1, Coords.getRobotX());
    double vectorY1 = distanceY(Coords.randomInt2, Coords.getRobotY());
    public double angle() {
        double angle = Math.acos((vectorX*hypotenuse())/(Math.abs(vectorX))*Math.abs(hypotenuse()));
        System.out.println(angle*180/Math.PI);
        return angle*180/Math.PI;
    }
    public double hypotenuse() {
        double hypotenuse = Math.sqrt(Math.pow(vectorX,2)+Math.pow(vectorY1,2));
        return hypotenuse;
    }
}