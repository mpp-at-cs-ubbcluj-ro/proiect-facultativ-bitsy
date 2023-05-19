package com.helpinghands.rest_services.data;

import com.helpinghands.domain.Eveniment;

public class EventParams extends UserActionParams {
    private Eveniment eveniment;

    public Eveniment getEveniment() {
        return eveniment;
    }

    public void setEveniment(Eveniment eveniment) {
        this.eveniment = eveniment;
    }
}
