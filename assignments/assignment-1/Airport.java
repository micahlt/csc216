import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

public class Airport {
    public static void main(String[] args) {
        // SET UP VARIABLES
        int clock = 0, crashCount = 0, landTime, takeOffTime, maxLandingQueueTime, timeToSimulate;
        double landProbability, takeOffProbability;
        boolean showDebug = false;
        Queue<Plane> toLand = new ArrayDeque<Plane>();
        Queue<Plane> toTakeOff = new ArrayDeque<Plane>();
        Scanner in = new Scanner(System.in);
        Random rand = new Random();

        // GATHER ALL VARIABLES
        System.out.println("How long for one plane to land? ");
        landTime = in.nextInt();
        System.out.println("How long for one plane to take off? ");
        takeOffTime = in.nextInt();
        System.out.println("What is the probability that a plane will arrive to land? ");
        landProbability = in.nextDouble();
        System.out.println("What is the probability that a plane will arrive to take off?");
        takeOffProbability = in.nextDouble();
        System.out.println(
                "What is the maximum amount of time that a plane can stay in the landing queue without running out of fuel and crashing?");
        maxLandingQueueTime = in.nextInt();
        System.out.println("What is the total length of time to simulate? ");
        timeToSimulate = in.nextInt();
        System.out.println("Show each iteration? (true/false) ");
        showDebug = in.nextBoolean();

        // RUN SIMULATION
        while (clock < timeToSimulate) {
            if (showDebug) {
                System.out.println("⧖ Current time | " + clock + " min\n-----------------------------");
            }
            if (rand.nextDouble(1) < landProbability) {
                toLand.add(new Plane(maxLandingQueueTime, clock));
                if (showDebug)
                    System.out.println("→ Plane enters landing queue | " + toLand.size() + " in queue");

            }
            if (rand.nextDouble(1) < takeOffProbability) {
                toTakeOff.add(new Plane(clock));
                if (showDebug)
                    System.out.println("→ Plane enters takeoff queue | " + toTakeOff.size() + " in queue");
            }
            if (!toLand.isEmpty()) {
                if (showDebug)
                    System.out
                            .println("~ First in landing queue | Entered at minute " + toLand.peek().getTimeEntered());
            }
            if (!toLand.isEmpty() && ((clock - toLand.peek().getTimeEntered()) > maxLandingQueueTime)) {
                toLand.poll();
                crashCount++;
                if (showDebug)
                    System.out.println("↯ Plane crashes from landing queue | " + toLand.size() + " in queue");
            }

            if (!toLand.isEmpty() && ((clock - toLand.peek().getTimeEntered()) >= landTime)) {
                toLand.poll();
                if (showDebug)
                    System.out.println("↘ Plane lands from queue | " + toLand.size() + " in queue");
            }

            if (!toTakeOff.isEmpty() && ((clock - toTakeOff.peek().getTimeEntered()) >= takeOffTime)) {
                toTakeOff.poll();
                if (showDebug)
                    System.out.println("↗ Plane takes off from queue | " + toTakeOff.size() + " in queue");
            }

            if (showDebug)
                System.out.println("\n");
            clock++;
        }
        System.out.println("-----------------------------\n◈ Simulation ended ◈\n");
        System.out.println("~ " + toTakeOff.size() + " planes left in takeoff queue");
        System.out.println("~ " + toLand.size() + " planes left in landing queue");
        System.out.println("↯ " + crashCount + " planes crashed");
        in.close();
    }
}