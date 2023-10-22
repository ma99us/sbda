package org.ma99us.sbda;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@SpringBootApplication
@Slf4j
public class App {
    @Value("${server.port}")
    private String serverPort;

    @Value("${open-browser:false}")
    private boolean openBrowser;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onStartup() {
        try {
            if (openBrowser) {
                doOpenBrowser();
            }
        } catch (Exception ex) {
            log.error("Startup Error", ex);
        }
    }

    private void doOpenBrowser() throws IOException, URISyntaxException {
        // open default browser
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(new URI(getServerUrl()));
        } else if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + getServerUrl());
        } else {
            log.warn("Open browser, pointed to: {}", getServerUrl());
        }
    }

    private String getServerUrl() {
        return "http://localhost:" + serverPort;
    }
}