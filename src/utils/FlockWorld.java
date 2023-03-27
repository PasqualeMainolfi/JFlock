package utils;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;
import utils.qtree.Point;
import utils.qtree.Quadtree;
import utils.qtree.Rectangle;

public class FlockWorld {

    private PApplet parent;
    private int nbirds;
    private int width;
    private int height; 
    public Bird[] birds;
    private Quadtree qt;
    private Rectangle rectangle;

    public FlockWorld(PApplet parent, int n) {

        this.parent = parent;
        this.nbirds = n;
        this.width = this.parent.width;
        this.height = this.parent.height;

        this.rectangle = new Rectangle(this.width/2, this.height/2, this.width/2, this.height/2);
        this.qt = new Quadtree(rectangle, 4);

        this.birds = new Bird[this.nbirds];
        for (int i = 0; i < this.nbirds; i++) {
            double mass = (Math.random() * 2) + 2;
            double[] pos = {Math.random() * this.width, Math.random() * this.height};
            birds[i] = new Bird(this.parent, pos, mass, 50, 7, 2);
        }
    }

    public void flock(boolean touch) {

        this.qt.clear();

        for (int i = 0; i < this.nbirds; i++) {
            Point p = new Point(birds[i].pos.x, birds[i].pos.y, birds[i]);
            this.qt.insert(p);
        }

        for (var bird : this.birds) {
            Rectangle shape = new Rectangle(bird.pos.x, bird.pos.y, bird.check_radius, bird.check_radius);
            ArrayList<Point> founded = this.qt.query(shape, bird.check_radius, null);
            // System.out.println(founded.size());
            
            ArrayList<Bird> neighbors = new ArrayList<>();
            for (var p : founded) {
                neighbors.add(p.data);
            }

            bird.interact(neighbors);
            // bird.show_web(neighbors);

        }

        Rectangle shape = new Rectangle(this.parent.mouseX, this.parent.mouseY, 100, 100);
        ArrayList<Point> founded = this.qt.query(shape,100, null);
        // System.out.println(founded.size());
        
        ArrayList<Bird> neighbors = new ArrayList<>();
        for (var p : founded) {
            neighbors.add(p.data);
        }

        if (touch) {
            for (var b : neighbors) {
                b.flee(new PVector(this.parent.mouseX, this.parent.mouseY));
            }
        }

    }

    public void render() {

        for (var bird : this.birds) {
            bird.show();
            bird.update_state();
        }
    }

}
