package concurrent.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SharedList {

    private final List<Body> updatedBodies;

    public SharedList() {
        updatedBodies = new ArrayList<>();
    }

    public List<Body> getBodies() {
        return updatedBodies;
    }

    public void updateBody(Body body) {
        updatedBodies.set(body.getId(), body);
    }

    public void addBodies(Collection<Body> collection) {
        updatedBodies.addAll(collection);
    }
}
