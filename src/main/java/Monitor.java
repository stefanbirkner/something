import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.nio.file.StandardOpenOption.*;
import static java.util.Collections.singleton;
import static java.util.concurrent.Executors.newSingleThreadExecutor;

public class Monitor {
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private final Path log;

    public static void main(String... args) throws Exception {
        createAndStartMonitor(args);
    }

    static Monitor createAndStartMonitor(String... args) throws Exception {
        Path log = Paths.get(args[0]);
        Monitor monitor = new Monitor(log);
        monitor.start();
        return monitor;
    }

    private Monitor(Path log) {
        this.log = log;
    }

    private void start() {
        log("DEBUG Monitor started");
        executorService.scheduleWithFixedDelay(this::monitorServer, 0, 100, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        executorService.shutdown();
    }

    private void monitorServer() {
        log("ERROR Failed to connect to server");
    }

    private void log(String message) {
        try {
            Files.write(log, singleton(message), APPEND);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
