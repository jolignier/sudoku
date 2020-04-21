package model;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableValueBase;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Properties;

/**
 * @author Ipro
 * Date  20/04/2020
 */

public class Stopwatch implements ActionListener {

    private int hours,minutes, seconds;
    private Timer timer;

    public Stopwatch() {
        this.hours = 0;
        this.minutes = 0;
        this.seconds = 0;
        timer = new Timer(1000, this);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        seconds++;
        if (seconds ==60) {
            minutes++;
            seconds =0;
            if (minutes==60){
                hours++;
                minutes=0;
            }
        }
    }

    public void start(){
        timer.start();
    }

    public void stop(){
        timer.stop();
    }

    public boolean isRunning() {
        return timer.isRunning();
    }

    public void reset(){
        if (timer.isRunning())
            stop();
        this.hours = 0;
        this.minutes = 0;
        this.seconds = 0;
    }

    public String getTime() {
        String res = "";
        DecimalFormat formatter = new DecimalFormat("00");
        String h = formatter.format(hours);
        String m = formatter.format(minutes);
        String s = formatter.format(seconds);
        if (hours >0) {
            res = h +":"+m+":"+s;
        } else {
            res = m+":"+s;
        }
        return res;
    }
}
