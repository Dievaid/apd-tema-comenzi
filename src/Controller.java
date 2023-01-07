import java.io.FileWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Phaser;

public class Controller {
    private FileWriter productWriter;
    private FileWriter orderWriter;
    private ExecutorService productService;
    private ExecutorService orderService;
    private Phaser phaser;

    private static final Controller instance = new Controller();

    private Controller() { }

    public static synchronized Controller getInstance() {
        return instance;
    }

    public FileWriter getOrderWriter() {
        return orderWriter;
    }

    public FileWriter getProductWriter() {
        return productWriter;
    }

    public ExecutorService getProductService() {
        return productService;
    }

    public ExecutorService getOrderService() {
        return orderService;
    }

    public Phaser getPhaser() {
        return phaser;
    }

    public void setOrderWriter(FileWriter orderWriter) {
        this.orderWriter = orderWriter;
    }

    public void setProductWriter(FileWriter productWriter) {
        this.productWriter = productWriter;
    }

    public void setOrderService(ExecutorService orderService) {
        this.orderService = orderService;
    }

    public void setProductService(ExecutorService productService) {
        this.productService = productService;
    }

    public void setPhaser(Phaser phaser) {
        this.phaser = phaser;
    }
}