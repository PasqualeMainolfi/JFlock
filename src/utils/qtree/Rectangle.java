package utils.qtree;

public class Rectangle {

    public double x;
    public double y;
    public double w;
    public double h;
    private double left;
    private double right;
    private double top;
    private double bottom;

    public Rectangle(double x, double y, double w, double h) {
        
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        this.left = this.x - this.w;
        this.right = this.x + this.w;
        this.top = this.y - this.h;
        this.bottom = this.y + this.h;

    }

    public boolean contains(Point point) {

        return (
            point.x <= this.right &&
            point.x >= this.left &&
            point.y <= this.bottom &&
            point.y >= this.top 
        );

    }

    public boolean intersects(Rectangle range) {

        return (
            range.left <= this.right ||
            range.right >= this.left ||
            range.top <= this.bottom ||
            range.bottom >= this.top
        );

    }
    
}
