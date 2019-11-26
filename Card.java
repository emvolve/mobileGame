import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Represents the concept of a card in the game. Holds a reference to the
 * rectangle that defines where on the canvas this card is located, and utility methods to flip
 * the card, check if the card has the same colour as another card
 */
class Card {

    private final Rect cardRect;
    private Paint colour;
    private boolean isFaceUp;
    private boolean isMatched;

    /**
     * Constructor
     * @param cardRect
     * The Rectangle object containing the coordinates of where on the canvas this card is located
     * @param colour
     * The colour of the card
     */
    Card(Rect cardRect, Paint colour) {
        this.cardRect = cardRect;
        this.colour = colour;
        isFaceUp = false;
        isMatched = false;
    }

    /**
     * Gets the Rectangle associated with this card
     * @return
     * the Rectangle where this card is located.
     */
    Rect getCardRect() {
        return cardRect;
    }

    /**
     * Is this card face up
     * @return
     * True if it face up, false otherwise
     */
    boolean isFaceUp() {
        return isFaceUp;
    }

    /**
     * Get the current colour of this card
     * @return
     * The current colour of this card
     */
    Paint getColour() {
        return colour;
    }

    /**
     * Set the colour of this card
     * @param colour
     * The colour the card will become
     */
    void setColour(Paint colour) {
        this.colour = colour;
    }

    /**
     * Turn a face down card up, or a face up card down
     */
    void flip() {
        isFaceUp = !isFaceUp;
    }

    /**
     * Has this card already been matched with a another card
     * @return
     * True is this card has already been matched, false otherwise
     */
    boolean isMatched() {
        return isMatched;
    }

    /**
     * Set whether this card is matched
     * @param isMatched
     * True is the card is to become matched, false otherwise
     */
    void setMatched(boolean isMatched) {
        this.isMatched = isMatched;
    }

    /**
     * Determines if the current card object has the same integer colour number as another card object
     * @param card
     * The card to be compared to the current object
     * @return
     * True if the cards share a colour, false otherwise
     */
    boolean matches(Card card) {
        return this != card && this.getColour().getColor() == card.getColour().getColor();
    }
}
