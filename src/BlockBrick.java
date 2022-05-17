public class BlockBrick extends Block {

    public BlockBrick() {
    }

    public BlockBrick(int x, int y, int size) {
        super(x, y, size);
        setBreakable(true);
        setPassable(false);
    }
}
