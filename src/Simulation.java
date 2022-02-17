public class Simulation {
    public static void run() throws InterruptedException {
        Drawer drawer = new Drawer();
        boolean running = true;

        while(running){
            drawer.drawRink();
            if (StdDraw.hasNextKeyTyped()){
                if(StdDraw.nextKeyTyped() == 'q'){
                    running = false;
                }
            }

            wait(500);

        }

    }
    public static void wait(int time) throws InterruptedException {
        Thread.sleep(time);
    }
}
