import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import static java.nio.file.StandardOpenOption.*;

public class Monitor {
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

    private void start() throws IOException {
        Files.write(
            log,
            Collections.singleton("DEBUG Monitor started"),
            APPEND
        );
    }
}
