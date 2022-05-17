public class BlockSteel extends Block {

    public BlockSteel() {
    }

    public BlockSteel(int x, int y, int size) {
        super(x, y, size);
        setBreakable(false);
        setPassable(false);
    }
}
