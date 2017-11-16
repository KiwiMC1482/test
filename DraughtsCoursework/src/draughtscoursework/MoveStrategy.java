package draughtscoursework;

public interface MoveStrategy 
{
    long getNumBoardsEvaluated();

    Move execute(Board board);

}
