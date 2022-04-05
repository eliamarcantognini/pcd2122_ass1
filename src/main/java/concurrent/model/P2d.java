package concurrent.model;

public class P2d {

    private double x, y;

    public P2d(double x,double y){
        this.x = x;
        this.y = y;
    }

    public P2d(P2d p) {
        this.x = p.getX();
        this.y = p.getY();
    }

    public P2d sum(V2d v) {
    	x += v.x;
    	y += v.y;
    	return this;
    }
     
    public void change(double x, double y){
    	this.x = x;
    	this.y = y;
    }

    public double getX() {
    	return x;
    }

    public double getY() {
    	return y;
    }

    @Override
    public String toString() {
        return "P2d{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
