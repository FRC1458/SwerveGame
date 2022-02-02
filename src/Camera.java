public class Camera {

    public float distanceX(float ballX, float robotX) {
        return (ballX - robotX);
    }
    public float distanceY(float ballY, float robotY) {
        return (ballY - robotY);
    }
}