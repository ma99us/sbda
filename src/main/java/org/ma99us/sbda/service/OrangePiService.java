package org.ma99us.sbda.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

@Slf4j
@Service
public class OrangePiService {

    @Value("${gpio-path:/usr/local/bin/gpio}")
    private String GPIO;

    private String statusText = "";

    public OrangePiService() {
    }

    private void updateStatus() {
        statusText = "";
        checkPlatformTools();
        if (statusText.isEmpty()) {
            statusText = "Ready.";
        }
    }

    private void checkPlatformTools() {
        File gpioFile = new File(GPIO);
        if (!gpioFile.isFile()) {
            appendStatus("GPIO tool is not found at " + GPIO + "!");
        }
    }

    private void appendStatus(String stat) {
        log.warn(stat);
        if (!statusText.isEmpty()) {
            statusText += ", ";
        }
        statusText += stat;
    }

    public String getStatusText() {
        updateStatus();
        return statusText;
    }

    public String gpioStatusText() {
        try {
            Process p = Runtime.getRuntime().exec("gpio readall");
            p.waitFor();
            BufferedReader buf = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = buf.readLine()) != null) {
                output.append(line + "\n");
            }
            return output.toString().trim();
        } catch (Exception ex) {
            log.error("gpioStatusText error", ex);
            throw new RuntimeException(ex);
        }
    }

    public String gpioPinMode(int pin, String mode) {
        try {
            Process p = Runtime.getRuntime().exec("gpio mode " + pin + " " + mode);
            p.waitFor();
            BufferedReader buf = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = buf.readLine()) != null) {
                output.append(line + "\n");
            }
            return output.toString().trim();
        } catch (Exception ex) {
            log.error("gpioPinMode error", ex);
            throw new RuntimeException(ex);
        }
    }

    public String gpioPinWrite(int pin, boolean isHigh) {
        try {
            Process p = Runtime.getRuntime().exec("gpio write " + pin + " " + (isHigh ? "1" : "0"));
            p.waitFor();
            BufferedReader buf = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = buf.readLine()) != null) {
                output.append(line + "\n");
            }
            return output.toString().trim();
        } catch (Exception ex) {
            log.error("gpioPinWrite error", ex);
            throw new RuntimeException(ex);
        }
    }
}
