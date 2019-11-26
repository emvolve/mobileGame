import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.LinkedList;

/**
 * Contains all the data structures and logic for the game & updating the canvas
 */
public class CustomView extends View {

    private int canvasWidthAndHeight;
    private boolean isGameInitialised = false;
    private Paint gridLinesColour, cardFaceDownColour, transparent;
    private final float partitionRatio = 1 / 4f;
    private LinkedList<Card> allCards;

    private Player playerOne;
    private Player playerTwo;
    private Player currentPlayer;

    private int pairsRemaining;

    private Card cardOne;
    private Card cardTwo;

    private boolean isCardOneFlipped;
    private boolean isCardTwoFlipped;

    public CustomView(Context context) {
        super(context);
        init();
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * Draws the current game board on our canvas. We initialise the game board rectangles and cards
     * in here, even though object allocation is not recommended in this method due to the
     * frequency it can be called. However we need a reference to the width/height of the canvas
     * which can potentially be zero before this method is called. Given that we only call this once
     * at the start of the game it has had no impact on performance
     * @param canvas
     * The canvas where we will draw our cards. Passed in by the Android Framework
     */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //Only initialise our board once
        if (!isGameInitialised) {
            GameBoard gameBoard = new GameBoard(generateTiles(), this.getContext());
            isGameInitialised = true;
            allCards = gameBoard.getCards();
        }
        drawCardsAndGrid(canvas);
    }

