import processing.core.*;
import processing.sound.*;

public class App extends PApplet {
    SoundFile GravityFalls;
    boolean InstructionsOnScreen = false;
    long previousFrameTime = 0;
    int songLength = 0;
    boolean SoundPlay = true;
    // global frame counter for timing
    float GlobalTimer = 0;
    // handles how fast the circle closes in
    int AproachRate = 0;
    // handles the size of the circles
    float CircleSize = 0;
    // handles the speed things spawn in based on the bpm of the music
    float bpm = 0;
    float fpb = 0;
    int globalId = 0;
    // handles if a circle is able to be clicked
    boolean CircleClick = false;
    // handles the hp amount
    int hp = 750;
    int failTimer = 200;
    int score = 0;
    int misses = 0;
    int combo = 0;
    int hits = 0;
    // fixes a nan bug and this was the best thing i could thing of so this is dumb.
    int STUPIDBUGFIX = 1;
    double accuracy = 100 - ((misses / (hits + misses + STUPIDBUGFIX)) * 100);
    int maximumCombo = 0;
    int mapCombo = 6;
    // handles if you are still alive
    boolean notFailed = true;
    // handles if sliders are clickable
    boolean sliderClick = false;
    // handles the size of sliders
    float sliderMulti = 1;
    // handles if a slider is being clicked.
    boolean SliderActiveClick = false;
    // These variables check if a map is started and which map is staryed
    boolean GravityFallsMapHard = false;
    boolean anyMapStarted = false;
    boolean GravityFallsMapEasy = false;

    public static void main(String[] args) {
        PApplet.main("App");

    }

    // this function handles drawing the menu before the game starts
    public void menu() {
        if (anyMapStarted == false) {
            if (InstructionsOnScreen == false) {
                fill(255, 0, 0);
                ellipse(1100, 375, 300, 300);
                fill(0, 255, 0);
                ellipse(400, 375, 300, 300);
                textAlign(CENTER);
                fill(0);
                textSize(100);
                text("CLICK THE CIRCLES", 750, 100);
                textSize(50);
                text("EASY", 400, 400);
                text("Hard", 1100, 400);
                textAlign(LEFT);
                text("Instructions", 850, 675);

            } else {
                fill(0);
                textAlign(LEFT);
                text("Back", 850, 675);
                textAlign(CENTER);
                text("Instructions", 750, 50);
                textAlign(LEFT);
                textSize(35);
                text("In clicking the circles your goal is to click every single circle that you see the beat. You will see two different types of circles. First hit circles. These circles are stationary and have a ring that closes in around them. When the ring meets the edge of the circle the circle will turn from red to green. That is your que to click it. The other type of circle are the sliding circles. These circles move when you click them. Instead of turning green they turn orange. You must press and hold on them and keep your mouse on the circle as it moves along the line. You can click with your mouse or use the z and x keys on your keyboard. If you miss too many circles your hp will run out and you will die. Give it your best shot.",
                        25, 100, 1475, 725);
                textSize(50);

            }
            fill(0, 0, 255);
            circle(750, 650, 100);
        }
    }

    // handles printing certain values when winning
    public void WIN() {
        fill(0);
        textAlign(CENTER);
        text((int) (score * AproachRate * (100 / CircleSize) * (bpm / 200)), 750, 150);
        textAlign(LEFT);
        text(Double.toString(accuracy) + "%", 150, 600);

        text(Integer.toString(maximumCombo) + "/" + Integer.toString(mapCombo), 1000, 600);

        fill(-16711936);
        text(hits, 300, 375);
        fill(255, 0, 0);
        text(misses, 1200, 375);
    }

    // simple function that checks mouse distance to an object
    public double mouseDistance(int objectx, int objecty) {
        return Math.sqrt(Math.pow((mouseX - objectx), 2) + Math.pow((mouseY - objecty), 2));
    }

