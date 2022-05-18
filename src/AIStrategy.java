interface AIStrategy {
    Command getNextMoveCommand(World world, Tank myTank);
}