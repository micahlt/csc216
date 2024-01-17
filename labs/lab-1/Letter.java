public class Letter {
    private char thisLetter;
    private Letter[] neighbors = new Letter[8];

    public static final int E = 0;
    public static final int SE = 1;
    public static final int S = 2;
    public static final int SW = 3;
    public static final int W = 4;
    public static final int NW = 5;
    public static final int N = 6;
    public static final int NE = 7;
    public static final int NODIR = -1;

    Letter(char let) {
        thisLetter = let;
    }

    private static int rev(int dir) {
        switch (dir) {
            case E:
                return W;
            case SE:
                return NW;
            case S:
                return N;
            case SW:
                return NE;
            case W:
                return E;
            case NW:
                return SE;
            case N:
                return S;
            case NE:
                return SW;
            default:
                return NODIR;
        }
    }

    void setNeighbor(int dir, Letter let) {
        neighbors[dir] = let;
        let.neighbors[rev(dir)] = this;
    }

    Letter getNeighbor(int dir) {
        return neighbors[dir];
    }

    boolean matches(String toFind, int dir) {
        if (toFind.charAt(0) == thisLetter) {
            if (toFind.length() == 1) {
                return true;
            } else {
                if (this.getNeighbor(dir) == null) {
                    return false;
                } else {
                    return this.getNeighbor(dir).matches(toFind.substring(1), dir);
                }
            }
        } else {
            return false;
        }
    }

    int matches(String toFind) {
        int[] directions = { E, SE, S, SW, W, NW, N, NE };
        for (int dir : directions) {
            if (matches(toFind, dir)) {
                return dir;
            } else {
                return NODIR;
            }
        }
        return NODIR;
    }
}
