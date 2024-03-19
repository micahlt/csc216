package Testing;

/* Perform an equality comparison allowing the two items to be null. */
public class EQ {
    public static <T> boolean test(T a, T b) {
        if(a == null && b == null) return true;
        if(a == null || b == null) return false;
        return a.equals(b);
    }
}
