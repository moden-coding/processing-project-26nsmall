import processing.core.*;
import processing.sound.*;

public class App extends PApplet {
    SoundFile file;
    boolean SoundPlay = true;
    int GlobalTimer = 0;
    int heresacolor = color(255, 165, 0);
    int AproachRate = 2;
    float CircleSize = 100;
    float bpm = 80;
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
    boolean sliderClick = false;
    float sliderMulti = 1;
    boolean SliderActiveClick = false;

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
            if (hp < 750)
                hp += Math.min(50, 750 - hp);
            combo++;
            score += 100 * combo;
            hits++;
            STUPIDBUGFIX = 0;
        }
        if (sliderClick) {
            sliderMulti = (float) 3 / 2;
            SliderActiveClick = true;
        }

    }

    public void setup() {
        file = new SoundFile(this, "GravityFalls.mp3");

    }

    public void settings() {
        size(1500, 750);

    }

    public void SliderTime(float SpawnTime, float endtime, int xPosStart, int yPosStart, int xPosEnd, int yPosEnd,
            int id) {

        if (SpawnTime - GlobalTimer <= (100 / (float) AproachRate) && SpawnTime - GlobalTimer >= (fpb / 4)) {
            fill(255);
            line(xPosStart, yPosStart, xPosEnd, yPosEnd);
            fill(255);
            strokeWeight(10);
            ellipse(xPosStart, yPosStart, ((CircleSize - (fpb / 8) + AproachRate * (SpawnTime - GlobalTimer))),
                    ((CircleSize - (fpb / 8) + AproachRate * (SpawnTime - GlobalTimer))));
        }
        strokeWeight(4);
        if (globalId < id) {
            if ((SpawnTime - GlobalTimer) <= (fpb / 2) && endtime - GlobalTimer > 0) {
                fill(255);
                line(xPosStart, yPosStart, xPosEnd, yPosEnd);
                fill(255, 165, 0);
                if ((SpawnTime - GlobalTimer) <= 0) {
                    ellipse(xPosStart + ((xPosEnd - xPosStart) * ((GlobalTimer - SpawnTime) / (endtime - SpawnTime))),
                            yPosStart + ((yPosEnd - yPosStart) * ((GlobalTimer - SpawnTime) / (endtime - SpawnTime))),
                            CircleSize * sliderMulti, CircleSize * sliderMulti);
                } else {
                    ellipse(xPosStart, yPosStart, CircleSize * sliderMulti, CircleSize * sliderMulti);

                }

            } else if ((SpawnTime - GlobalTimer) <= (2 * fpb / AproachRate) && endtime - GlobalTimer > 0) {
                fill(255);
                line(xPosStart, yPosStart, xPosEnd, yPosEnd);
                fill(255, 0, 0);
                ellipse(xPosStart, yPosStart, CircleSize, CircleSize);
            }
            if ((SpawnTime - GlobalTimer) <= (-fpb / 2) && sliderMulti != (float) 3 / 2) {

                fill(255, 0, 0);
                text("MISS", 0, 70);
                sliderMulti = 0;
                if (endtime - GlobalTimer < -5) {
                    globalId = id;
                    hp -= 150;
                    misses++;
                    combo = 0;
                    STUPIDBUGFIX = 0;
                }
            } else if (endtime - GlobalTimer <= fpb / 2 && sliderMulti == (float) 3 / 2) {
                fill(0, 255, 0);
                text("HIT", 0, 120);
                if (endtime - GlobalTimer < 1) {
                    sliderMulti = 1;
                    if (hp < 750)
                        hp += Math.min(50, 750 - hp);
                    combo++;
                    score += 100 * combo;
                    hits++;
                    STUPIDBUGFIX = 0;
                    globalId++;
                }

            }

        }

    }

    public void CircleTime(float SpawnTime, int xLoc, int yLoc, int id) {

        if (SpawnTime - GlobalTimer <= (100 / (float) AproachRate) && SpawnTime - GlobalTimer >= (fpb / 4)) {
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
        
          if (SoundPlay) {
          file.play();
          SoundPlay = false;
          }
         
        textSize(50);
        CircleClick = false;
        sliderClick = false;
        background(255);
        GlobalTimer++;
        accuracy = (double) Math.round((100 - ((misses / ((double) (hits + misses + STUPIDBUGFIX))) * 100)) * 100d)
                / 100d;

        if (hp > 0) {
           // SliderTime(fpb * (5), fpb * (12), 100, 100, 500, 550, 1);

            
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
        if (get(mouseX, mouseY) == -23296) {
            sliderClick = true;
        }
        if (get(mouseX, mouseY) != -23296 && SliderActiveClick) {
            SliderActiveClick = false;
            sliderMulti = 0;
        }

        if (GlobalTimer > fpb*13 && notFailed) {
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

    public void mouseReleased() {
        if (sliderClick) {
            sliderMulti = 1;
        }

    }

   /*  public void keyReleased() {
        if (key == 'z') {
            if (sliderClick) {
                sliderMulti = 1;
            }
        }

        if (key == 'x') {
            if (sliderClick) {
                sliderMulti = 1;
            }

        }
        if (key == CODED) {
            if (keyCode == UP) {
                if (sliderClick) {
                    sliderMulti = 1;
                }
            }
        }
    }*/

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
