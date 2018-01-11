import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.junit.MockServerRule;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;

import static java.nio.file.Files.readAllLines;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class MonitorTest {
    @Rule
    public final TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Rule
    public MockServerRule mockServerRule = new MockServerRule(this, 12345);

    private MockServerClient mockServerClient;

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
        assertThat(content(logFile).size()).isGreaterThanOrEqualTo(3);
    }

    @Test
    public void logs_healthy_server_as_info() throws Exception {
        useServerThatReturnsStatusCode(200);
        File logFile = temporaryFolder.newFile();

        startMonitorWithLogfile(logFile);

        Thread.sleep(500);
        assertThat(content(logFile))
            .contains(
                "INFO Server is healthy"
            );
    }

    @Test
    public void logs_faulty_server_as_error() throws Exception {
        useServerThatReturnsStatusCode(500);
        File logFile = temporaryFolder.newFile();

        startMonitorWithLogfile(logFile);

        Thread.sleep(500);
        assertThat(content(logFile))
            .contains(
                "ERROR Server returned status code 500"
            );
    }

    private void useServerThatReturnsStatusCode(int code) {
        mockServerClient
            .when(request().withPath(""))
            .respond(response().withStatusCode(code));
    }

    private void startMonitorWithLogfile(File log) throws Exception {
        monitor = Monitor.createAndStartMonitor(log.getAbsolutePath());
    }

    private List<String> content(File file) throws IOException {
        return readAllLines(file.toPath());
    }
}
