package concurrent.view.gui;

import concurrent.model.Body;
import concurrent.model.Boundary;
import concurrent.view.View;

import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

/**
 * Simulation view
 *
 * @author aricci
 *
 */
public class SimulationView implements View {
        
	private final VisualiserFrame frame;
	private ActionListener listener;

	/**
     * Creates a view of the specified size (in pixels)
     * 
     * @param w
     * @param h
     */
    public SimulationView(int w, int h){
    	frame = new VisualiserFrame(w,h);
    }
        
    @Override
	public void display(ArrayList<Body> bodies, double vt, long iter, Boundary bounds){
 	   frame.display(bodies, vt, iter, bounds); 
    }
    
    public class VisualiserFrame extends JFrame {

        private final VisualiserPanel panel;

        public VisualiserFrame(int w, int h){
            setTitle("Bodies Simulation");
            setSize(w,h);
            setResizable(false);
            panel = new VisualiserPanel(w,h);
			new JButton("ciao").addActionListener(listener);
            getContentPane().add(panel);
            addWindowListener(new WindowAdapter(){
    			public void windowClosing(WindowEvent ev){
    				System.exit(-1);
    			}
    			public void windowClosed(WindowEvent ev){
    				System.exit(-1);
    			}
    		});
    		this.setVisible(true);
        }
        
        public void display(ArrayList<Body> bodies, double vt, long iter, Boundary bounds){
        	try {
	        	SwingUtilities.invokeAndWait(() -> {
	        		panel.display(bodies, vt, iter, bounds);
	            	repaint();
	        	});
        	} catch (Exception ignored) {}
        };
        
        public void updateScale(double k) {
        	panel.updateScale(k);
        }    	
    }

	public void addListener(ActionListener listener) {
		this.listener = listener;

	}

}
