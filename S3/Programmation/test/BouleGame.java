import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BouleGame extends JPanel implements MouseListener, MouseMotionListener, ActionListener {
    private final int CASE_SIZE = 200; // Taille de la case
    private final int BALL_RADIUS = 12;

    private double ballX = CASE_SIZE / 2.0;
    private double ballY = CASE_SIZE / 2.0;

    private double vx = 0;
    private double vy = 0;

    private int mouseX, mouseY;
    private boolean aiming = false;

    private Timer timer = new Timer(16, this);

    public BouleGame() {
        setPreferredSize(new Dimension(CASE_SIZE, CASE_SIZE));
        setBackground(new Color(135, 206, 250)); // couleur de la case
        addMouseListener(this);
        addMouseMotionListener(this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Bord de la case
        g2.setColor(Color.BLACK);
        g2.drawRect(0, 0, CASE_SIZE - 1, CASE_SIZE - 1);

        // Boule
        g2.setColor(Color.RED);
        g2.fillOval((int)(ballX - BALL_RADIUS), (int)(ballY - BALL_RADIUS), BALL_RADIUS * 2, BALL_RADIUS * 2);

        // Flèche
        if(aiming){
            g2.setColor(Color.BLACK);
            g2.drawLine((int)ballX, (int)ballY, mouseX, mouseY);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ballX += vx;
        ballY += vy;

        vx *= 0.98;
        vy *= 0.98;

        // Collision avec la case
        if(ballX - BALL_RADIUS < 0){ ballX = BALL_RADIUS; vx = -vx * 0.8; }
        if(ballX + BALL_RADIUS > CASE_SIZE){ ballX = CASE_SIZE - BALL_RADIUS; vx = -vx * 0.8; }
        if(ballY - BALL_RADIUS < 0){ ballY = BALL_RADIUS; vy = -vy * 0.8; }
        if(ballY + BALL_RADIUS > CASE_SIZE){ ballY = CASE_SIZE - BALL_RADIUS; vy = -vy * 0.8; }

        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        double dx = e.getX() - ballX;
        double dy = e.getY() - ballY;
        if(Math.sqrt(dx*dx + dy*dy) <= BALL_RADIUS + 5){
            aiming = true;
            mouseX = e.getX();
            mouseY = e.getY();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(aiming){
            mouseX = e.getX();
            mouseY = e.getY();
            repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(aiming){
            double dx = mouseX - ballX;
            double dy = mouseY - ballY;

            vx += dx * 0.05;
            vy += dy * 0.05;
        }
        aiming = false;
    }

    public void mouseMoved(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Case avec boule");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new BouleGame());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
