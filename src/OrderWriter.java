import java.io.IOException;

public class OrderWriter implements Runnable {
    private final String orderId;
    private final String numberOfProducts;

    public OrderWriter(String orderId, Integer numberOfProducts) {
        this.orderId = orderId;
        this.numberOfProducts = Integer.toString(numberOfProducts);
    }

    @Override
    public void run() {
        var fileWriter = Controller.getInstance().getOrderWriter();

        StringBuilder stringBuilder = new StringBuilder(orderId);
        stringBuilder.append(",").append(numberOfProducts).append(",shipped\n");

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
