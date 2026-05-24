package ge.credo.automation.driver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class PortUtil {

    private static final Logger LOG = LogManager.getLogger(PortUtil.class);

    private PortUtil() {
    }

    public static void freePort(int port) {
        String osName = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
        try {
            if (osName.contains("win")) {
                freePortWindows(port);
            } else {
                freePortUnix(port);
            }
        } catch (IOException | InterruptedException e) {
            LOG.warn("Failed to free port {}: {}", port, e.getMessage());
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static void freePortWindows(int port) throws IOException, InterruptedException {
        List<String> output = runAndRead(new ProcessBuilder(
                "cmd.exe", "/c", "netstat -ano | findstr :" + port));
        for (String line : output) {
            if (!line.contains("LISTENING")) {
                continue;
            }
            String[] tokens = line.trim().split("\\s+");
            String pid = tokens[tokens.length - 1];
            killPid(pid, port, new ProcessBuilder("taskkill", "/F", "/PID", pid));
        }
    }

    private static void freePortUnix(int port) throws IOException, InterruptedException {
        List<String> pids = runAndRead(new ProcessBuilder("sh", "-c", "lsof -ti :" + port));
        for (String pid : pids) {
            if (pid.isBlank()) {
                continue;
            }
            killPid(pid.trim(), port, new ProcessBuilder("kill", "-9", pid.trim()));
        }
    }

    private static void killPid(String pid, int port, ProcessBuilder killCommand)
            throws IOException, InterruptedException {
        killCommand.redirectErrorStream(true);
        Process proc = killCommand.start();
        proc.getInputStream().readAllBytes();
        proc.waitFor();
        LOG.info("Released port {} (killed PID {})", port, pid);
    }

    private static List<String> runAndRead(ProcessBuilder pb) throws IOException, InterruptedException {
        pb.redirectErrorStream(true);
        Process proc = pb.start();
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(proc.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        proc.waitFor();
        return lines;
    }
}
