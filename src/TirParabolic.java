import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.sound.SoundFile;

public class TirParabolic extends PApplet {
    public static void main(String[] args){
        PApplet.main("TirParabolic");
    }

    // Declaració objectes o variables de classe Target
    Projectil p;
        // Força i altura del canó
    float f = 100;
    float h = 400;
        // Temps, gravetat i disparat (boolean)
    float t = 0;
    final float g = 9.8f;
    boolean disparat = false;
        // Array de Targets
    Target[] targets;
        //
    int numShots = 0, numPoints = 0, numTargets = 0;
        // Fonts
    PFont f1, f2;
        // Sons
    SoundFile soCano, soImpacte;

    // Mètodes bàsics processing
    public void settings(){
        size(1400, 800);
    }
    public void setup(){
        // Instàncies dels objectes de la classe
        p = new Projectil(100, h, 50);
        p.setImgCano(this);

        // Carrega les fonts
        f1 = createFont("EvilEmpire.otf", 34);
        f2 = createFont("Sono.ttf", 14);

        // Carrega els sons
        soCano = new SoundFile(this, "explosio.wav");
        soImpacte = new SoundFile(this, "impacte.wav");

        // Modificació estats
        /*t1.setEstat(Target.ESTAT.EXPLOTAT);
        t2.setEstat(Target.ESTAT.FALLAT);*/
        setTargets(3, 9);
    }
    public void draw(){
        background(220);

        // Display
        /*t1.display(this);
        t2.display(this);
        t3.display(this);*/

        // Dibuixa estadístiques
        displayInfo();

        // NO disparat: configura posició, força i direcció del canó
        if(!disparat){
            float a = map(mouseY, this.h-100, this.h+100, 0, -PI/4); // Actualització canó
            p.setProperties(a, f, h);
        }// Disparat i el projectil està DINS del camp de joc
        else if(disparat && p.x <= width && p.y <= height) {
            p.update(t, g);  // Actualització posició projectil
            t += 0.1;        // Actualització temps

            // Comprovació col·lisió projectil amb targets
            for (int i = 0; i < targets.length; i++) {
                if (targets[i].estat != Target.ESTAT.EXPLOTAT &&
                        targets[i].esImpactatPer(this, p)) {
                    targets[i].setEstat(Target.ESTAT.EXPLOTAT);
                    numPoints++;
                    soImpacte.play();
                }
            }
        }// Disparat i el projectil ha SORTIT del camp de joc
        else if(disparat && (p.x > width || p.y > height)){
            // Resta d'objectius a fallats
            for(int i=0; i<targets.length; i++){
                if(targets[i].estat == Target.ESTAT.PENDENT){
                    targets[i].setEstat(Target.ESTAT.FALLAT);
                }
            }
            // Missatge per resetear posició objectius
            textAlign(CENTER); textSize(36); fill(255, 0, 0);
            text("Press R key to set up the next scenario", width/2, height/2);

        }
        // Dibuixa projectil
        p.display(this);

        // Dibuixa targets
        for(int i=0; i<targets.length; i++){
            targets[i].display(this);
        }
    }

    public void keyPressed(){
        // Augmenta força dispar
        if(keyCode == RIGHT){
            f += 10;
            f = constrain(f, 10, 300);
        }

        // Disminueix força dispar
        if(keyCode == LEFT){
            f -= 10;
            f = constrain(f, 10, 300);
        }

        if(keyCode == UP){ h -= 10; h = constrain(h, 0, height); } // Augmenta altura canó

        if(keyCode == DOWN){ h += 10; h = constrain(h, 0, height);} // Disminueix altura canó

        // Dispara canó
        if(key == 's' || key == 'S'){
            if(!disparat){
                numShots++;
                soCano.play();
            }
            disparat = true;
        }

        // Reset posició objectius
        if(key == 'r' || key == 'R'){
            disparat = false;
            t = 0;
            setTargets(3, 12);
        }
    }

    void setTargets(int n1, int n2){
        // Num (d'entre n1 i n2) aleatori d'objectius
        int nt = (int) random(n1, n2);
        targets = new Target[nt];

        // Posicionament dels targets
        for(int i=0; i<nt; i++){
            float x = random(width/2, width);
            float y = random(height/5, 4*height/5);
            float r = random(20, 60);
            targets[i] = new Target(x, y, r);
        }
        numTargets += nt;
    }

        // Estadístiques i instruccions
    void displayInfo(){
        // Títol
        fill(0); textAlign(LEFT); textSize(24);
        textFont(f1);
        text("Tir Parabòlic", 50, 50);

        // Marcador
        fill(0); textAlign(RIGHT);
        text("Score", width-50, 50);
        textSize(14);
        String percentage = nf(100*(numPoints/(float)numTargets), 2, 2);
        text("Rate: "+ percentage +"%", width-50, 80);
        text("Hits: "+ numPoints +" / "+ numTargets, width-50, 100);
        text("Shots: "+ numShots, width-50, 120);

        // Instruccions
        fill(0); textSize(14); textAlign(LEFT);
        textFont(f2);
        text("Press 'S' to shoot cannon.", 50, height-90);
        text("Use MOUSE to set cannon direction.", 50, height-70);
        text("Press ARROW KEYS to set up cannon.", 50, height-50);
        text("UP: move up, DOWN: move down, LEFT: decrease force, RIGHT: increase force.", 50, height-30);
    }
}
