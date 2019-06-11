package com.mgr.server.entity;


import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Memory implements Serializable {
    private String name;
    private String response;
    private Double percent;
    private String body;
    private String method;
    private Boolean ready = false;

    public synchronized void setPercent( Double _percent){
        this.percent=_percent;
    }
    public synchronized Double getPercent(){
        return this.percent;
    }
}
