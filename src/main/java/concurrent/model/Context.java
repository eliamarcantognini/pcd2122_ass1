package concurrent.model;

/**
 * Context class represents context of the simulation. It contains information about time and limits of two-dimensional
 * world. It also allows to know if simulation is ended or not. It owns two {@link concurrent.model.BodiesSharedList} to use
 * one to read and the other to write.
 *
 * @see concurrent.model.BodiesSharedList
 * @see concurrent.model.Boundary
 */

public class Context {

    private final double dt;
    private final Boundary boundary;
    private boolean keepWorking;
    private BodiesSharedList writeSharedList;
    private BodiesSharedList readSharedList;

    /**
     * Create the context with boundaries and deltaT passed
     *
     * @param boundary - boundaries of two-dimensional world
     * @param dt       - the deltaT, that is quantity time added to virtual time each iteration
     */
    public Context(final Boundary boundary, final double dt) {
        this.boundary = boundary;
        this.dt = dt;
        this.restartContext();
    }

    /**
     * Restart context reinitializing lists and keepWorking flag
     */
    public void restartContext() {
        this.keepWorking = true;
        this.writeSharedList = new BodiesSharedList();
        this.readSharedList = new BodiesSharedList();
    }

    /**
     * Get boundaries of two-dimensional world
     *
     * @return - boundaries of two-dimensional world
     */
    public Boundary getBoundary() {
        return boundary;
    }

    /**
     * Check if simulation is running yet
     *
     * @return true if simulation is running yet
     */
    public boolean isKeepWorking() {
        return keepWorking;
    }

    /**
     * Set if simulation can run
     *
     * @param keepWorking - to set if simulation can run
     */
    public void setKeepWorking(boolean keepWorking) {
        this.keepWorking = keepWorking;
    }

    /**
     * Get the list used to write new bodies
     *
     * @return the list used to write new bodies
     */
    public BodiesSharedList getWriteSharedList() {
        return writeSharedList;
    }

    /**
     * Get the list used to read bodies
     *
     * @return the list used to read bodies
     */
    public BodiesSharedList getReadSharedList() {
        return readSharedList;
    }

    /**
     * Get the deltaT of the simulation
     *
     * @return the deltaT, that is quantity time added to virtual time each iteration
     */
    public double getDT() {
        return this.dt;
    }
}
