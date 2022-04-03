package concurrent.view.gui;

import concurrent.model.Body;
import concurrent.model.Boundary;
import concurrent.model.P2d;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class VisualiserPanel extends JPanel{

    private ArrayList<Body> bodies;
    private Boundary bounds;

    private long nIter;
    private double vt;
    private double scale = 1;

    private final long dx;
    private final long dy;

    public VisualiserPanel(int w, int h){
        setSize(w,h);
        dx = w/2 - 20;
        dy = h/2 - 20;
    }

    public void paint(Graphics g){
        if (bodies != null) {
            Graphics2D g2 = (Graphics2D) g;

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);
            g2.clearRect(0,0,this.getWidth(),this.getHeight());


            int x0 = getXcoord(bounds.getX0());
            int y0 = getYcoord(bounds.getY0());

            int wd = getXcoord(bounds.getX1()) - x0;
            int ht = y0 - getYcoord(bounds.getY1());

            g2.drawRect(x0, y0 - ht, wd, ht);

            bodies.forEach( b -> {
                P2d p = b.getPos();
                int radius = (int) (10*scale);
                if (radius < 1) {
                    radius = 1;
                }
                g2.drawOval(getXcoord(p.getX()),getYcoord(p.getY()), radius, radius);
            });
            String time = String.format("%.2f", vt);
            g2.drawString("Bodies: " + bodies.size() + " - vt: " + time + " - nIter: " + nIter + " (UP for zoom in, DOWN for zoom out)", 2, 20);
        }
    }

    private int getXcoord(double x) {
        return (int)(dx + x*dx*scale);
    }

    private int getYcoord(double y) {
        return (int)(dy - y*dy*scale);
    }

    public void display(ArrayList<Body> bodies, double vt, long iter, Boundary bounds){
        this.bodies = bodies;
        this.bounds = bounds;
        this.vt = vt;
        this.nIter = iter;
    }

    public void updateScale(double k) {
        scale *= k;
    }

}
