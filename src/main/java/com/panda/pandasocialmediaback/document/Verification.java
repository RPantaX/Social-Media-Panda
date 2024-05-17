package com.panda.pandasocialmediaback.document;

import lombok.Data;

import java.time.LocalDateTime;

//será en bebida
@Data
public class Verification {
    private boolean status=false;
    private LocalDateTime startedAt;
    private LocalDateTime endsAt;
    private String planType;
}
