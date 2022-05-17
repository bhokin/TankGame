public class BlockTree extends Block {

    public BlockTree() {
    }

    public BlockTree(int x, int y, int size) {
        super(x, y, size);
        setBreakable(false);
        setPassable(true);
    }
}
