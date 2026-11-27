package com.example.attendance.model;

	import jakarta.persistence.*;
	import java.time.LocalDate;
	import java.time.LocalDateTime;

	@Entity
	@Table(
	    name = "attendance_records",
	    uniqueConstraints = {
	        @UniqueConstraint(
	            columnNames = {"employee_id", "attendance_date"}
	        )
	    }
	)
	public class AttendanceRecord {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(name = "employee_id", nullable = false)
	    private Long employeeId;

	    @Column(name = "attendance_date", nullable = false)
	    private LocalDate attendanceDate;

	    private LocalDateTime checkIn;

	    private LocalDateTime checkOut;

	    @Enumerated(EnumType.STRING)
	    @Column(nullable = false)
	    private AttendanceStatus status;

	    private String remarks;

	    private LocalDateTime createdAt;

	    private LocalDateTime updatedAt;

	    @PrePersist
	    public void onCreate() {
	        createdAt = LocalDateTime.now();
	        updatedAt = LocalDateTime.now();
	    }

	    @PreUpdate
	    public void onUpdate() {
	        updatedAt = LocalDateTime.now();
	    }

	    public Long getId() {
	        return id;
	    }

	    public Long getEmployeeId() {
	        return employeeId;
	    }

	    public void setEmployeeId(Long employeeId) {
	        this.employeeId = employeeId;
	    }

	    public LocalDate getAttendanceDate() {
	        return attendanceDate;
	    }

	    public void setAttendanceDate(LocalDate attendanceDate) {
	        this.attendanceDate = attendanceDate;
	    }

	    public LocalDateTime getCheckIn() {
	        return checkIn;
	    }

	    public void setCheckIn(LocalDateTime checkIn) {
	        this.checkIn = checkIn;
	    }

	    public LocalDateTime getCheckOut() {
	        return checkOut;
	    }

	    public void setCheckOut(LocalDateTime checkOut) {
	        this.checkOut = checkOut;
	    }

	    public AttendanceStatus getStatus() {
	        return status;
	    }

	    public void setStatus(AttendanceStatus status) {
	        this.status = status;
	    }

	    public String getRemarks() {
	        return remarks;
	    }

	    public void setRemarks(String remarks) {
	        this.remarks = remarks;
	    }
	}


