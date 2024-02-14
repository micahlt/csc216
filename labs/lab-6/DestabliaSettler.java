import java.util.*;

public class DestabliaSettler {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("How many frobs do you wish to settle? ");
        int amount = in.nextInt();
        in.close();

        TotallingStack stack = new TotallingStack();

        // Fill the stack with enough of the largest coin to at least pay the bill
        while (stack.getValue() < amount) {
            stack.push(Mint.largest());
        }

        // Try to reduce the money on the stack by replacing the smallest coin
        // (larger than a grop) with something smaller
        while (!stack.isEmpty() && (stack.getValue() > amount)) {
            // Find the first coin which can be replaced with a smaller one
            while (!stack.isEmpty() && (stack.peek().getName() == "grop")) {
                stack.pop();
            }

            // If we found some coin larger than a grop, replace it with smaller
            // coins until we again reach the amount
            if (!stack.isEmpty()) {
                Mint.Coin oldTop = stack.pop();
                Mint.Coin less = Mint.smaller(oldTop);

                while (stack.getValue() < amount) {
                    stack.push(less);
                }
            }

            if (stack.isEmpty()) {
                System.out.println("It is impossible to pay " + amount + " in Destablian coins.");
            } else {
                // System.out.println("The coins on the stack will add up to " + amount);
                // System.out.println(stack.toString());
                Map<String, Integer> counts = new HashMap<>();
                while (!stack.isEmpty()) {
                    Mint.Coin popped = stack.pop();
                    counts.put(popped.getName(),
                            counts.containsKey(popped.getName())
                                    ? counts.get(popped.getName()) + 1
                                    : 1);
                }
                for (Map.Entry<String, Integer> entry : counts.entrySet()) {
                    System.out.println(entry.getValue() + " " + entry.getKey());
                }
            }
        }
    }
}
