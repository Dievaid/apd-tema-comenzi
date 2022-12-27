import java.util.Scanner;
import java.util.concurrent.ExecutorService;

public class ProductTask implements Runnable {
    private final String orderId;
    private final Integer noProducts;
    private final Scanner productScanner;

    public ProductTask(String orderId, Integer noProducts, Scanner productScanner) {
        this.orderId = orderId;
        this.noProducts = noProducts;
        this.productScanner = productScanner;
    }

    @Override
    public void run() {
        Integer productCounter = 0;
        ExecutorService service = Controller.getInstance().getExecutorService();
        while (productScanner.hasNextLine()) {
            String line = productScanner.nextLine();

            String[] splStrings = line.split(",");

            String currentOrderId = splStrings[0];
            String currentOrderProductId = splStrings[1];

            //! Ship product
            if (currentOrderId.equals(orderId)) {
                Controller.getInstance().getPhaser().register();
                service.submit(new ProductWriter(orderId, currentOrderProductId));
                productCounter++;
            }

            //! If the entire order was shipped
            if (noProducts == productCounter) {
                Controller.getInstance().getPhaser().register();
                service.submit(new OrderWriter(orderId, productCounter));
                break;
            }
        }
        Controller.getInstance().getPhaser().arrive();
    }
}
