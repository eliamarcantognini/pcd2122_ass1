package concurrent.model;

import java.util.ArrayList;
import java.util.List;

public class SyncList {

    private List<Body> updatedBodies;

    public SyncList() {
        updatedBodies = new ArrayList<>();
    }

    public synchronized List<Body> getBodies() {
        return updatedBodies;
    }

    public synchronized void reset() {
        updatedBodies = new ArrayList<>();
    }

    public synchronized void updateBody(Body body) {
        updatedBodies.add(body);
    }
}
