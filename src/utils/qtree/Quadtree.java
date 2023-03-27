package utils.qtree;
import java.util.ArrayList;


public class Quadtree {

    private Rectangle boundary;
    private int capacity;
    private boolean is_divided;
    private Quadtree[] nodes;
    private ArrayList<Point> points;

    public Quadtree(Rectangle boundary, int capacity) {
    
        this.boundary = boundary;
        this.capacity = capacity;
        this.points = new ArrayList<>();
        this.nodes = new Quadtree[4];
        this.is_divided = false;
    
    }

    private void subdivide() {

        double x = this.boundary.x;
        double y = this.boundary.y;
        double w = this.boundary.w/2;
        double h = this.boundary.h/2;

        this.nodes[0] = new Quadtree(new Rectangle(x + w, y - h, w, h), this.capacity);
        this.nodes[1] = new Quadtree(new Rectangle(x - w, y - h, w, h), this.capacity);
        this.nodes[2] = new Quadtree(new Rectangle(x + w, y + h, w, h), this.capacity);
        this.nodes[3] = new Quadtree(new Rectangle(x - w, y + h, w, h), this.capacity);

        this.is_divided = true;

    }

    public boolean insert(Point point) {

        if (!this.boundary.contains(point)) {
            return false;
        }

        if (this.points.size() < this.capacity) {
            this.points.add(point);
            return true;
        }

        if (!this.is_divided) {
            this.subdivide();
        }

        for (var node : this.nodes) {
            if (node.insert(point)) {
                return true;
            }
        }

        return false;

    }

    public ArrayList<Point> query(Rectangle range, double radius, ArrayList<Point> found) {

        if (found == null) {
            found = new ArrayList<>();
        }

        if (!range.intersects(this.boundary)) {
            return found;
        }
        
        for (Point p : this.points) {
            double dx = p.x - range.x;
            double dy = p.y - range.y;
            double d = Math.sqrt((dx * dx) + (dy * dy));
            if (range.contains(p) && d < radius) {
                found.add(p);
            }
        }
        
        if (this.is_divided) {
            for (var node : this.nodes) {
                node.query(range, radius, found);
            }
        }
        
        return found;

    }

    public void clear() {

        this.points = new ArrayList<>();
        this.nodes = new Quadtree[4];
        this.is_divided = false;

    }

    


}
