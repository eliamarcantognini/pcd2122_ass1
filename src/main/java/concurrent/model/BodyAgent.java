package concurrent.model;

public class BodyAgent extends Thread{
    private final Body body;

    public BodyAgent(final Body body){
        this.body = body;
    }

    @Override
    public void run() {
        System.out.println(this.body);
    }
}