    /**
     * Contains our logic to deal with one of the card on the canvas being touched.
     * Find the cards associated with the x,y coordinates that the user has touched.
     * Does validation to make sure the users second touch isn't on the card already touched,
     * that the position touched is not an already matched card location, and then flips the cards.
     * It then redraws the board, with a delay for the second card touch so the user can see what
     * colour the second card turned out to be.
     * Finally it calls updateGameState to see if the cards match or not
     * @param event
     * The touch event
     * @return
     * True when the event has successfully been dealt with
     */
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            if (!isCardOneFlipped) {
                for (Card card : allCards) {
                    if (card.getCardRect().contains((int) event.getX(), (int) event.getY())) {
                        cardOne = card;
                        if (cardOne.isMatched()) return true;
                        cardOne.flip();
                        invalidate();
                    }
                }
                isCardOneFlipped = true;
            } else if (!isCardTwoFlipped) {
                for (Card card : allCards) {
                    if (card.getCardRect().contains((int) event.getX(), (int) event.getY())) {
                        cardTwo = card;

                        if (cardOne.equals(cardTwo) || cardTwo.isMatched()) {
                            return true;
                        }

                        cardTwo.flip();
                        isCardTwoFlipped = true;
                        invalidate();
                        this.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                updateGameState(cardOne, cardTwo);
                            }
                        }, 500);

                    }
                }

            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * Checks to see if the current player has selected two cards that match. If they do, it updates
     * the score of the current player, sets the cards status' to matched, and changes their colour
     * to blend in with the rest of the board so that they appear to have vanished from the canvas.
     * It decrements the amount of pairs remaining, switches the current player and checks if the
     * game is over.
     * If the cards do not match the score is not increased, the cards are flipped back to face
     * down, the current player is switched and the game continues.
     * In both cases it sends a String to the main activity containing the current game state.
     * @param cardOne
     * The first card the user turned up
     * @param cardTwo
     * The second card the user turned up
     */
    private void updateGameState(Card cardOne, Card cardTwo) {

        if (cardOne.matches(cardTwo)) {
            cardOne.setColour(transparent);
            cardTwo.setColour(transparent);
            cardOne.setMatched(true);
            cardTwo.setMatched(true);
            isCardOneFlipped = false;
            isCardTwoFlipped = false;
            currentPlayer.incrementSccore();
            changeCurrentPlayer();
            pairsRemaining--;
            checkIfGameIsOver();
            invalidate();
        } else {
            isCardOneFlipped = false;
            isCardTwoFlipped = false;
            cardOne.flip();
            cardTwo.flip();
            changeCurrentPlayer();
            invalidate();
        }
        updateTextViewInMainActivity();
    }

    /**
     * Send a String to the main activity showing the scores and the current player
     */
    private void updateTextViewInMainActivity() {
        MainActivity mainActivity = (MainActivity) getContext();
        String updateText = "Player one: " + playerOne.getCurrentScore() + ". Player two: " + playerTwo.getCurrentScore() + ".\nCurrentPlayer: " + currentPlayer.getPlayerId();
        mainActivity.update(updateText);
    }

    /**
     * Flips from Player one to Player two, or vice versa
     */
    private void changeCurrentPlayer() {
        if (currentPlayer == playerOne)
            currentPlayer = playerTwo;
        else currentPlayer = playerOne;
    }

    /**
     * Checks if the game is over (there are no more card pairs left unmatched)
     * Checks the users scores and declares the winner or a draw in a Toast message.
     */
    private void checkIfGameIsOver() {

        if (pairsRemaining == 0) {
            if (playerOne.getCurrentScore() == playerTwo.getCurrentScore()) {
                String displayWinningText = "Game ends in a draw with a score of " + playerOne.getCurrentScore() + " each";
                Toast.makeText(getContext(), displayWinningText, Toast.LENGTH_SHORT).show();
            } else {
                Player winningPlayer = (playerOne.getCurrentScore()) > playerTwo.getCurrentScore() ? playerOne : playerTwo;
                String displayWinningText = winningPlayer.getPlayerId() + " wins, with a score of " + winningPlayer.getCurrentScore();
                Toast.makeText(getContext(), displayWinningText, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void init() {

        gridLinesColour = new Paint(Paint.ANTI_ALIAS_FLAG);
        gridLinesColour.setColor(getResources().getColor(R.color.black));
        gridLinesColour.setStyle(Paint.Style.STROKE);
        gridLinesColour.setStrokeWidth(getResources().getDisplayMetrics().density * 5);

        cardFaceDownColour = new Paint(Paint.ANTI_ALIAS_FLAG);
        cardFaceDownColour.setStyle(Paint.Style.FILL);
        cardFaceDownColour.setColor(getResources().getColor(R.color.gray));

        transparent = new Paint(Paint.ANTI_ALIAS_FLAG);
        transparent.setStyle(Paint.Style.FILL);
        transparent.setColor(getResources().getColor(R.color.transparent));

        isCardOneFlipped = false;
        isCardTwoFlipped = false;

        playerOne = new Player("Player One");
        playerTwo = new Player("Player Two");
        currentPlayer = playerOne;
        pairsRemaining = 8;
    }

    /**
     * Overridden method to force our custom view to be square in both portrait and landscape modes.
     * This method is not my own work and is taken in its entirety from
     * https://blog.jayway.com/2012/12/12/creating-custom-android-views-part-4-measuring-and-how-to-force-a-view-to-be-square/
     * @param widthMeasureSpec
     * Provided by the Android Framework
     * @param heightMeasureSpec
     * Provided by the Android Framework
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int size;
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int widthWithoutPadding = width - getPaddingLeft() - getPaddingRight();
        int heightWithoutPadding = height - getPaddingTop() - getPaddingBottom();

        // set the dimensions
        if (widthWithoutPadding > heightWithoutPadding) {
            size = heightWithoutPadding;
            canvasWidthAndHeight = heightWithoutPadding;
        } else {
            size = widthWithoutPadding;
            canvasWidthAndHeight = widthWithoutPadding;
        }

        setMeasuredDimension(size + getPaddingLeft() + getPaddingRight(), size + getPaddingTop() + getPaddingBottom());
    }

    /**
     * Draw the grid lines in between each card using the partition ratio defined when the class is
     * created. We use 1 / 4f to give us a four by four grid which will contain our sixteen cards
     * @param canvas
     * The canvas where our grid lines will be drawn
     */
    private void drawGridLines(Canvas canvas) {

        for (int i = 1; i <= 3; i++) {
            canvas.drawLine(
                    canvasWidthAndHeight * (i * partitionRatio),
                    0f,
                    canvasWidthAndHeight * (i * partitionRatio),
                    canvasWidthAndHeight,
                    gridLinesColour);

            canvas.drawLine(
                    0f,
                    canvasWidthAndHeight * (i * partitionRatio),
                    canvasWidthAndHeight,
                    canvasWidthAndHeight * (i * partitionRatio),
                    gridLinesColour);
        }
    }

    /**
     * Generate a two dimensional array - four by four - containing the Rectangle objects that will
     * be placed in our cards. Using our partition ratio of 1 / 4f gives us the four by four grid.
     * @return
     * The four by four array of rectangles that will be used to construct the card objects
     */
    private Rect[][] generateTiles() {
        Rect[][] tiles = new Rect[4][4];
        int offset = (int) (canvasWidthAndHeight * partitionRatio);
        for (int j = 0; j <= 3; j++) {
            for (int i = 0; i <= 3; i++) {
                tiles[i][j] = new Rect(i * offset, j * offset, (i + 1) * offset, (j + 1) * offset);
            }
        }
        return tiles;
    }

    /**
     * Draw the cards and grid lines on our canvas, asking each card what colour it is currently if
     * it is face up
     * @param canvas
     * The canvas where our cards and grid lines will be drawn
     */
    private void drawCardsAndGrid(Canvas canvas) {
        Paint currentColour;
        for (Card card : allCards) {
            if (card.isFaceUp())
                currentColour = card.getColour();
            else currentColour = cardFaceDownColour;
            canvas.drawRect(card.getCardRect(), currentColour);
        }
        drawGridLines(canvas);
    }
}
