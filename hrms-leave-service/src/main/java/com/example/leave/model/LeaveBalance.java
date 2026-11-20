package com.example.leave.model;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(
    name = "leave_balances",
    schema = "leave_management",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"employee_id", "leave_type_id", "year"}
        )
    }
)
public class LeaveBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "employee_id", nullable = false)
    private UUID employeeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leave_type_id", nullable = false)
    private LeaveType leaveType;

    private Integer year;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal balance;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal consumed;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal reserved;

    @Version
    private Long version;

    public LeaveBalance() {
    }

    public void reserve(BigDecimal days) {

        if (days == null || days.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Days must be positive");
        }

        BigDecimal available = balance.subtract(consumed)
                                      .subtract(reserved);

        if (available.compareTo(days) < 0) {
            throw new IllegalArgumentException(
                "Insufficient leave balance"
            );
        }

        reserved = reserved.add(days);
    }

    public void confirmDeduction(BigDecimal days) {

        if (days == null || days.compareTo(BigDecimal.ZERO) <= 0
                || reserved.compareTo(days) < 0) {
            throw new IllegalArgumentException(
                "Invalid reservation amount"
            );
        }

        reserved = reserved.subtract(days);
        consumed = consumed.add(days);
    }

    public void releaseReservation(BigDecimal days) {

        if (days == null || days.compareTo(BigDecimal.ZERO) <= 0
                || reserved.compareTo(days) < 0) {
            throw new IllegalArgumentException(
                "Invalid reservation amount"
            );
        }

        reserved = reserved.subtract(days);
    }

    // Getters and Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(UUID employeeId) {
        this.employeeId = employeeId;
    }

    public LeaveType getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getConsumed() {
        return consumed;
    }

    public void setConsumed(BigDecimal consumed) {
        this.consumed = consumed;
    }

    public BigDecimal getReserved() {
        return reserved;
    }

    public void setReserved(BigDecimal reserved) {
        this.reserved = reserved;
    }

    public Long getVersion() {
        return version;
    }
}