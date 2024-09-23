package com.fiap.hackathon.core.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TimeSlotsEnum {

    SLOT_7H_8H("07:00-08:00"),
    SLOT_8H_9H("08:00-09:00"),
    SLOT_9H_10H("09:00-10:00"),
    SLOT_10H_11H("10:00-11:00"),
    SLOT_11H_12H("11:00-12:00"),
    SLOT_12H_13H("12:00-13:00"),
    SLOT_13H_14H("13:00-14:00"),
    SLOT_14H_15H("14:00-15:00"),
    SLOT_15H_16H("15:00-16:00"),
    SLOT_16H_17H("16:00-17:00"),
    SLOT_17H_18H("17:00-18:00"),
    SLOT_18H_19H("18:00-19:00"),
    UNAVAILABLE("--:--");

    private String slot;

    public static Boolean isValid(String receivedTimeSlot) {
        for (var value : TimeSlotsEnum.values()) {
            if (value.getSlot().equals(receivedTimeSlot)) {
                return true;
            }
        }

        return false;
    }

}
