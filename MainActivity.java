
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Bootstraps the application
 */
public class MainActivity extends AppCompatActivity {

    private TextView scores;

    /**
     * Bootstraps the application, setting up the custom view for the game canvas, the TextView for
     * the player scores, and a button to reset the game.
     * The reset button wipes the current state of the application and restarts the application
     * entirely.
     * @param savedInstanceState
     * Passed in by the Android Framework
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scores = findViewById(R.id.scores);
        scores.setText("Player one: 0. Player two: 0. \nCurrent player: Player One");

        Button resetGameButton = findViewById(R.id.resetButton);
        resetGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
    }

    /**
     * Updates the text view containing the players scores and the player whose turn it is
     * @param updateText
     * The text to be displayed in the TextView scores
     */
    void update(String updateText) {
        scores.setText(updateText);
    }
}
