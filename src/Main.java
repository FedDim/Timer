import java.util.Scanner;

public class Main {

    private static final int PROGRESS_BAR_WIDTH = 50;

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws InterruptedException {

        int timer = getValidTimerValue();

        System.out.println("\nStarting timer... ");
        Thread timerThread = startTimer(timer);
        timerThread.join();

        System.out.println("\nTimer finished!");
    }

    private static int getValidTimerValue(){
        int timer = 0;
        while (true){
            System.out.println("Enter timer value in seconds: ");
            try{
                timer = scanner.nextInt();
                if(timer > 0){
                    break;
                }
                else
                {
                    System.out.println("Timer value must be greater than 0.");
                }
            }
            catch (Exception e){
                System.out.println("Invalid input. Please enter a valid timer value.");
                scanner.nextLine();
            }
        }
        scanner.close();
        return timer;
    }

    private static String getProgressBar(float progress){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");

        int completed = (int) ((1- progress) * PROGRESS_BAR_WIDTH);
        int remaining = PROGRESS_BAR_WIDTH - completed;

        for(int i = 0; i < completed; i++){
            stringBuilder.append("\033[32m█\033[0m");
        }
        for(int i = 0; i < remaining; i++){
            stringBuilder.append("\033[31m█\033[0m");
        }

        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    private static String getCountdownTimer(int timer, long elapsedTime){
        long remainigTime = timer * 1000 - elapsedTime;
        float remainingPercentage = (float) remainigTime / (timer * 1000);

        remainingPercentage = Math.round(remainingPercentage * 100) / 100f;
        remainigTime = remainigTime / 1000 + 1;

        return String.format("%02d:%02d (%.1f%%)", remainigTime / 60, remainigTime % 60, remainingPercentage);
    }

    private static Thread startTimer(int timer){
        Thread thread = new Thread(() -> {
            long startTime = System.currentTimeMillis();
            long elaspsedTime = 0;

            while (elaspsedTime < timer * 1000){
                try
                {
                    Thread.sleep(100);
                }
                catch (InterruptedException e){
                    break;
                }
                elaspsedTime = System.currentTimeMillis() - startTime;
                float progress = (float) elaspsedTime/(timer*1000);
                String progressBar = getProgressBar(progress);
                String countdownTimer = getCountdownTimer(timer, elaspsedTime);
                System.out.print("\r" + progressBar + " " + countdownTimer);
            }
        });
        thread.start();
        return thread;
    }

}