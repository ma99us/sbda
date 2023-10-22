package org.ma99us.sbda.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PinAction {
    private Integer gpio;
    private Integer wPi;
    private Boolean isHigh;
    private String mode;
}
