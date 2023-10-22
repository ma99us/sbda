package org.ma99us.sbda.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;


/**
 * GPIO utility interface.
 * See http://wiringpi.com/the-gpio-utility/
 */
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

    private String runCommand(String command){
        try {
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
            return output.toString();
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
}
