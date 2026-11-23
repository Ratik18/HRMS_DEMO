package com.example.leave.model;



import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name = "leave_types", schema = "leave_management")
public class LeaveType {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal accrualRatePerMonth;

    private Integer maxCarryForward;

    @Column(nullable = false)
    private Boolean isPaid;

    public LeaveType() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAccrualRatePerMonth() {
        return accrualRatePerMonth;
    }

    public void setAccrualRatePerMonth(BigDecimal accrualRatePerMonth) {
        this.accrualRatePerMonth = accrualRatePerMonth;
    }

    public Integer getMaxCarryForward() {
        return maxCarryForward;
    }

    public void setMaxCarryForward(Integer maxCarryForward) {
        this.maxCarryForward = maxCarryForward;
    }

    public Boolean getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(Boolean isPaid) {
        this.isPaid = isPaid;
    }
}