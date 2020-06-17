package model;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

/**
 * Date  20/04/2020 <br>
 * Represent a basic stopwatch to watch out time spent between two calls
 */
public class Stopwatch implements ActionListener {

    private int hours, minutes, seconds;
    private Timer timer;

    /**
     * Default constructor
     */
    public Stopwatch() {
        this.hours = 0;
        this.minutes = 0;
        this.seconds = 0;
        timer = new Timer(1000, this);
    }

    /**
     * Action to be performed each second
     * @param actionEvent the action event
     */
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

    /**
     * Start the timer
     */
    public void start(){
        timer.start();
    }

    /**
     * stop the timer
     */
    public void stop(){
        timer.stop();
    }

    /**
     * @return true if the timer is running
     */
    public boolean isRunning() {
        return timer.isRunning();
    }

    /**
     * reset the timer to 0
     */
    public void reset(){
        if (timer.isRunning())
            stop();
        this.hours = 0;
        this.minutes = 0;
        this.seconds = 0;
    }

    /**
     * @return a formatted string representing time as HH:MM:SS
     */
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
