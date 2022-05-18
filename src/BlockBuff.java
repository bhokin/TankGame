public class BlockBuff extends Block{

    public BlockBuff() {
    }

    public BlockBuff(int x, int y, int size) {
        super(x, y, size);
        setBreakable(false);
        setPassable(true);
        setBuff(true);
    }
}
