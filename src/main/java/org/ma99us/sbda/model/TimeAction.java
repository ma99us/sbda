package org.ma99us.sbda.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TimeAction {
    private Integer hours;
    private Integer minutes;
    private Integer seconds;
}
