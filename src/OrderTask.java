import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;

public class OrderTask implements Runnable {
    private final Scanner orderScanner;
    private final File productsFile;

    public OrderTask(Scanner orderScanner, File productsFile) {
        this.orderScanner = orderScanner;
        this.productsFile = productsFile;
    }

    @Override
    public void run() {
        String line = null;
        ExecutorService service = Controller.getInstance().getExecutorService();
        while (true) {
            synchronized (orderScanner) {
                try {
                    line = orderScanner.nextLine();
                }
                catch (NoSuchElementException e) {
                    break;
                }
            }
            String[] splStrings = line.split(",");
            
            //Prepare data for product managing
            String orderId = splStrings[0];
            Integer noProducts = Integer.parseInt(splStrings[1]);
            try {
                if (noProducts != 0) {
                    Scanner scanner = new Scanner(productsFile);
                    Controller.getInstance().getPhaser().register();
                    service.submit(new ProductTask(orderId, noProducts, scanner));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        Controller.getInstance().getPhaser().arrive();
    }
}
