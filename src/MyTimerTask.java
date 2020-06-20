import java.util.TimerTask;

public class MyTimerTask extends TimerTask {
    private RentedCarWindow controller;
    @Override
    public void run() {
        controller.tick();
    }
    public MyTimerTask(RentedCarWindow controller)
    {
        super();
        this.controller= controller;
    }
}
