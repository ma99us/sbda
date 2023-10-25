package org.ma99us.sbda;

import lombok.extern.slf4j.Slf4j;
import org.ma99us.sbda.service.OrangePiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.awt.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;

@SpringBootApplication
@Slf4j
public class App {

    @Value("${server.port}")
    private String serverPort;
    private String serverName;
    private String serverAddress;

    @Value("${open-browser:false}")
    private boolean openBrowser;

    public static void main(String[] args) {
        OrangePiService service = SpringApplication.run(App.class, args).getBean(OrangePiService.class);
        service.init();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onStartup() {
        try {
            serverName = InetAddress.getLocalHost().getHostName();
            serverAddress = InetAddress.getLocalHost().getHostAddress();
            log.info("Running on \"{}\" ({}), port: {} ", serverName, serverAddress, serverPort);

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
            Desktop.getDesktop().browse(new URI(getLocalUrl()));
        } else if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + getLocalUrl());
        } else {
            log.warn("Open browser, pointed to: {} or {}", getLocalUrl(), getServerUrl());
        }
    }

    private String getLocalUrl() {
        return "http://localhost:" + serverPort;
    }

    private String getServerUrl() {
        return "http://" + serverAddress + ":" + serverPort;
    }

}