import processing.core.*;
import utils.*;


public class App extends PApplet{

    FlockWorld fw;
    boolean is_pressed = false;

    public void settings() {
        fullScreen();
    }

    public void setup() {
        frameRate(60);
        fw = new FlockWorld(this, 2100);
        
    }

    public void draw() {
        background(255);

        fw.flock(is_pressed);
        fw.render();

    }

    public void mousePressed() {
        is_pressed = true;
    }

    public void mouseReleased() {
        is_pressed = false;
    }


    public static void main(String[] args) {
        PApplet.main("App");
    }
}
