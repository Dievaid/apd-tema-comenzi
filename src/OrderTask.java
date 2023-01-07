import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderTask implements Runnable {
    private final Scanner orderScanner;
    private final File productsFile;

    public OrderTask(Scanner orderScanner, File productsFile) {
        this.orderScanner = orderScanner;
        this.productsFile = productsFile;
    }

    public void setShippedOrderStatus(String orderStatus) {
        FileWriter writer = Controller.getInstance().getOrderWriter();
        synchronized (writer) {
            try {
                writer.write(orderStatus);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        String line = null;
        ExecutorService service = Controller.getInstance().getProductService();
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
            CyclicBarrier barrier = new CyclicBarrier(2);
            AtomicInteger productNoTracker = new AtomicInteger(0);
            try {
                if (noProducts != 0) {
                    Scanner scanner = new Scanner(productsFile);
                    Controller.getInstance().getPhaser().register();
                    service.submit(new ProductTask(orderId, scanner, barrier, productNoTracker));                
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            if (noProducts != 0) {
                StringBuilder orderStatusBuilder = new StringBuilder(orderId);
                orderStatusBuilder.append(",").append(noProducts).append(",shipped\n");
                try {
                    barrier.await();
                }
                catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
                if (productNoTracker.get() == noProducts) {
                    setShippedOrderStatus(orderStatusBuilder.toString());
                }
            }
        }

        Controller.getInstance().getPhaser().arrive();
    }
}
