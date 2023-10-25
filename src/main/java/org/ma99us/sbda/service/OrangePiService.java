package org.ma99us.sbda.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * GPIO utility interface.
 * See http://wiringpi.com/the-gpio-utility/
 */
@Slf4j
@Service
public class OrangePiService {

    @Value("${gpio-path:/usr/local/bin/gpio}")
    private String GPIO;

    @Value("${gpio-out-pins:3,4,6}")
    private String outPinsStr;

    @Value("${mjpg-streamer-script:/home/orangepi/mjpg-streamer/mjpg-streamer-experimental/runme.sh}")
    private String MJPG_STREAMER;

    @Getter
    private String statusText = "";

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private FutureCommand mjpgFuture;

    public OrangePiService() {
    }

    public void init() {
        log.debug("OrangePiService init()");
        updateStatus();

        if (outPinsStr != null && !outPinsStr.isEmpty()) {
            String[] split = outPinsStr.split(",");
            for (String pin : split) {
                gpioPinMode(Integer.parseInt(pin), "out");
            }
        }
    }

    @PreDestroy
    public void deinit() {
        log.debug("OrangePiService deinit()");
        stopMjpgStreamer();
        executor.shutdownNow();
    }

    private void updateStatus() {
        statusText = "";
        validatePlatform();
        if (statusText.isEmpty()) {
            statusText = "Ready.";
        }
    }

    private void validatePlatform() {
        File gpioFile = new File(GPIO);
        if (!gpioFile.isFile()) {
            appendStatus("GPIO tool is not found at " + GPIO + "!");
        }

        File mjpgStreamerFile = new File(MJPG_STREAMER);
        if (!mjpgStreamerFile.isFile()) {
            appendStatus("mjpg-streamer tool is not found at " + MJPG_STREAMER + "!");
        }
    }

    private void appendStatus(String stat) {
        log.warn(stat);
        if (!statusText.isEmpty()) {
            statusText += ", ";
        }
        statusText += stat;
    }

    public boolean isBadStatus() {
        return statusText != null && !statusText.isEmpty() && !statusText.equals("Ready.") && !statusText.equals("Ok.");
    }

    private String runCommand(String command) {
        try {
            log.debug("> \"{}\"", command);
            Process p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader buf = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = buf.readLine()) != null) {
                if (output.length() != 0) {
                    output.append("\n");
                }
                output.append(line);
            }
            String res = output.toString();
            if (!res.isEmpty()) {
                log.debug("< {}", dumpText(res));
            }
            return res;
        } catch (Exception ex) {
            log.error("Command {} failed", command, ex);
            throw new RuntimeException(ex);
        }
    }

    public String gpioStatusText() {
        return runCommand("gpio readall");
    }

    public String gpioPinMode(int pin, String mode) {
        return runCommand("gpio mode " + pin + " " + mode);
    }

    public String gpioPinWrite(int pin, boolean isHigh) {
        return runCommand("gpio write " + pin + " " + (isHigh ? "1" : "0"));
    }

    public String gpioPinRead(int pin) {
        return runCommand("gpio read " + pin);
    }

    public void startMjpgStreamer() {
        synchronized (executor) {
            stopMjpgStreamer();
            mjpgFuture = new FutureCommand(MJPG_STREAMER);
            executor.submit(mjpgFuture);
        }
    }

    public void stopMjpgStreamer() {
        synchronized (executor) {
            if (mjpgFuture != null) {
                mjpgFuture.cancel();
                mjpgFuture = null;
            }
        }
    }

    public void goToSleep(int minutes) {
        runCommand("sudo rtcwake -m mem -s " + (60 * minutes));
    }

    private static String dumpText(String text) {
        if (text == null) {
            return "";
        }
        if (text.length() > 80) {
            return "[" + text.length() + " chars]";
        } else {
            return text;
        }
    }
}
