package org.ma99us.sbda.service;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

/**
 * Asynchronous, cancellable, long-lasting system process executable/command
 */
@Slf4j
public class FutureCommand implements Callable<String> {
    private final String command;
    private Process process;
    private volatile boolean isCancelled;

    public FutureCommand(String command) {
        this.command = command;
    }

    @Override
    public String call() throws Exception {
        try {
            log.debug("> \"{}\"", command);
            process = Runtime.getRuntime().exec(command);
            process.waitFor();
            if (isCancelled) {
                throw new InterruptedException("cancel");
            }
            BufferedReader buf = new BufferedReader(new InputStreamReader(process.getInputStream()));
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
                log.debug("< {}", res);
            }
            return res;
        } catch (InterruptedException iex) {
            log.warn("Command {} interrupted by: {}", command, iex.getMessage());
            return "";
        } catch (Exception ex) {
            log.error("Command {} failed", command, ex);
            throw new RuntimeException(ex);
        } finally {
            process = null;
        }
    }

    public void cancel() {
        if (process != null && !isCancelled) {
            isCancelled = true;
//            log.warn("cancel {}", command);
            process.descendants().forEach(p -> p.destroy());
            process.destroy();
        }
    }
}
