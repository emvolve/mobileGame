import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.Collections;
import java.util.LinkedList;

/**
 * Utility class to simplify generating the cards and their colours
 */
class GameBoard {

    private final Rect[][] tiles;
    private LinkedList<Card> cards;
    private final Context context;

    /**
     * Initialises the game board
     * @param tiles
     * The Rectangles created by the custom view activity. Each new card will be located in a unique
     * tile
     * @param context
     * We need a reference to the callers context in order to be able to access the colour resources
     */
    GameBoard(Rect[][] tiles, Context context) {
        this.tiles = tiles;
        this.context = context;
        cards = generateCards();
    }

    /**
     * Go through each of the rectangles we were passed and create a card object containing that
     * Rectangle. A LinkedList is used here as
     * a)the list will frequently be iterated but we will never add or remove cards to it once it
     * is constructed.
     * And b) due to the way the application is structured we never require random access
     * Iteration on a Linked List is O(n)
     * @return
     * The Linked List containing all the cards in the game
     */
    private LinkedList<Card> generateCards() {
        LinkedList<Card> cards = new LinkedList<>();
        Card currentCard;
        LinkedList<Paint> cardColours = generateAllColours();

        for (int j = 0; j <= 3; j++) {
            for (int i = 0; i <= 3; i++) {
                currentCard = new Card(tiles[i][j], cardColours.poll());
                cards.add(currentCard);
            }
        }
        return cards;
    }

    /**
     * Return the games Linked List of cards
     * @return
     * The Linked List containing all the cards in the game
     */
    LinkedList<Card> getCards() {
        return cards;
    }

    /**
     * Generates two sets of colours, merges them and randomised them.
     * This ensures we always have two cards for each colour, and that games will be different
     * every time
     * @return
     * A list containing 16 Paint objects that represent colours, having 8 unique colours and
     * guaranteeing that each colour occurs no more and no less than twice.
     */
    private LinkedList<Paint> generateAllColours() {
        LinkedList<Paint> shuffledColours = generateCardColours();
        shuffledColours.addAll(generateCardColours());
        Collections.shuffle(shuffledColours);
        return shuffledColours;
    }

    /**
     * Use the callers context to generate the Paint objects representing colours from the colors
     * resource file
     * @return
     * A list containing eight unique colours
     */
    private LinkedList<Paint> generateCardColours() {
        LinkedList<Paint> colours = new LinkedList<>();

        Paint aqua = new Paint(Paint.ANTI_ALIAS_FLAG);
        aqua.setStyle(Paint.Style.FILL);
        aqua.setColor(context.getResources().getColor(R.color.aqua));
        colours.add(aqua);

        Paint lime = new Paint(Paint.ANTI_ALIAS_FLAG);
        lime.setStyle(Paint.Style.FILL);
        lime.setColor(context.getResources().getColor(R.color.lime));
        colours.add(lime);

        Paint teal = new Paint(Paint.ANTI_ALIAS_FLAG);
        teal.setStyle(Paint.Style.FILL);
        teal.setColor(context.getResources().getColor(R.color.teal));
        colours.add(teal);

        Paint blue = new Paint(Paint.ANTI_ALIAS_FLAG);
        blue.setStyle(Paint.Style.FILL);
        blue.setColor(context.getResources().getColor(R.color.blue));
        colours.add(blue);

        Paint navy = new Paint(Paint.ANTI_ALIAS_FLAG);
        navy.setStyle(Paint.Style.FILL);
        navy.setColor(context.getResources().getColor(R.color.navy));
        colours.add(navy);

        Paint yellow = new Paint(Paint.ANTI_ALIAS_FLAG);
        yellow.setStyle(Paint.Style.FILL);
        yellow.setColor(context.getResources().getColor(R.color.yellow));
        colours.add(yellow);

        Paint fuchsia = new Paint(Paint.ANTI_ALIAS_FLAG);
        fuchsia.setStyle(Paint.Style.FILL);
        fuchsia.setColor(context.getResources().getColor(R.color.orange));
        colours.add(fuchsia);

        Paint silver = new Paint(Paint.ANTI_ALIAS_FLAG);
        silver.setStyle(Paint.Style.FILL);
        silver.setColor(context.getResources().getColor(R.color.silver));
        colours.add(silver);

        return colours;
    }
}
