import java.util.Scanner;
import java.io.IOException;

public class ProductTask implements Runnable {
    private final String orderId;
    private final Scanner productScanner;

    public ProductTask(String orderId, Scanner productScanner) {
        this.orderId = orderId;
        this.productScanner = productScanner;
    }

    @Override
    public void run() {
        Integer productCounter = 0;
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
                productCounter++;
            }
        }
        Controller.getInstance().getPhaser().arrive();
    }
}