    // handles the events that need to happen when circles or sliders are clicked
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

    // multiplies beat number by fpb for simplicity sake when programming the maps
    public float beatTime(float beat) {

        return (float) fpb * beat;
    }

    public void setup() {
        GravityFalls = new SoundFile(this, "GravityFalls.mp3");
        frameRate(60);

    }

    public void settings() {
        size(1500, 750);

    }

    // handles drawing sliders
    public void SliderTime(float SpawnTime, float endtime, int xPosStart, int yPosStart, int xPosEnd, int yPosEnd,
            int id) {
        // draws the shrinking circles
        if (SpawnTime - GlobalTimer <= (100 / (float) AproachRate) && SpawnTime - GlobalTimer >= (fpb / 4)) {
            fill(255);
            strokeWeight(10);
            ellipse(xPosStart, yPosStart, ((CircleSize - (fpb / 8) + AproachRate * (SpawnTime - GlobalTimer))),
                    ((CircleSize - (fpb / 8) + AproachRate * (SpawnTime - GlobalTimer))));
        }
        strokeWeight(4);
        // draws the main slider and colors it so it is clickable
        if (globalId == (id - 1)) {
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
            // handles what counts as a hit on a slider
            if (endtime - GlobalTimer <= fpb / 2 && sliderMulti == (float) 3 / 2) {
                fill(0, 255, 0);
                text("HIT", 0, 120);
                SliderActiveClick = false;
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
            // handles what counts as a miss on a slider
            else if ((SpawnTime - GlobalTimer) <= (-fpb / 2) && sliderMulti != (float) 3 / 2) {

                fill(255, 0, 0);
                text("MISS", 0, 70);
                sliderMulti = 0;
                if (endtime - GlobalTimer < -5) {
                    globalId = id;
                    hp -= 50;
                    misses++;
                    combo = 0;
                    STUPIDBUGFIX = 0;
                    sliderMulti = 1;

                }
            }

        }

    }

    // handles drawing circles at specific times and the closing in circles
    public void CircleTime(float SpawnTime, int xLoc, int yLoc, int id) {
        // handles the shrinking circles
        if (SpawnTime - GlobalTimer <= (100 / (float) AproachRate) && SpawnTime - GlobalTimer >= (fpb / 4)) {
            fill(255);
            strokeWeight(10);
            ellipse(xLoc, yLoc, ((CircleSize - (fpb / 8) + AproachRate * (SpawnTime - GlobalTimer))),
                    ((CircleSize - (fpb / 8) + AproachRate * (SpawnTime - GlobalTimer))));
        }
        // draws the main circle and colors it properly so it is clickable;
        strokeWeight(4);
        if ((SpawnTime - GlobalTimer) <= (2 * fpb / AproachRate) && globalId < id
                && (SpawnTime - GlobalTimer) >= -(fpb / 2)) {

            fill(255, 0, 0);
            if (Math.abs(SpawnTime - GlobalTimer) <= (fpb / 2) && globalId == id - 1) {
                fill(0, 255, 0);
            }
            ellipse(xLoc, yLoc, CircleSize, CircleSize);

        }
        // handles missing a circle
        if (GlobalTimer - SpawnTime > (fpb / 2) && globalId - id == -1) {
            fill(255, 0, 0);
            text("MISS", 0, 70);
            if (GlobalTimer - SpawnTime > (3 * fpb / 4)) {
                globalId = id;
                hp -= 50;
                misses++;
                combo = 0;
                STUPIDBUGFIX = 0;
            }

            // handles hitting a circle
        } else if (globalId == id && GlobalTimer - SpawnTime < (fpb / 2)) {
            fill(0, 255, 0);
            text("HIT", 0, 120);
        }

    }

    // a function containg a pattern used in the hard map multiple times
    public void GravityFallsJumpPattern(float startTime, int startid) {
        CircleTime(beatTime(startTime + 6), 300, 300, startid + 7);
        CircleTime(beatTime(startTime + 5), 400, 600, startid + 6);
        CircleTime(beatTime(startTime + 4), 500, 400, startid + 5);
        CircleTime(beatTime(startTime + 3.5f), 600, 500, startid + 4);
        CircleTime(beatTime(startTime + 3), 700, 400, startid + 3);
        CircleTime(beatTime(startTime + 2), 800, 600, startid + 2);
        CircleTime(beatTime(startTime + 1), 900, 300, startid + 1);
        CircleTime(beatTime(startTime), 1000, 600, startid);
    }

    // a function containg the hard map
    public void GravityFallsHard() {

        if (GravityFallsMapHard) {
            // starts the music
            if (GlobalTimer >= beatTime(2)) {
                if (SoundPlay) {
                    GravityFalls.play();
                    SoundPlay = false;
                }
            }
            // contains the actaul circles of the map with each individual time and location
            // being hard coded
            if (hp > 0) {
                CircleTime(beatTime(104), 750, 325, 91);
                SliderTime(beatTime(100), beatTime(103.5f), 100, 100, 1400, 650, 90);
                SliderTime(beatTime(97), beatTime(99), 750, 325, 750, 425, 89);
                SliderTime(beatTime(96), beatTime(97), 750, 325, 650, 325, 88);
                SliderTime(beatTime(94), beatTime(95), 750, 325, 850, 325, 87);
                SliderTime(beatTime(92), beatTime(93), 750, 325, 750, 225, 86);
                CircleTime(beatTime(91), 750, 325, 85);
                CircleTime(beatTime(90), 750, 325, 84);
                CircleTime(beatTime(89), 750, 325, 83);
                GravityFallsJumpPattern(81, 75);
                CircleTime(beatTime(79), 100, 650, 74);
                CircleTime(beatTime(78), 750, 650, 73);
                CircleTime(beatTime(77), 1400, 650, 72);
                CircleTime(beatTime(75.5f), 1400, 200, 71);
                CircleTime(beatTime(74.5f), 750, 200, 70);
                CircleTime(beatTime(73.5f), 100, 200, 69);
                GravityFallsJumpPattern(65.5f, 61);
                CircleTime(beatTime(64), 1000, 300, 60);
                CircleTime(beatTime(63), 900, 600, 59);
                CircleTime(beatTime(62), 800, 400, 58);
                CircleTime(beatTime(61.5f), 700, 500, 57);
                CircleTime(beatTime(61), 600, 400, 56);
                CircleTime(beatTime(60), 500, 600, 55);
                CircleTime(beatTime(59), 400, 300, 54);
                CircleTime(beatTime(58), 300, 600, 53);
                GravityFallsJumpPattern(50, 45);
                CircleTime(beatTime(48.5f), 900, 500, 44);
                CircleTime(beatTime(48), 850, 550, 43);
                CircleTime(beatTime(47.5f), 800, 500, 42);
                CircleTime(beatTime(47), 750, 550, 41);
                CircleTime(beatTime(46.5f), 700, 500, 40);
                CircleTime(beatTime(46), 650, 550, 39);
                CircleTime(beatTime(45.5f), 600, 500, 38);
                CircleTime(beatTime(45), 550, 550, 37);
                CircleTime(beatTime(44.5f), 550, 350, 36);
                CircleTime(beatTime(44), 600, 400, 35);
                CircleTime(beatTime(43.5f), 650, 350, 34);
                CircleTime(beatTime(43), 700, 400, 33);
                CircleTime(beatTime(42.5f), 750, 350, 32);
                CircleTime(beatTime(42), 800, 400, 31);
                CircleTime(beatTime(41.5f), 850, 350, 30);
                CircleTime(beatTime(41), 900, 400, 29);
                SliderTime(beatTime(39), beatTime(40), 1050, 325, 900, 400, 28);
                SliderTime(beatTime(37), beatTime(38), 900, 325, 1050, 400, 27);
                CircleTime(beatTime(36), 900, 325, 26);
                SliderTime(beatTime(34), beatTime(35), 750, 325, 900, 400, 25);
                CircleTime(beatTime(33), 750, 325, 24);
                CircleTime(beatTime(32.5f), 650, 425, 23);
                CircleTime(beatTime(32), 850, 425, 22);
                CircleTime(beatTime(31.5f), 850, 225, 21);
                CircleTime(beatTime(31), 650, 225, 20);
                CircleTime(beatTime(30), 750, 325, 19);
                CircleTime(beatTime(29), 750, 325, 18);
                CircleTime(beatTime(27.5f), 200, 200, 17);
                CircleTime(beatTime(26), 1000, 600, 16);
                SliderTime(beatTime(22), beatTime(25), 450, 225, 1050, 325, 15);
                CircleTime(beatTime(21), 450, 425, 14);
                SliderTime(beatTime(18), beatTime(20), 750, 425, 450, 225, 13);
                CircleTime(beatTime(17), 750, 425, 12);
                CircleTime(beatTime(16), 750, 225, 11);
                CircleTime(beatTime(15.5f), 690, 405, 10);
                CircleTime(beatTime(15), 810, 405, 9);
                CircleTime(beatTime(14.5f), 750, 225, 8);
                CircleTime(beatTime(14), 750, 325, 7);
                SliderTime(beatTime(12), beatTime(13), 750, 325, 750, 175, 6);
                SliderTime(beatTime(10), beatTime(11), 750, 325, 660, 445, 5);
                SliderTime(beatTime(8), beatTime(9), 750, 325, 840, 445, 4);
                SliderTime(beatTime(6), beatTime(7), 750, 325, 750, 175, 3);
                SliderTime(beatTime(4), beatTime(5), 750, 325, 660, 445, 2);
                SliderTime(beatTime(2), beatTime(3), 750, 325, 840, 445, 1);

            }

        }
    }

    // contains the easy map
    public void GravityFallsMapEasy() {
        if (GravityFallsMapEasy) {
            // starts the music
            if (GlobalTimer >= beatTime(2)) {
                if (SoundPlay) {
                    GravityFalls.play();
                    SoundPlay = false;
                }
            }
            // contains the actaul circles of the map with each individual time and location
            // being hard coded
            if (hp > 0) {
                CircleTime(beatTime(105), 750, 325, 73);
                CircleTime(beatTime(103.5f), 750, 325, 72);
                CircleTime(beatTime(103), 750, 325, 71);
                CircleTime(beatTime(102.5f), 750, 325, 70);
                CircleTime(beatTime(102), 750, 325, 69);
                CircleTime(beatTime(101.5f), 750, 325, 68);
                CircleTime(beatTime(101), 750, 325, 67);
                CircleTime(beatTime(100.5f), 750, 325, 66);
                CircleTime(beatTime(100), 750, 325, 65);
                SliderTime(beatTime(98), beatTime(99), 750, 425, 750, 325, 64);
                SliderTime(beatTime(96), beatTime(97), 750, 225, 750, 325, 63);
                SliderTime(beatTime(94), beatTime(95), 650, 325, 750, 325, 62);
                SliderTime(beatTime(92), beatTime(93), 850, 325, 750, 325, 61);
                CircleTime(beatTime(91), 650, 325, 60);
                CircleTime(beatTime(90), 850, 425, 59);
                CircleTime(beatTime(89), 650, 225, 58);
                CircleTime(beatTime(87), 750, 325, 57);
                CircleTime(beatTime(86), 850, 325, 56);
                CircleTime(beatTime(85), 650, 325, 55);
                CircleTime(beatTime(84), 850, 225, 54);
                CircleTime(beatTime(83), 850, 425, 53);
                CircleTime(beatTime(82), 650, 425, 52);
                CircleTime(beatTime(81), 650, 225, 51);
                CircleTime(beatTime(79), 750, 625, 50);
                CircleTime(beatTime(78), 750, 625, 49);
                CircleTime(beatTime(77), 750, 625, 48);
                CircleTime(beatTime(75), 750, 125, 47);
                CircleTime(beatTime(74), 750, 125, 46);
                CircleTime(beatTime(73), 750, 125, 45);
                CircleTime(beatTime(71.5f), 250, 425, 44);
                CircleTime(beatTime(70.5f), 250, 525, 43);
                CircleTime(beatTime(69.5f), 250, 625, 42);
                CircleTime(beatTime(68.5f), 450, 625, 41);
                CircleTime(beatTime(67.5f), 450, 525, 40);
                CircleTime(beatTime(66.5f), 450, 425, 39);
                CircleTime(beatTime(65.5f), 450, 325, 38);
                CircleTime(beatTime(64.5f), 450, 325, 37);
                CircleTime(beatTime(63), 450, 225, 36);
                CircleTime(beatTime(62), 450, 125, 35);
                CircleTime(beatTime(61), 650, 125, 34);
                CircleTime(beatTime(60), 650, 225, 33);
                CircleTime(beatTime(59), 650, 325, 32);
                CircleTime(beatTime(58), 650, 425, 31);
                CircleTime(beatTime(56), 650, 425, 30);
                CircleTime(beatTime(55), 650, 525, 29);
                CircleTime(beatTime(54), 650, 625, 28);
                CircleTime(beatTime(53), 850, 625, 27);
                CircleTime(beatTime(52), 850, 525, 26);
                CircleTime(beatTime(51), 850, 425, 25);
                CircleTime(beatTime(50), 850, 325, 24);
                SliderTime(beatTime(47), beatTime(48), 850, 125, 850, 325, 23);
                SliderTime(beatTime(45), beatTime(46), 650, 125, 850, 125, 22);
                SliderTime(beatTime(43), beatTime(44), 650, 325, 650, 125, 21);
                SliderTime(beatTime(41), beatTime(42), 450, 325, 650, 325, 20);
                SliderTime(beatTime(39), beatTime(40), 450, 125, 450, 325, 19);
                SliderTime(beatTime(37), beatTime(38), 650, 125, 450, 125, 18);
                CircleTime(beatTime(36), 650, 125, 17);
                SliderTime(beatTime(34), beatTime(35), 650, 225, 650, 125, 16);
                SliderTime(beatTime(30), beatTime(33), 650, 325, 850, 325, 15);
                CircleTime(beatTime(29), 750, 425, 14);
                CircleTime(beatTime(27.5f), 750, 325, 13);
                CircleTime(beatTime(26), 750, 225, 12);
                SliderTime(beatTime(22), beatTime(25), 850, 225, 650, 225, 11);
                CircleTime(beatTime(21), 850, 225, 10);
                SliderTime(beatTime(18), beatTime(20), 750, 325, 850, 325, 9);
                CircleTime(beatTime(17), 750, 325, 8);
                SliderTime(beatTime(14), beatTime(15), 750, 325, 840, 445, 7);
                SliderTime(beatTime(12), beatTime(13), 750, 325, 750, 175, 6);
                SliderTime(beatTime(10), beatTime(11), 750, 325, 660, 445, 5);
                SliderTime(beatTime(8), beatTime(9), 750, 325, 840, 445, 4);
                SliderTime(beatTime(6), beatTime(7), 750, 325, 750, 175, 3);
                SliderTime(beatTime(4), beatTime(5), 750, 325, 660, 445, 2);
                SliderTime(beatTime(2), beatTime(3), 750, 325, 840, 445, 1);
            }
        }

    }

    // Sets up things that need to happen at the beginning of every frame
    public void frameSetup() {
        if (anyMapStarted) {
            // resets if something can be clicked
            CircleClick = false;
            sliderClick = false;

            // increments the timer so that it increments by 60 every second using system
            // time
            GlobalTimer += 60 * (millis() - previousFrameTime) / 1000f;
            previousFrameTime = millis();

            accuracy = (double) Math.round((100 - ((misses / ((double) (hits + misses + STUPIDBUGFIX))) * 100)) * 100d)
                    / 100d;
        }

    }

    // handles things that happen at the end of every frame
    public void endOfFrameUtils() {
        if (anyMapStarted) {
            fill(0);
            // handles events that happen when you lose
            if (hp <= 0) {
                GravityFalls.pause();
                notFailed = false;
                textSize(150);
                fill(255, 0, 0);
                textAlign(CENTER);
                text("FAIL", 750, 375);
                failTimer--;
                if (failTimer <= 0) {
                    System.exit(0);
                }

            }
            // checks if you are on a circle or a slider by checking the color codes
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

            // checks to see if you have won and prints values that hapen then.
            if (GlobalTimer > 60 * songLength && notFailed) {
                textSize(100);
                if (misses == 0) {
                    textAlign(CENTER);
                    fill(0, 255, 0);
                    textSize(150);
                    text("PERFECT", 750, 375);
                    textAlign(LEFT);
                    WIN();
                } else {
                    textSize(150);
                    fill(0, 255, 0);
                    textAlign(CENTER);
                    text("WIN", 750, 375);
                    textAlign(LEFT);
                    WIN();

                }
                // prints information such as hp and score for the player to see when they are
                // playing
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
            if (hp >= 750) {
                hp = 750;
            }

        }

    }
//main draw loop 
    public void draw() {
        textSize(50);
        background(255);
        menu();
        frameSetup();
        GravityFallsMapEasy();
        GravityFallsHard();
        endOfFrameUtils();
        // draws cursor
        fill(0, 0, 255);
        ellipse(mouseX, mouseY, 50, 50);

    }

    public void mousePressed() {
        if (anyMapStarted) {
            //if a map is started calls thw clicking function
            clickStuff();
        } else {
            if (mouseDistance(1100, 375) < 150) {
                //sets up the maps based on which one you click on the menu.
                if (GravityFallsMapHard == false && anyMapStarted == false && InstructionsOnScreen == false) {
                    GravityFallsMapHard = true;
                    anyMapStarted = true;
                    songLength = 40;
                    previousFrameTime = millis();
                    bpm = 160;
                    fpb = 3600 / 160f;
                    AproachRate = 2;
                    CircleSize = 100;
                    mapCombo = 91;
                }
            }
            if (mouseDistance(400, 375) < 150) {
                if (GravityFallsMapEasy == false && anyMapStarted == false && InstructionsOnScreen == false) {
                    GravityFallsMapEasy = true;
                    anyMapStarted = true;
                    songLength = 40;
                    previousFrameTime = millis();
                    bpm = 160;
                    fpb = 3600 / 160f;
                    AproachRate = 1;
                    CircleSize = 125;
                    mapCombo = 73;
                }
            }
            if (mouseDistance(750, 650) < 50) {
                if (InstructionsOnScreen == false) {
                    InstructionsOnScreen = true;
                } else {
                    InstructionsOnScreen = false;
                }

            }
        }

    }
//relleased functions are for if you realese to early pn th emoving circles
    public void mouseReleased() {
        if (sliderClick) {
            sliderMulti = 1;
        }

    }

    public void keyReleased() {
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
    }

    public void keyPressed() {
        if (key == 'z') {
            //if a map is started calls thw clicking function
            clickStuff();

        }

        if (key == 'x') {
            //if a map is started calls thw clicking function
            clickStuff();

        }
        if (key == CODED) {
            if (keyCode == UP) {
                //if a map is started calls thw clicking function
                clickStuff();
            }
        }
    }
}
