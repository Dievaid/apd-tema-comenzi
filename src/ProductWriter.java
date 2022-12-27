import java.io.IOException;

public class ProductWriter implements Runnable {
    private final String orderId;
    private final String productId;
    
    public ProductWriter(String orderId, String productId) {
        this.orderId = orderId;
        this.productId = productId;
    }

    @Override
    public void run() {
        var fileWriter = Controller.getInstance().getProductWriter();

        StringBuilder stringBuilder = new StringBuilder(orderId);
        stringBuilder.append(",").append(productId).append(",shipped\n");

        synchronized (fileWriter) {
            try {
                fileWriter.write(stringBuilder.toString());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        Controller.getInstance().getPhaser().arrive();
    }
}
