package edu.sdu.rnyati.drawingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private static final String DEFAULT_COLOR = "black";
    private static final String DEFAULT_MODE = "draw";

    private DrawingView drawingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawingView = new DrawingView(this);
        drawingView.setDefaultColor(DEFAULT_COLOR);
        drawingView.setDefaultMode(DEFAULT_MODE);
        setContentView(this.drawingView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        menu.findItem(R.id.color_mode).getSubMenu().findItem(R.id.black).setChecked(true);
        menu.findItem(R.id.app_mode).getSubMenu().findItem(R.id.draw).setChecked(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.black:
                drawingView.setDefaultColor("black");
                item.setChecked(true);
                break;
            case R.id.red:
                drawingView.setDefaultColor("red");
                item.setChecked(true);
                break;
            case R.id.blue:
                drawingView.setDefaultColor("blue");
                item.setChecked(true);
                break;
            case R.id.green:
                drawingView.setDefaultColor("green");
                item.setChecked(true);
                break;
            case R.id.draw:
                drawingView.setDefaultMode("draw");
                item.setChecked(true);
                break;
            case R.id.move:
                this.drawingView.setDefaultMode("move");
                item.setChecked(true);
                break;
            case R.id.delete:
                drawingView.setDefaultMode("delete");
                item.setChecked(true);
                break;
            default:
                break;
        }
        return true;
    }
}
