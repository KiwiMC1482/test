package draughtscoursework;


class MoveTransition 
{
    
    private final Board boardTransition;
    private final Move move;
    private final MoveStatus moveStatus;
    
    public MoveTransition(final Board boardTransition, final Move move, final MoveStatus moveStatus)
    {
        this.boardTransition = boardTransition;
        this.move = move;
        this.moveStatus = moveStatus;
    }
    
    public MoveStatus getMoveStatus()
    {
        return this.moveStatus;
    }
    
    
}
