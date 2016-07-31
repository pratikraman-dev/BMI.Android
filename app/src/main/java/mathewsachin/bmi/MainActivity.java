package mathewsachin.bmi;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    EditText massText, heightText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        massText = (EditText) findViewById(R.id.WeightText);
        heightText = (EditText) findViewById(R.id.HeightText);

        TextView.OnEditorActionListener listener = new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN)
                    Solve(v.getRootView());

                return true;
            }
        };

        massText.setOnEditorActionListener(listener);
        heightText.setOnEditorActionListener(listener);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Solve(view);
            }
        });
    }

    private void Solve(View view) {
        if (massText.getText().toString().isEmpty())
        {
            Toast.makeText(MainActivity.this, "Empty Weight", Toast.LENGTH_SHORT).show();
            return;
        }

        if (heightText.getText().toString().isEmpty())
        {
            Toast.makeText(MainActivity.this, "Empty Height", Toast.LENGTH_SHORT).show();
            return;
        }

        double massInKg;

        try { massInKg = Double.parseDouble(massText.getText().toString()); }
        catch (Exception e)
        {
            Toast.makeText(MainActivity.this, "Invalid Weight", Toast.LENGTH_SHORT).show();
            return;
        }

        if (((RadioButton)findViewById(R.id.PoundUnit)).isChecked())
            massInKg *= 0.45359237;

        String heightString = heightText.getText().toString();

        double heightInCm;

        try
        {
            if (((RadioButton) findViewById(R.id.fiUnit)).isChecked()) {
                if (heightString.indexOf("'") == -1)
                    heightInCm = Integer.parseInt(heightString) * 2.54 * 12;
                else {
                    String[] splitted = heightString.split("'");
                    int[] splittedInts = new int[]{Integer.parseInt(splitted[0]), Integer.parseInt(splitted[1])};
                    heightInCm = (splittedInts[0] * 12 + splittedInts[1]) * 2.54;
                }
            } else heightInCm = Double.parseDouble(heightString);
        }
        catch (Exception e)
        {
            Toast.makeText(MainActivity.this, "Invalid Height", Toast.LENGTH_SHORT).show();
            return;
        }

        if (heightInCm <= 0)
        {
            Toast.makeText(MainActivity.this, "Invalid Height", Toast.LENGTH_SHORT).show();
            return;
        }

        if (massInKg <= 0)
        {
            Toast.makeText(MainActivity.this, "Invalid Weight", Toast.LENGTH_SHORT).show();
            return;
        }

        final double bmi = massInKg * 10000 / (heightInCm * heightInCm);
        final String bmiString = new DecimalFormat("#0.00").format(bmi) + " (" + GetRemark(bmi) + ")";

        Snackbar.make(view, bmiString, Snackbar.LENGTH_INDEFINITE)
                .setAction("COPY", new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("BMI", bmiString);
                        clipboard.setPrimaryClip(clip);
                    }
                }).show();
    }

    static String GetRemark(double BmiValue)
    {
        if (BmiValue < 18.5)
            return "Underweight";

        if (BmiValue >= 30)
            return "Obesity";

        if (BmiValue >= 18.5 && BmiValue < 25)
            return "Normal";

        if (BmiValue >= 25 && BmiValue < 30)
            return "Overweight";

        return "Error";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
            return true;

        return super.onOptionsItemSelected(item);
    }
}
