public class Pair<P extends Comparable<P>, Q extends Comparable<Q>> implements Comparable<Pair<P, Q>> {
    private P first;
    private Q second;

    public Pair(P f, Q s) {
        first = f;
        second = s;
    }

    public int compareTo(Pair<P, Q> other) {
        int firstCompare = other.getFirst().compareTo(this.first);
        int secondCompare = other.getSecond().compareTo(this.second);
        if (firstCompare == 0 && secondCompare == 0) {
            return 0;
        } else if (firstCompare > secondCompare) {
            return 1;
        } else {
            return -1;
        }
    }

    public P getFirst() {
        return first;
    }

    public Q getSecond() {
        return second;
    }

    public String toString() {
        return first.toString() + " : " + second.toString();
    }
}
