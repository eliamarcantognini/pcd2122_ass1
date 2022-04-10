package concurrent.view;

import concurrent.controller.GUIListener;
import concurrent.model.Body;
import concurrent.model.Boundary;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface to be implemented by the classes that are designed to be a View for the system.
 */
public interface View {
    /**
     * Display the system status in some way.
     *
     * @param bodies - the list of bodies to show
     * @param vt - the virtual time passed since the beginning of the simulation
     * @param iter - the current iterazione to be displayed
     * @param bounds - the {@link Boundary} that indicate the 2D plan where the bodies can move
     */
    void display(List<Body> bodies, double vt, long iter, Boundary bounds);

    /**
     * Method called by the controller to say if it's possible to stop the simulation.
     *
     * @param enabled - is possible to stop the simulation
     */
    void setStopEnabled(final Boolean enabled);

    /**
     * Method called by the controller to say if it's possible to start the simulation.
     *
     * @param enabled - is possible to start the simulation
     */
    void setStartEnabled(final Boolean enabled);

    /**
     * Method called to set a {@link GUIListener} to respond to certain event.
     *
     * @param listener - the listener to notify when the event happen
     */
    void addListener(final GUIListener listener);
}
