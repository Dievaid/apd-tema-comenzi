import java.io.FileWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Phaser;

public class Controller {
    private FileWriter productWriter;
    private FileWriter orderWriter;
    private ExecutorService executorService;
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

    public ExecutorService getExecutorService() {
        return executorService;
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

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public void setPhaser(Phaser phaser) {
        this.phaser = phaser;
    }
}