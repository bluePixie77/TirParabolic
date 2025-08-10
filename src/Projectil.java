import processing.core.PApplet;
import processing.core.PImage;

import static processing.core.PApplet.cos;
import static processing.core.PApplet.sin;

public class Projectil {

    // Atributs o propietats
    float x0, y0; // Posició canó (posició inicial projectil)
    float x, y;   // Posició del projectil (en un instant de temps determinat)
    float f, a;   // Paràmetres del dispar del projectil (força i angle)
    float v0x, v0y, vy; // Velocitat del projectil
    float r = 10; // Radi projectil
    PImage imgCano;

    // Constructor
    Projectil(float x, float h, float f){
        // Posició del canó
        this.x0 = x;
        this.y0 = h;

        // Posició del projectil
        this.x = x;
        this.y = h;

        // Força i angle del dispar
        this.f = f;
        this.a = 0;

        // Velocitat
        this.v0x = + f * cos(this.a);
        this.v0y = - f * sin(this.a);
        this.vy = this.v0y;
    }

    // Setters
    void setProperties(float a, float f, float h){
        // Posició (y) inicial (canó i projectil)
        this.x = 100;
        this.y = h;
        this.y0 = h;

        // Angle i força
        this.a = a;
        this.f = f;

        // Velocitats (x i y)
        this.v0x = + this.f * cos(this.a);
        this.v0y = - this.f * sin(this.a);
        this.vy = this.v0y;
    }
    void setImgCano(PApplet p5){
        this.imgCano = p5.loadImage("cannon.png");
    }

    // Altres mètodes
    void display(PApplet p5){
        float dx = this.x0 + this.f * cos(this.a);
        float dy = this.y0 - this.f * sin(this.a);

        p5.pushStyle();
            // Cos del canó
            p5.imageMode(p5.CENTER);
            p5.pushMatrix();
                p5.translate(this.x0, this.y0);
                p5.scale(0.15f, 0.15f);
                p5.rotate(-this.a);
                p5.image(this.imgCano, 0, 210);
            p5.popMatrix();

            // Projectil
            p5.noStroke(); p5.fill(255, 0, 0);
            p5.circle(this.x, this.y,10);

            // Paràmetres del projectil (posició, velocitat i força)
            p5.fill(0); p5.textSize(14); p5.textAlign(p5.LEFT);
            p5.text("Sx: "+p5.nf(this.x, 3, 2)+"  Sy: "+p5.nf(this.y, 3, 2), 55, 80);
            p5. text("Vx: "+p5.nf(this.v0x, 2, 2)+"  Vy: "+p5.nf(this.vy, 2, 2), 55, 100);
            p5.text("F: "+p5.nf(this.f, 2, 2), 55, 120);
        p5.popStyle();
    }

    void update(float t, float g){  // temps (t) i gravetat (g)

        this.x = this.x0 + this.v0x * t;
        this.y = this.y0 + this.vy*t - 0.5f*g*t*t;

        this.vy = this.v0y + g*t;
    }
}
