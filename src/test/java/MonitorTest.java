import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.nio.file.Files;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class MonitorTest {
    @Rule
    public final TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void writes_to_log_that_it_was_started() throws Exception {
        File logFile = temporaryFolder.newFile();
        Monitor.createAndStartMonitor(logFile.getAbsolutePath());
        assertThat(logFile).hasContent("DEBUG Monitor started");
    }

    @Test
    public void does_not_override_old_log_but_appends_new_logs() throws Exception {
        File logFile = temporaryFolder.newFile();
        Files.write(
            logFile.toPath(),
            Collections.singleton("existing logs")
        );
        Monitor.createAndStartMonitor(logFile.getAbsolutePath());
        assertThat(logFile).hasContent("existing logs\nDEBUG Monitor started");
    }
}
