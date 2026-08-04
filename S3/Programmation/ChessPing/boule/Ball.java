package boule;

public class Ball {
    private double x, y;
    private double vx = 0, vy = 0;
    private final int radius;

    public Ball(int startX, int startY, int radius) {
        this.x = startX;
        this.y = startY;
        this.radius = radius;
    }


    //Deplace la boule selon la vitesse
    public void update() {
        x += vx;
        y += vy;
        vx *= 0.98;
        vy *= 0.98;
    }

    //Verifier si le boule touche la case
    public void checkCollision(int width, int height) {

    if (x - radius < 0) {
        x = radius;
        vx = -vx * 0.8;
    }

    if (x + radius > width) {
        x = width - radius;
        vx = -vx * 0.8;
    }

    if (y - radius < 0) {
        y = radius;
        vy = -vy * 0.8;
    }

    if (y + radius > height) {
        y = height - radius;
        vy = -vy * 0.8;
    }
}


    //Applique la force a la boule pour modifier sa vitesse
    public void addVelocity(double dx, double dy) {
        vx += dx;
        vy += dy;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public int getRadius() { return radius; }
}
