package draughtscoursework;

public interface PGNPer 
{
    void persistGame(Game game);

    Move getNextBestMove(Board board, Player player, String gameText);
}
