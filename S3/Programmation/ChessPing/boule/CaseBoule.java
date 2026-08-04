package boule;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CaseBoule extends JPanel implements MouseListener, MouseMotionListener, ActionListener {
    private final int CASE_SIZE = 200;
    private Ball ball;
    private int mouseX, mouseY; //coordonnées actuelles ou finales de la souris
    private boolean aiming = false;
    private Timer timer = new Timer(16, this);

    //Constructer
    public CaseBoule() {
        ball = new Ball(CASE_SIZE / 2, CASE_SIZE / 2, 12);
        setPreferredSize(new Dimension(CASE_SIZE, CASE_SIZE));
        setBackground(new Color(135, 206, 250));
        addMouseListener(this);
        addMouseMotionListener(this);
        timer.start();
        
    }


    //Ocupe tout le rendu graphique 
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
       
        //Desin case
      //  g2.setColor(Color.BLACK);
        //g2.drawRect(0, 0, CASE_SIZE - 1, CASE_SIZE - 1);

        //Desin Boule
        g2.setColor(Color.RED);
        //Cercle
        g2.fillOval((int)(ball.getX() - ball.getRadius()), (int)(ball.getY() - ball.getRadius()),
                    ball.getRadius() * 2, ball.getRadius() * 2);

        //Si la Boule est toucher
        if(aiming) {
            g2.setColor(Color.BLACK);
            g2.drawLine((int)ball.getX(), (int)ball.getY(), mouseX, mouseY);
        }
    }

    //Animation : vitesse,Vérifie si la boule touche les bords de la case.,redesiner
    @Override
    public void actionPerformed(ActionEvent e) {
        ball.update();
        ball.checkCollision(getWidth(), getHeight());
        repaint();
    }


    //sert à détecter si le joueur clique sur la boule pour commencer à viser.
    @Override
    public void mousePressed(MouseEvent e) {
        double dx = e.getX() - ball.getX();
        double dy = e.getY() - ball.getY();
        //cal la distance entre le clic et le centre de la boule
        if(Math.sqrt(dx*dx + dy*dy) <= ball.getRadius() + 5){
            aiming = true; //ball vise
            mouseX = e.getX();
            mouseY = e.getY();
        }
    }

    //lige de vise
    @Override
    public void mouseDragged(MouseEvent e) {
        if(aiming){
            mouseX = e.getX();
            mouseY = e.getY();
            repaint();
        }
    }

    //relacer la vise et deplacem. du boul
    @Override
    public void mouseReleased(MouseEvent e) {
        if(aiming){
            double dx = mouseX - ball.getX();
            double dy = mouseY - ball.getY();
            ball.addVelocity(dx * 0.05, dy * 0.05);
        }
        aiming = false;
    }

    // Méthodes inutilisées
    public void mouseMoved(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}
