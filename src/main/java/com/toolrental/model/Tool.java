package com.toolrental.model;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tools")
public class Tool {

    @Id
    @Column(name = "tool_code")
    private String toolCode;

    @Column(name = "tool_type")
    private String toolType;

    private String brand;

    @Column(name = "daily_charge")
    private double dailyCharge;

    @Column(name = "weekday_charge")
    private boolean weekdayCharge;

    @Column(name = "weekend_charge")
    private boolean weekendCharge;

    @Column(name = "holiday_charge")
    private boolean holidayCharge;

}