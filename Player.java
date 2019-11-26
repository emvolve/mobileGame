
/**
 * Represents the concept of a Player in the game and keep track of their score
 */
class Player {

    private int currentScore;
    private final String playerId;

    Player(String playerId) {
        currentScore = 0;
        this.playerId = playerId;
    }

    /**
     * Set the name of this player
     * @return
     * The name of this player
     */
    String getPlayerId() { return  playerId; }

    /**
     * Get the current score for this player
     * @return
     * The current score of this player
     */
    int getCurrentScore() { return currentScore; }

    /**
     * Add one to this players current score
     */
    void incrementSccore() { currentScore += 1; }
}
