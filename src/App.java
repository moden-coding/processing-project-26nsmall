import processing.core.*;
import processing.sound.*;



public class App extends PApplet {
    SoundFile file;
    int GlobalTimer = 0;
    int heresacolor = color(0, 255, 0);
    int AproachRate = 3;
    float CircleSize = 100;
    float bpm = 160;
    float fpb = 3600 / bpm;
    int globalId = 0;
    boolean CircleClick = false;
    int hp = 750;
    int failTimer = 200;
    int score = 0;
    int misses = 0;
    int combo = 0;
    int hits = 0;
    int STUPIDBUGFIX = 1;
    double accuracy = 100 - ((misses / (hits + misses + STUPIDBUGFIX)) * 100);
    int maximumCombo = 0;
    int mapCombo = 6;
    boolean notFailed = true;

    public static void main(String[] args) {
        PApplet.main("App");

    }

    public void WIN() {
        fill(0);
        text((int) (score * AproachRate * (100 / CircleSize) * (bpm / 200)), 600, 150);
        text(Double.toString(accuracy) + "%", 150, 600);
        text(Integer.toString(maximumCombo) + "/" + Integer.toString(mapCombo), 1000, 600);
        fill(-16711936);
        text(hits, 300, 375);
        fill(255, 0, 0);
        text(misses, 1200, 375);
    }

    public void clickStuff() {
        if (CircleClick) {
            globalId++;
            hp += 50;
            if (hp >= 750) {
                hp = 750;
            }
            combo++;
            score += 100 * combo;
            hits++;
            STUPIDBUGFIX = 0;
        }

    }

    public void setup() {

    }

    public void settings() {
        size(1500, 750);
        file = new SoundFile(this, "GravityFalls.mp3");
        file.play();

    }

    public void CircleTime(float SpawnTime, int xLoc, int yLoc, int id) {
        if (SpawnTime - GlobalTimer <= ((300 / ((float) 3 * AproachRate))) && SpawnTime - GlobalTimer >= (fpb / 4)) {
            fill(255);
            strokeWeight(10);
            ellipse(xLoc, yLoc, ((CircleSize - (fpb / 8) + AproachRate * (SpawnTime - GlobalTimer))),
                    ((CircleSize - (fpb / 8) + AproachRate * (SpawnTime - GlobalTimer))));
        }
        strokeWeight(4);
        if ((SpawnTime - GlobalTimer) <= (2 * fpb / AproachRate) && globalId < id
                && (SpawnTime - GlobalTimer) >= -(fpb / 2)) {

            fill(255, 0, 0);
            if (Math.abs(SpawnTime - GlobalTimer) <= (fpb / 2)) {
                fill(0, 255, 0);
            }
            ellipse(xLoc, yLoc, CircleSize, CircleSize);

        }
        if (GlobalTimer - SpawnTime > (fpb / 2) && globalId - id == -1) {
            fill(255, 0, 0);

            text("MISS", 0, 70);
            if (GlobalTimer - SpawnTime > (3 * fpb / 4)) {
                globalId = id;
                hp -= 150;
                misses++;
                combo = 0;
                STUPIDBUGFIX = 0;
            }

        } else if (globalId == id && GlobalTimer - SpawnTime < (fpb / 2)) {
            fill(0, 255, 0);
            text("HIT", 0, 120);
        }

    }

    public void draw() {

        textSize(50);
        CircleClick = false;
        background(255);
        GlobalTimer++;
        accuracy = (double) Math.round((100 - ((misses / ((double) (hits + misses + STUPIDBUGFIX))) * 100)) * 100d)
                / 100d;

        if (hp > 0) {

            CircleTime(fpb * (10), 100, 500, 6);
            CircleTime(fpb * (9), 500, 100, 5);
            CircleTime(fpb * (8), 100, 100, 4);
            CircleTime(fpb * (7), 100, 500, 3);
            CircleTime(fpb * (6), 500, 100, 2);
            CircleTime(fpb * (5), 100, 100, 1);
            fill(0);

        } else {
            notFailed = false;
            textSize(150);
            fill(255, 0, 0);
            text("FAIL", 600, 375);
            failTimer--;
            if (failTimer <= 0) {
                System.exit(0);
            }

        }

        if (get(mouseX, mouseY) == -16711936) {
            CircleClick = true;
        }

        if (GlobalTimer > 500 && notFailed) {
            textSize(100);
            if (misses == 0) {
                fill(0, 255, 0);
                textSize(150);
                text("PERFECT", 500, 375);
                WIN();
            } else {
                textSize(150);
                fill(0, 255, 0);
                text("WIN", 600, 375);
                WIN();

            }
        } else if (notFailed) {

            fill(0, 255, 0);
            rect(0, 0, hp, 20);
            fill(0);
            text((int) (score * AproachRate * (100 / CircleSize) * (bpm / 200)), 1350, 50);

            text(Double.toString(accuracy) + "%", 1350, 100);
            if (combo > 0) {
                if (combo > maximumCombo) {
                    maximumCombo = combo;
                }
                text("X" + combo, 1300, 700);

            }
        }

        fill(0, 0, 255);
        ellipse(mouseX, mouseY, 50, 50);
        if (hp >= 750) {
            hp = 750;
        }

    }

    public void mousePressed() {
        clickStuff();
    }

    public void keyPressed() {
        if (key == 'z') {
            clickStuff();

        }

        if (key == 'x') {
            clickStuff();

        }
        if (key == CODED) {
            if (keyCode == UP) {
                clickStuff();
            }
        }
    }
}
