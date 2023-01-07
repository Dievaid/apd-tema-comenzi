import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.IOException;

public class ProductTask implements Runnable {
    private final String orderId;
    private final Scanner productScanner;
    private final CyclicBarrier barrier;
    private final AtomicInteger productNoTracker;

    public ProductTask(String orderId, Scanner productScanner, CyclicBarrier barrier, AtomicInteger productNoTracker) {
        this.orderId = orderId;
        this.productScanner = productScanner;
        this.barrier = barrier;
        this.productNoTracker = productNoTracker;
    }

    @Override
    public void run() {
        var fileWriter = Controller.getInstance().getProductWriter();
        while (productScanner.hasNextLine()) {
            String line = productScanner.nextLine();

            String[] splStrings = line.split(",");

            String currentOrderId = splStrings[0];
            String currentOrderProductId = splStrings[1];

            //! Ship product
            if (currentOrderId.equals(orderId)) {
                StringBuilder stringBuilder = new StringBuilder(orderId);
                stringBuilder.append(",").append(currentOrderProductId).append(",shipped\n");

                synchronized (fileWriter) {
                    try {
                        fileWriter.write(stringBuilder.toString());
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                productNoTracker.incrementAndGet();
            }
        }
        try {
            barrier.await();
        }
        catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
        Controller.getInstance().getPhaser().arrive();
    }
}
