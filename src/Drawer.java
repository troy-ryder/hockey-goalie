import java.awt.*;

public class Drawer {
    private static final int WIDTH = 1400;
    private static final int WIDTH_ADD = 500;
    private static final int HEIGHT = 800;
    private static final int FEET = 10;
    private static final int DISPLAY_FEET = 200;
    private static final double[] LEFTPOST = {WIDTH / 2 - 3 * FEET, HEIGHT / 6};
    private static final double[] RIGHTPOST = {WIDTH / 2 + 3 * FEET, HEIGHT / 6};
    private static final double[] CENTER_ARC = {(LEFTPOST[0] + RIGHTPOST[0]) / 2, (LEFTPOST[1] + RIGHTPOST[1]) / 2 + 2 * FEET}; //point arc is drawn around
    private static final double[] CENTER_GOAL = {(LEFTPOST[0] + RIGHTPOST[0]) / 2, (LEFTPOST[1] + RIGHTPOST[1]) / 2}; //point arc is drawn around
    public static final double PI = 3.14159265358979323846;
    public static final double maxDistance = 8*FEET;
    private static final int GOAL_HEIGHT = 4*FEET;


    public static void drawRink() {
        double goalieWidth = 3 * FEET;
        double goalieHeight = 4*FEET;
        double[] goalieBody = {CENTER_ARC[0] - goalieWidth / 2, CENTER_ARC[1], CENTER_ARC[0] + goalieWidth / 2, CENTER_ARC[1]}; //x0 y0 x1 y1
        double goalieFactor = Math.log(DISPLAY_FEET * (Math.atan(goalieHeight/(40)))/PI*4); //pi over 4 is 90 deg in rad
        double leftFactor = Math.log(DISPLAY_FEET * (Math.atan(GOAL_HEIGHT/(40)))/PI*4);
        double rightFactor = Math.log(DISPLAY_FEET * (Math.atan(GOAL_HEIGHT/(40)))/PI*4);
        double fromCenterFactor = Math.log(DISPLAY_FEET * (Math.atan((0) / (40 - CENTER_GOAL[1])) - Math.atan( 3/ (40 - LEFTPOST[1])))/PI*4);

        StdDraw.setCanvasSize(WIDTH + WIDTH_ADD, HEIGHT);
        StdDraw.setXscale(0, WIDTH + WIDTH_ADD);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.enableDoubleBuffering();
        while (true) {
            StdDraw.clear();


            double rad = Math.sqrt(Math.pow(RIGHTPOST[0] - CENTER_ARC[0], 2) + Math.pow(RIGHTPOST[1] - CENTER_ARC[1], 2));
            double deg = Math.acos(((RIGHTPOST[0] - LEFTPOST[0]) / 2) / rad) * 360 / (2 * PI);
            double mouseX = StdDraw.mouseX();
            double mouseY = StdDraw.mouseY();
            double puckDist = Math.sqrt(Math.pow(mouseX - CENTER_GOAL[0], 2) + Math.pow(mouseY - CENTER_GOAL[1], 2));
            double goalieDistance = getDistance(CENTER_GOAL, goalieCenter(goalieBody[0],goalieBody[1], goalieBody[2], goalieBody[3]))/FEET;

            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                if (key == 'q') {
                    System.exit(0);
                } else if (key == 'g') {
                    goalieBody = reorient(mouseX, mouseY, goalieWidth);
                    goalieDistance = getDistance(CENTER_GOAL, goalieCenter(goalieBody[0],goalieBody[1], goalieBody[2], goalieBody[3]))/FEET;
                    if(mouseY>CENTER_GOAL[1]) {
                        goalieFactor = Math.log(DISPLAY_FEET * (Math.atan(goalieHeight / (puckDist - goalieDistance))) / PI * 4); //pi over 4 is 90 deg in rad
                        leftFactor = Math.log(DISPLAY_FEET * (Math.atan(GOAL_HEIGHT / (getDistance(new double[]{mouseX, mouseY}, LEFTPOST)))) / PI * 4);
                        rightFactor = Math.log(DISPLAY_FEET * (Math.atan(GOAL_HEIGHT / getDistance(new double[]{mouseX, mouseY}, RIGHTPOST))) / PI * 4);
                        double distC = Math.abs(mouseX - CENTER_GOAL[0]);
                        double distB = Math.abs(mouseX - LEFTPOST[0]);
                        fromCenterFactor = Math.log(DISPLAY_FEET * (Math.atan( distC / ((mouseY - CENTER_GOAL[1]))) - Math.atan( distB/ (mouseY - LEFTPOST[1])))/PI*4);
                    } else{
                        goalieFactor = 0;
                        leftFactor = 0;
                        rightFactor = 0;
                        fromCenterFactor = 0;
                    }
                }
            }






            StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
            StdDraw.filledCircle(CENTER_GOAL[0],CENTER_GOAL[1], FEET*6);
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.filledRectangle(0,0,WIDTH,CENTER_GOAL[1]);

            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius(.002);
            StdDraw.line(CENTER_GOAL[0], CENTER_GOAL[1], mouseX, mouseY);
            StdDraw.setPenRadius(.005);
            StdDraw.arc(CENTER_GOAL[0],CENTER_GOAL[1], FEET *6, 0, 180);
            StdDraw.line(CENTER_GOAL[0]-44.3*FEET, CENTER_GOAL[1], CENTER_GOAL[0]+44.3*FEET, CENTER_GOAL[1]);
            StdDraw.setPenRadius(.01);
            StdDraw.line(LEFTPOST[0], LEFTPOST[1], RIGHTPOST[0], RIGHTPOST[1]);
            StdDraw.arc(CENTER_ARC[0], CENTER_ARC[1], rad, 180 + deg, 360 - deg);

            StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
            StdDraw.filledRectangle(CENTER_GOAL[0], CENTER_GOAL[1] + 64*FEET, 50*FEET, .5 *FEET);

            StdDraw.setPenColor(StdDraw.BOOK_BLUE);
            StdDraw.arc(CENTER_GOAL[0]+22*FEET, CENTER_GOAL[1]+17*FEET, 28*FEET, 270, 360);
            StdDraw.arc(CENTER_GOAL[0]-22*FEET, CENTER_GOAL[1]+17*FEET, 28*FEET, 180, 270);
            StdDraw.line(CENTER_GOAL[0]-22*FEET, CENTER_GOAL[1]-11*FEET, CENTER_GOAL[0]+22*FEET, CENTER_GOAL[1]-11*FEET);
            StdDraw.line(CENTER_GOAL[0]-50*FEET, CENTER_GOAL[1]+17*FEET, CENTER_GOAL[0]-50*FEET, HEIGHT);
            StdDraw.line(CENTER_GOAL[0]+50*FEET, CENTER_GOAL[1]+17*FEET, CENTER_GOAL[0]+50*FEET, HEIGHT);

            //double xOff = 3 * fromCenterFactor;
            //double yStart = goaliePolyY(goalieFactor,goalieHeight)[0];

            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.filledCircle(CENTER_GOAL[0]+22*FEET, CENTER_GOAL[1]+20*FEET, FEET);
            StdDraw.filledCircle(CENTER_GOAL[0]-22*FEET, CENTER_GOAL[1]+20*FEET,FEET);
            StdDraw.circle(CENTER_GOAL[0]+22*FEET, CENTER_GOAL[1]+20*FEET, 15*FEET);
            StdDraw.circle(CENTER_GOAL[0]-22*FEET, CENTER_GOAL[1]+20*FEET, 15*FEET);

            //StdDraw.line(WIDTH+WIDTH_ADD/2 - xOff, yStart,WIDTH+WIDTH_ADD/2 - xOff, yStart + GOAL_HEIGHT * rightFactor );
            //StdDraw.line(WIDTH+WIDTH_ADD/2 + xOff, yStart,WIDTH+WIDTH_ADD/2 + xOff, yStart + GOAL_HEIGHT * leftFactor);



            StdDraw.setPenColor(Color.BLACK);
            StdDraw.textLeft(WIDTH / 50, (HEIGHT * 14) / 15, "Press 'q' to quit");
            StdDraw.textLeft(WIDTH / 50, (HEIGHT * 13.5) / 15, "'g' move goalie");

            StdDraw.textLeft(WIDTH  / 50, (HEIGHT * 13) / 15, "Puck Dist: " + ((int) puckDist) / FEET + " feet");
            StdDraw.textLeft(WIDTH / 50, (HEIGHT * 12.5) / 15, "Goalie Dist: " + (int) goalieDistance + " feet");

            StdDraw.setPenRadius(.03);
            StdDraw.point(mouseX, mouseY);

            StdDraw.setPenRadius(.005);
            StdDraw.line(LEFTPOST[0], LEFTPOST[1], mouseX, mouseY);
            StdDraw.line(RIGHTPOST[0], RIGHTPOST[1], mouseX, mouseY);

            StdDraw.setPenColor(Color.BLUE);
            StdDraw.setPenRadius(.025);
            StdDraw.line(goalieBody[0], goalieBody[1], goalieBody[2], goalieBody[3]);


            //StdDraw.rectangle(WIDTH + WIDTH_ADD/2, HEIGHT/2, goalieFactor/2 * goalieWidth, goalieFactor/2*goalieHeight);
            StdDraw.polygon(goaliePolyX(goalieFactor,goalieWidth),goaliePolyY(goalieFactor,goalieHeight));







            StdDraw.show();
            StdDraw.pause(20);
        }
    }

    public static double[] reorient(double mouseX, double mouseY, double goalieWidth) {
        double[] returnPos = new double[4];
        boolean flipped = false;
        double centerDistance = Math.sqrt(Math.pow(mouseX - CENTER_GOAL[0], 2) + Math.pow(mouseY - CENTER_GOAL[1], 2));
        if (mouseX < CENTER_GOAL[0]) {
            mouseX = CENTER_GOAL[0]-mouseX + CENTER_GOAL[0];
            flipped = true;
        }
        double degree = 90 - Math.abs(Math.acos(Math.abs(mouseX - CENTER_GOAL[0]) / centerDistance) * 360 / (2 * PI)) % 90 ;
        if(degree == 90){
            degree = 0;
        }
        if (mouseY <= CENTER_GOAL[1]) {
            if (flipped) {
                returnPos = new double[]{LEFTPOST[0], LEFTPOST[1] + goalieWidth, LEFTPOST[0], LEFTPOST[1]};
            } else {
                returnPos = new double[]{RIGHTPOST[0], RIGHTPOST[1] + goalieWidth, RIGHTPOST[0], RIGHTPOST[1]};
            }
            return returnPos;
        }
        double slopeRight = (mouseY - RIGHTPOST[1]) / ((mouseX - RIGHTPOST[0])+1);
        double slopeLeft = (mouseY - LEFTPOST[1]) / ((mouseX - LEFTPOST[0])+1);
        double slopeCenter = (mouseY - CENTER_GOAL[1])/((mouseX-CENTER_GOAL[0])+1);

        double bRight = mouseY - slopeRight * mouseX;
        double bLeft = mouseY - slopeLeft * mouseX;
        double bCenter = mouseY - slopeCenter * mouseX;

        for (double yRight = RIGHTPOST[1]; yRight < mouseY; yRight += (mouseY-RIGHTPOST[1]) / 50) {
            double xRight = (yRight-bRight)/slopeRight;
            double yLeft = Math.sin(degree/360 * 2 * PI) * goalieWidth + yRight;
            double xLeft = xRight - Math.cos(degree/360*2*PI) * goalieWidth ;
            if(yLeft >= slopeLeft*xLeft + bLeft){
                returnPos = new double[]{xLeft,yLeft,xRight,yRight};
                break;
            }
            if(getDistance(goalieCenter(xRight,yRight,xLeft,yLeft), CENTER_GOAL)>maxDistance){
                for (double yCenter = CENTER_GOAL[1]; yCenter<mouseY; yCenter += (mouseY-CENTER_GOAL[1]) / 50){
                    double xCenter = (yCenter-bCenter)/slopeCenter;
                    double[] center = new double[]{xCenter, yCenter};
                    yRight =  yCenter - Math.sin(degree/360 *2 * PI) *goalieWidth/2;
                    xRight = xCenter + Math.cos(degree/360*2*PI) *goalieWidth/2;
                    yLeft = Math.sin(degree/360 * 2 * PI) * goalieWidth + yRight;
                    xLeft = xRight - Math.cos(degree/360*2*PI) * goalieWidth ;
                    if(getDistance(center, CENTER_GOAL)>maxDistance){
                        break;
                    }
                }
                returnPos = new double[]{xLeft,yLeft,xRight,yRight};
                break;
            }

        }
        if(flipped){
            returnPos[0] = CENTER_GOAL[0]*2 -returnPos[0];
            returnPos[2] = CENTER_GOAL[0] *2 -returnPos[2];
        }
        return returnPos;
    }

    public static double[] goalieCenter(double x0, double y0, double x1, double y1){
        double[] returnCenter = new double[2];
        returnCenter[0] = (x0+x1)/2;
        returnCenter[1] = (y0+y1) /2;
        return returnCenter;
    }

    public static double getDistance(double[] one, double[] two){
       return Math.sqrt(Math.pow(one[0] - two[0], 2) + Math.pow(one[1] - two[1], 2));
    }

    public static double[] goaliePolyX(double goalieFactor, double goalieWidth){
        double center = WIDTH + WIDTH_ADD/2;
        double left = center-goalieFactor*goalieWidth/2;
        double right = center+goalieFactor*goalieWidth/2;

        return new double[] {left,left, right, right };
    }
    public static double[] goaliePolyY(double goalieFactor, double goalieHeight){
        double center = HEIGHT/3 + goalieFactor*goalieHeight/2;
        double bottom = center-goalieFactor*goalieHeight/2;
        double top= center+goalieFactor*goalieHeight/2;
        return new double[] {bottom, top, top, bottom};
    }



}
