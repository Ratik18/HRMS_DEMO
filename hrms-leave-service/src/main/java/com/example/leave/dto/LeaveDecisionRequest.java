package com.example.leave.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LeaveDecisionRequest(

        @NotBlank(message = "Decision comment is required")
        @Size(max = 500, message = "Comment cannot exceed 500 characters")
        String comment

) {
}

