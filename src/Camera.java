import java.lang.Math;
public class Camera {
    SwerveGui Coords = new SwerveGui();
    /*
    public double distanceX(double ballX, double robotX) {
        return Math.abs((ballX - robotX));
    }
    public double distanceY(double ballY, double robotY) {
        return Math.abs((ballY - robotY));
    }
    */
    double vectorBX = (Coords.getRobotX() - Coords.ballX);
    double vectorBY = (Coords.getRobotY() - Coords.ballY);
    
    

    public double angle(double vectorX, double vectorY) {
        /*double angle = Math.atan(ballToRobotY/ballToRobotX);
        System.out.println(angle*180/Math.PI);
        return angle;*/
        /*double angle = Math.acos((vectorX*vectorY)/(Math.abs(vectorX)*Math.abs(vectorY)));
        System.out.println(angle);
        return angle;*/
        return 0;
    }
}