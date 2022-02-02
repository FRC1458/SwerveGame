public class Camera {
    private float ballX;
    private float ballY;
    public Camera(float ballX, float ballY) {
        this.ballX = ballX;
        this.ballY = ballY;
        SwerveGui Coords = new SwerveGui();
    }
    public void distanceX(float ballX, float Coords.getRobotX()) {
        return (ballX - Coords.getRobotX());
    }
    public void distanceY(float ballY, float Coords.getRobotY()) {
        return (ballY - Coords.getRobotY());
    }
}