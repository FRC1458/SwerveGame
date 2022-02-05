import java.lang.Math;
public class Camera {
    SwerveGui Coords = new SwerveGui();
    Swerve Robot = new Swerve();
    /*
    public double distanceX(double ballX, double robotX) {
        return Math.abs((ballX - robotX));
    }
    public double distanceY(double ballY, double robotY) {
        return Math.abs((ballY - robotY));
    }
    */




    public double angle() {
        double vectorBX = (Coords.getRobotX() - Coords.ballX);
        double vectorBY = (Coords.getRobotY() - Coords.ballY);
        
        double vectorAX = Robot.m_dRobotLen/2;
        double vectorAY = Robot.m_dRobotLen/2;

        double magVectorA = Math.sqrt(Math.pow(vectorAX, 2) + Math.pow(vectorAY, 2));
        double magVectorB = Math.sqrt(Math.pow(vectorBX, 2) + Math.pow(vectorBY, 2));
        double angle = Math.toDegrees(Math.acos(((vectorAX*vectorBX) + (vectorBY*vectorAY))/(magVectorA*magVectorB)));
        System.out.println(angle);
        return angle;
    }
}