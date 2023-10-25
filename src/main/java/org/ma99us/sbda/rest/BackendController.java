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
        long dur = dto.getDurationMs() != null ? dto.getDurationMs() : 500;
        opiService.gpioPinWrite(dto.getWPi(), true);
        try {
            Thread.sleep(dur);
        } catch (InterruptedException e) {
            // no-op
        }
        opiService.gpioPinWrite(dto.getWPi(), false);
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
