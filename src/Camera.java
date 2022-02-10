import java.lang.Math;
public class Camera {
    SwerveGui gui;
    //Swerve Robot;
    /*
    public double distanceX(double ballX, double robotX) {
        return Math.abs((ballX - robotX));
    }
    public double distanceY(double ballY, double robotY) {
        return Math.abs((ballY - robotY));
    }
    */
    public Camera(SwerveGui gui) {
        this.gui = gui;
        //this.Robot = Robot;
    }


    public double dotProduct(double v1X, double v1Y, double v2X, double v2Y) {
        double product = (v1X*v2X) + (v1Y*v2Y);
        return product;
    }

    public double angle() {
        double vectorBX = -(gui.getRobotX() - gui.ballX);
        double vectorBY = -(gui.getRobotY() - gui.ballY);
        

        double vectorAX = -(gui.getRobotX() - gui.getFrontX());
        double vectorAY = -(gui.getRobotY() - gui.getFrontY());
        //System.out.println("Robot: " + gui.getRobotX() + ", Front: " + gui.getFrontX());
        //System.out.println("FrontX: " + gui.getFrontX() + ", FrontY: " + gui.getFrontY());
        double magVectorA = Math.sqrt(dotProduct(vectorAX, vectorAY, vectorAX, vectorAY));
        double magVectorB = Math.sqrt(dotProduct(vectorBX, vectorBY, vectorBX, vectorBY));
        /*
        if ((magVectorA*magVectorB) != 0) {
            double angle2 = Math.acos((dotProduct(vectorAX, vectorAY, vectorBX, vectorBY))/(magVectorA*magVectorB));
            //System.out.println(Math.toDegrees(angle2));
            return angle2;
        }
        */
        double angle2 = Math.acos((dotProduct(vectorAX, vectorAY, vectorBX, vectorBY))/(magVectorA*magVectorB));
        if (angle2 > Math.PI/2) {
            System.out.println("Chec>>>>>>>>>>>");
            angle2 -= Math.PI;
        }
        System.out.println(angle2);
        System.out.println("VectorAX: " + vectorAX + ", VectorAY: " + vectorAY + ", VectorBX: " + vectorBX + "VectorBY: " + vectorBY);
        return angle2;
        //return 0.8;
    }
}