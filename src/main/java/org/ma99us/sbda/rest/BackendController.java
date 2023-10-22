package org.ma99us.sbda.rest;

import org.ma99us.sbda.model.PinAction;
import org.ma99us.sbda.service.OrangePiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin            // #TEST ONLY!
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
}
