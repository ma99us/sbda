package org.ma99us.sbda.rest;

import lombok.extern.slf4j.Slf4j;
import org.ma99us.sbda.model.PinAction;
import org.ma99us.sbda.model.TimeAction;
import org.ma99us.sbda.service.OrangePiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin            // TODO: Remove for production!
@Slf4j
public class BackendController {

    final long R_PRESS_MS = 100L;
    final long R_DBL_PRESS_MS = 100L;
    final long R_STOP_MS = 1400L;

    @Autowired
    private OrangePiService opiService;

    @RequestMapping("/status")
    public String getStatusText() {
        return opiService.getStatusText();
    }

    @RequestMapping("/gpio-all")
    public String gpioStatus() {
        return opiService.gpioStatusText();
    }

    @PostMapping("/pin-mode")
    public void onPinMode(@RequestBody PinAction dto) {
        opiService.gpioPinMode(dto.getWPi(), dto.getMode());
    }

    @PostMapping("/pin-write")
    public void onPinWrite(@RequestBody PinAction dto) {
        opiService.gpioPinWrite(dto.getWPi(), dto.getIsHigh());
    }

    @PostMapping("/pin-read")
    public String onPinRead(@RequestBody PinAction dto) {
        return opiService.gpioPinRead(dto.getWPi());
    }

    @PostMapping("/pin-blink")
    public void onPinBlink(@RequestBody PinAction dto) {
        long sleep = dto.getDurationMs() != null ? dto.getDurationMs() : R_PRESS_MS;
        log.debug("onPinBlink; sleep={}", sleep);
        dto.setIsHigh(true);
        onPinWrite(dto);
        try {
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
            // no-op
        }
        dto.setIsHigh(false);
        onPinWrite(dto);
    }

    @PostMapping("/pin-step")
    public void onPinStep(@RequestBody PinAction dto) {
        long duration = dto.getDurationMs() != null ? dto.getDurationMs() : 1000;
        log.debug("onPinStep; duration={}, forward={}", duration, dto.getForward());
        if (dto.getForward()) {
            dto.setDurationMs(R_PRESS_MS);
            onPinBlink(dto);
            try {
                Thread.sleep(R_DBL_PRESS_MS);
            } catch (InterruptedException e) {
                // no-op
            }
        }
        dto.setDurationMs(R_PRESS_MS);
        onPinBlink(dto);
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            // no-op
        }
        // now stop
        dto.setDurationMs(R_STOP_MS);
        onPinBlink(dto);
    }

    @PostMapping("/start-camera-stream")
    public void onStartCameraStream() {
        opiService.startMjpgStreamer();
    }

    @PostMapping("/stop-camera-stream")
    public void onStopCameraStream() {
        opiService.stopMjpgStreamer();
    }

    @PostMapping("/go-to-sleep")
    public void onGoToSleep(@RequestBody TimeAction dto) {
        opiService.goToSleep(dto.getMinutes());
    }
}
