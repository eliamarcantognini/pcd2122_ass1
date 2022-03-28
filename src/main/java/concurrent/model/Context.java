package concurrent.model;

public class Context {

    public final static double DT = 0.001;

    private Boundary boundary;
    private boolean keepWorking;

    public Context() {
        this.boundary = new Boundary(-6.0, -6.0, 6.0, 6.0);
        this.keepWorking = true;
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
}
