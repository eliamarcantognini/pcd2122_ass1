package concurrent.model;

import java.io.IOException;

public class ProveJava {
    public static void main(String[] args) throws IOException {
        ReadConfiguration rD = new ReadConfiguration("config.properties");
        System.out.println("prop:" + rD.getDT());
        System.out.println("prop:" + rD.getBodiesQuantity());
        System.out.println("prop:" + rD.getIterationsQuantity());
        System.out.println("prop:" + rD.getLefterBoundary());
        System.out.println("prop:" + rD.getRighterBoundary());
        System.out.println("prop:" + rD.getUpperBoundary());
        System.out.println("prop:" + rD.getLowerBoundary());
    }
}
