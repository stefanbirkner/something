import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
    private final OkHttpClient client = new OkHttpClient();
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
        try {
            Request request = new Request.Builder()
                .url("http://localhost:12345")
                .build();

            Response response = client.newCall(request).execute();
            int code = response.code();
            if (code == 200) {
                log("INFO Server is healthy");
            } else {
                log("ERROR Server returned status code " + code);
            }
        } catch (Exception e) {
            log("ERROR Failed to connect to server");
        }
    }

    private void log(String message) {
        try {
            Files.write(log, singleton(message), APPEND);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
