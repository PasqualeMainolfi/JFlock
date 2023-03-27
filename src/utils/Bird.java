package utils;
import processing.core.PVector;
import processing.core.PApplet;
import java.util.ArrayList;


public class Bird {

    PApplet parent;
    protected PVector pos, vel, acc;
    protected double mass;
    protected int check_radius;
    private double max_speed;
    private double max_force;
    private double separation_mag;
    private double cohesion_mag;
    private double alignment_mag;

    public Bird(PApplet parent, double[] pos, double mass, int check_radius, double max_speed, double max_force) {

        this.parent = parent;
        this.pos = new PVector((float) pos[0], (float) pos[1]);
        this.vel = PVector.random2D();
        this.vel.setMag(this.parent.random(3, 6));
        this.acc = new PVector(0, 0);

        this.mass = mass;
        this.max_speed = (float) max_speed;
        this.max_force = (float) max_force;
        this.check_radius = check_radius;

        this.separation_mag = 0.9;
        this.alignment_mag = 1.6;
        this.cohesion_mag = 0.7;

    }

    public void apply_force(PVector force) {
        this.acc.add(force.div((float) this.mass));
    }

    private void get_force(PVector f) {
        f.setMag((float) this.max_speed);
        f.sub(this.vel);
        f.limit((float) this.max_force);
    }

    public void interact(ArrayList<Bird> birds) {

        int count = 0;
        PVector cohesion = new PVector(0, 0);
        PVector separation = new PVector(0, 0);
        PVector alignment = new PVector(0, 0);

        for (var bird : birds) {
            double d = PVector.dist(this.pos, bird.pos);
            if (this != bird && d < this.check_radius + bird.mass) {
                PVector diff = PVector.sub(this.pos, bird.pos);
                diff.normalize();
                diff.div((float) (d * d));
                separation.add(diff);
                
                cohesion.add(bird.pos);
                alignment.add(bird.vel);
                count++;
            }
        }

        if (count > 1) {
            cohesion.div(count);
            cohesion.sub(this.pos);
            this.get_force(cohesion);

            alignment.div(count);
            this.get_force(alignment);

            separation.div(count);
            this.get_force(separation);
        }

        separation.mult((float) this.separation_mag);
        alignment.mult((float) this.alignment_mag);
        cohesion.mult((float) this.cohesion_mag);

        this.apply_force(cohesion);
        this.apply_force(separation);
        this.apply_force(alignment);
    }

    public void set_separation_mag(double value) {
        this.separation_mag = value;
    }

    public void set_alignment_mag(double value) {
        this.alignment_mag = value;
    }

    public void set_cohesion_mag(double value) {
        this.cohesion_mag = value;
    }


    private PVector seek_force(PVector target_pos) {

        PVector force = PVector.sub(target_pos, this.pos);
        force.setMag((float) this.max_speed);
        force.sub(this.vel);
        force.limit((float) max_force);
        return force;

    }

    public void seek(PVector targee_pos) {
        PVector force = this.seek_force(targee_pos);
        this.apply_force(force);
    }

    public void flee(PVector targee_pos) {
        PVector force = this.seek_force(targee_pos);
        this.apply_force(force.mult(-100));
    }

    public void update_state() {

        this.vel.add(this.acc);
        this.vel.limit((float) this.max_speed);
        this.pos.add(this.vel);
        this.acc.mult(0);

        this.wrap();

    }

    private void wrap() {

        if (this.pos.x < 0) {
            this.pos.x = this.parent.width;
        }

        if (this.pos.x > this.parent.width) {
            this.pos.x = 0;
        }

        if (this.pos.y < 0) {
            this.pos.y = this.parent.height;
        }

        if (this.pos.y > this.parent.height) {
            this.pos.y = 0;
        }
    }


    public void show() {
        this.parent.fill(0, 0, 0, 91);
        this.parent.noStroke();
        this.parent.circle(this.pos.x, this.pos.y, (float) (this.mass * 2));
    }

    public void show_web(ArrayList<Bird> neighs) {
        for (var b : neighs) {
            if (this != b) {
                parent.stroke(0);
                parent.strokeWeight(1);
                parent.line(this.pos.x, this.pos.y, b.pos.x, b.pos.y);
            }
        }
    }

}
