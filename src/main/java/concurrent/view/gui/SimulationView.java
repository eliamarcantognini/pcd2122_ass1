package concurrent.view.gui;

import concurrent.controller.ViewListener;
import concurrent.model.Body;
import concurrent.model.Boundary;
import concurrent.model.Commands;
import concurrent.view.View;

import javax.swing.*;
import java.util.List;
import java.awt.*;
import java.awt.event.*;

/**
 * Simulation view
 *
 * @author aricci
 */
public class SimulationView implements View {

    private final VisualiserFrame frame;
    private JButton btnStart;
    private JButton btnStop;

    /**
     * Creates a view of the specified size (in pixels)
     *
     * @param w width
     * @param h height
     */
    public SimulationView(int w, int h) {
        frame = new VisualiserFrame(w, h);
    }

    @Override
    public void display(List<Body> bodies, double vt, long iter, Boundary bounds) {
        frame.display(bodies, vt, iter, bounds);
    }

    public void setStopEnabled(final Boolean enabled){
        this.btnStop.setEnabled(enabled);
    }

    public void setStartEnabled(final Boolean enabled){
        this.btnStart.setEnabled(enabled);
    }

    public void addListener(final ViewListener listener) {
        btnStart.addActionListener(e -> listener.eventPerformed(Commands.START));
        btnStop.addActionListener(e -> listener.eventPerformed(Commands.STOP));
    }

    public class VisualiserFrame extends JFrame {

        private final VisualiserPanel panel;

        public VisualiserFrame(int w, int h) {
            setTitle("Bodies Simulation");
            setSize(w, h);
            setResizable(false);
            panel = new VisualiserPanel(w, h);

            JPanel btnPanel = new JPanel();
            btnPanel.setLayout(new FlowLayout());
            btnStart = new JButton("START");
            btnStop = new JButton("STOP");
            btnPanel.add(btnStart);
            btnPanel.add(btnStop);

            getContentPane().setLayout(new BorderLayout());
            getContentPane().add(btnPanel, BorderLayout.NORTH);
            getContentPane().add(panel, BorderLayout.CENTER);
            addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent ev) {
                    System.exit(-1);
                }

                public void windowClosed(WindowEvent ev) {
                    System.exit(-1);
                }
            });
            this.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {}
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == 38) {        /* KEY UP */
                        updateScale(1.1);
                        SwingUtilities.invokeLater(frame::repaintView);
                    } else if (e.getKeyCode() == 40) {    /* KEY DOWN */
                        updateScale(0.9);
                        SwingUtilities.invokeLater(frame::repaintView);
                    }
                }
                @Override
                public void keyReleased(KeyEvent e) {}
            });
            this.setVisible(true);
        }

        public void display(List<Body> bodies, double vt, long iter, Boundary bounds) {

            this.repaintView();
            try {
                SwingUtilities.invokeAndWait(() -> {
                    panel.display(bodies, vt, iter, bounds);
                    repaint();
                });
            } catch (Exception ignored) {
            }
        }

        public void updateScale(double k) {
            panel.updateScale(k);
        }

        public void repaintView(){
            setFocusable(true);
            setFocusTraversalKeysEnabled(false);
            requestFocusInWindow();
            repaint();
        }
    }
}
