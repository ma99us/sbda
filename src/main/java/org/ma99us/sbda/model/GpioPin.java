package org.ma99us.sbda.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GpioPin {
    private int num;
    private Integer gpio;
    private Integer wPi;
    private String name;
    private String mode;
    private Integer v;
}
