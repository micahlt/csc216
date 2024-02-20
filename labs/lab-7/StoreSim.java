package lab7;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

/**
 * A store simulator using queues.
 * 
 * @author Micah Lindley
 * @version 1.0
 */
public class StoreSim {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Simulation length (minutes): ");
        int length = in.nextInt();
        int customersIn = 0, customersServed = 0, totalWaitTime = 0, longestWait = 0;
        Queue<Customer> q = new ArrayDeque<Customer>();
        Random rand = new Random();
        for (int minute = 0; minute < length; minute++) {
            if (!q.isEmpty()) {
                Customer removed = q.remove();
                int wait = minute - removed.getTimeEntered();
                totalWaitTime += wait;
                if (wait > longestWait)
                    longestWait = wait;
                customersServed++;
            }
            int r = rand.nextInt(4);
            if (r == 3) {
                customersIn += 2;
                q.add(new Customer(minute));
                q.add(new Customer(minute));
            } else if (r == 2) {
                customersIn++;
                q.add(new Customer(minute));
            }
        }
        double avgWait = (double) totalWaitTime / (double) customersServed;
        System.out.println(customersIn + " customers started, " + customersServed + " finished.");
        System.out.println("longest wait: " + longestWait + " min");
        System.out.println("average wait: " + avgWait + " min");
        in.close();
    }
}