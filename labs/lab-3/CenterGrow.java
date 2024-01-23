import javafx.scene.paint.Color;

public class CenterGrow extends RecMaker {
    private int windowWidth;
    private int windowHeight;
    private double posX;
    private double posY;
    private double size = -10;

    public CenterGrow(DisplayContext c) {
        super(c);
        this.windowWidth = c.WIDTH;
        this.windowHeight = c.HEIGHT;
        this.grow();
    }

    private void grow() {
        size += 20;
        posX = (windowWidth / 2) - (size * 1.5);
        posY = (windowHeight / 2) - (size * 1.5);
    }

    @Override
    protected int cornerX() {
        return (int) posX;
    }

    @Override
    protected int cornerY() {
        // TODO Auto-generated method stub
        return (int) posY;
    }

    @Override
    protected int dimX() {
        // TODO Auto-generated method stub
        return (int) size;
    }

    @Override
    protected int dimY() {
        // TODO Auto-generated method stub
        return (int) size;
    }

    @Override
    protected Color fillColor() {
        return Color.BLACK;
    }

    @Override
    void generate() {
        super.generate();
        this.grow();
    }
}
