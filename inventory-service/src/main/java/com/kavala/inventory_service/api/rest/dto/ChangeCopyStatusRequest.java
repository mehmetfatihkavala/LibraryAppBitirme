package com.kavala.inventory_service.api.rest.dto;

import com.kavala.inventory_service.domain.model.CopyStatus;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for changing book copy status.
 */
public record ChangeCopyStatusRequest(
        @NotNull(message = "New status is required") CopyStatus newStatus,

        String reason // Optional: Reason for status change (damage description, withdrawal reason,
                      // etc.)
) {
}
