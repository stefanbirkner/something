import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;

import static java.nio.file.Files.readAllLines;
import static org.assertj.core.api.Assertions.assertThat;

public class MonitorTest {
    @Rule
    public final TemporaryFolder temporaryFolder = new TemporaryFolder();

    private Monitor monitor;

    @After
    public void stopMonitor() {
        if (monitor != null)
            monitor.stop();
    }
    @Test
    public void writes_to_log_that_it_was_started() throws Exception {
        File logFile = temporaryFolder.newFile();
        startMonitorWithLogfile(logFile);
        assertThat(content(logFile)).contains("DEBUG Monitor started");
    }

    @Test
    public void does_not_override_old_log_but_appends_new_logs() throws Exception {
        File logFile = temporaryFolder.newFile();
        Files.write(
            logFile.toPath(),
            Collections.singleton("existing logs")
        );
        startMonitorWithLogfile(logFile);
        assertThat(content(logFile))
            .containsSequence(
                "existing logs",
                "DEBUG Monitor started"
            );
    }

    @Test
    public void keeps_running() throws Exception {
        File logFile = temporaryFolder.newFile();
        startMonitorWithLogfile(logFile);
        Thread.sleep(500);
        assertThat(content(logFile))
            .containsSequence(
                "ERROR Failed to connect to server",
                "ERROR Failed to connect to server"
            );
    }

    private void startMonitorWithLogfile(File log) throws Exception {
        monitor = Monitor.createAndStartMonitor(log.getAbsolutePath());
    }

    private List<String> content(File file) throws IOException {
        return readAllLines(file.toPath());
    }
}
