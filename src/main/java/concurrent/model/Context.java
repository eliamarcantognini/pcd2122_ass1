package concurrent.model;

public class Context {

    public final static double DT = 0.001;

    private Boundary boundary;
    private boolean keepWorking;
    private BodiesSharedList sharedList;

    public Context() {
        this.boundary = new Boundary(-6.0, -6.0, 6.0, 6.0);
        this.keepWorking = true;
        this.sharedList = new BodiesSharedList();
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

    public BodiesSharedList getSharedList() {
        return sharedList;
    }

    public void resetSharedList() { this.sharedList = new BodiesSharedList();}
}
