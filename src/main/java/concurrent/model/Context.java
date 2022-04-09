package concurrent.model;

/**
 * Context class represents context of the simulation. It contains information about time and limits of two-dimensional
 * world. It also allows to know if simulation is ended or not. It owns two {@link concurrent.model.BodiesSharedList} to use
 * one to read and the other to write.
 *
 * @see concurrent.model.BodiesSharedList
 */

public class Context {

    public final static double DT = 0.001;

    private Boundary boundary;
    private boolean keepWorking;
    private final BodiesSharedList writeSharedList;
    private final BodiesSharedList readSharedList;

    public Context() {
        this.boundary = new Boundary(-6.0, -6.0, 6.0, 6.0);
        this.keepWorking = true;
        this.writeSharedList = new BodiesSharedList();
        this.readSharedList = new BodiesSharedList();
    }

    public Boundary getBoundary() {
        return boundary;
    }

    public void setBoundary(Boundary boundary) {
        this.boundary = boundary;
    }

    public boolean isKeepWorking() {
        return keepWorking;
    }

    public void setKeepWorking(boolean keepWorking) {
        this.keepWorking = keepWorking;
    }

    public BodiesSharedList getWriteSharedList() {
        return writeSharedList;
    }

    public BodiesSharedList getReadSharedList() {
        return readSharedList;
    }

//    public void resetWriteSharedList() { this.writeSharedList = new BodiesSharedList();}

}
