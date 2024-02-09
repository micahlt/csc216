public class Pair<P extends Comparable<P>, Q extends Comparable<Q>> implements Comparable<Pair<P, Q>> {
    private P first;
    private Q second;

    public Pair(P f, Q s) {
        first = f;
        second = s;
    }

    public int compareTo(Pair<P, Q> other) {
        int firstCompare = this.getFirst().compareTo(other.getFirst());
        if (firstCompare != 0) {
            return firstCompare;
        } else {
            return this.getSecond().compareTo(other.getSecond());
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
