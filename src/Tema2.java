import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

public class Tema2 {
    public static final File orderOut = new File("orders_out.txt");
    public static final File orderProductsOutput = new File("order_products_out.txt");

    public static void main(String[] args) throws FileNotFoundException, IOException {
        String folderPath = new String(args[0]);
        Integer threads = Integer.parseInt(args[1]);

        ExecutorService orderService = Executors.newFixedThreadPool(threads);
        ExecutorService prodService = Executors.newFixedThreadPool(threads);
        Phaser phaser = new Phaser(1);

        File ordersFile = new File(folderPath + "/orders.txt");
        File productsFile = new File(folderPath + "/order_products.txt");

        FileWriter productWriter = new FileWriter(orderProductsOutput);
        FileWriter orderWriter = new FileWriter(orderOut);

        Controller.getInstance().setOrderWriter(orderWriter);
        Controller.getInstance().setProductWriter(productWriter);
        Controller.getInstance().setOrderService(orderService);
        Controller.getInstance().setProductService(prodService);
        Controller.getInstance().setPhaser(phaser);

        Scanner orderScanner = new Scanner(ordersFile);

        for (int i = 0; i < threads; i++) {
            phaser.register();
            orderService.submit(new OrderTask(orderScanner, productsFile));
        }
        phaser.arriveAndDeregister();

        phaser.register();
        phaser.arriveAndAwaitAdvance();

        productWriter.close();
        orderWriter.close();
        orderService.shutdown();
        prodService.shutdown();
    }
}