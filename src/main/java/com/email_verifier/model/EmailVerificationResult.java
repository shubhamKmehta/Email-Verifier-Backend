package com.email_verifier.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailVerificationResult {

    private String email;
    private boolean syntaxValid;
    private boolean domainExists; // MX Record h ?
    private boolean mailboxExists; // SMTP se confirm hua;
    private boolean isDeliverable; // overall active h ?
    private String status; // "ACTIVE" / "INVALID" / "UNKNOWN"
    private String message; // user ko dikhane ke liye..

}
