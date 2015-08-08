package sg.nemysolutions.hapme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.parse.Parse;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "0RgU1BAusmGZiIkOFDVucZcEtbCHPiJx479CDcKG", "GhyTkHlqG22YziVd7fbP8YNTYK6wbmrcwF99yM5G");

        Button bn1 = (Button) findViewById(R.id.button);
        Button bn2 = (Button) findViewById(R.id.button2);
        Button bn3 = (Button) findViewById(R.id.button3);
        Button bn4 = (Button) findViewById(R.id.button4);
        Button bn5 = (Button) findViewById(R.id.button5);

        bn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Add.class);
                startActivity(intent);
            }
        });

        bn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Commands.class);
                startActivity(intent);
            }
        });
        bn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Create.class);
                startActivity(intent);
            }
        });
        bn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
            }
        });
        bn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Operation.class);
                startActivity(intent);
            }
        });

    }

}