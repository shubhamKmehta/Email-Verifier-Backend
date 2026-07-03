package com.email_verifier.service;

import com.email_verifier.model.EmailVerificationResult;
import org.springframework.stereotype.Service;

@Service
public class EmailVerifierService {

    private final SyntaxCheckService syntaxCheckService;
    private final DnsCheckService dnsCheckService;
    private final SmtpCheckService smtpCheckService;

    public EmailVerifierService(SyntaxCheckService syntaxCheckService, DnsCheckService dnsCheckService, SmtpCheckService smtpCheckService) {
        this.syntaxCheckService = syntaxCheckService;
        this.dnsCheckService = dnsCheckService;
        this.smtpCheckService = smtpCheckService;
    }

    public EmailVerificationResult verify(String email){
        // step-1 - Syntax check
        boolean validSyntax = syntaxCheckService.isValidSyntax(email);

        if(!validSyntax){
            return EmailVerificationResult.builder()
                    .email(email)
                    .syntaxValid(false)
                    .domainExists(false)
                    .mailboxExists(false)
                    .isDeliverable(false)
                    .status("INVALID")
                    .message("Wrong email format!")
                    .build();
        }

        // step -2 - DNS / MX Check
        boolean hasMX = dnsCheckService.hasMxRecord(email);
        if(!hasMX){
            return EmailVerificationResult.builder()
                    .email(email)
                    .syntaxValid(true)
                    .domainExists(false)
                    .mailboxExists(false)
                    .isDeliverable(false)
                    .status("INVALID")
                    .message("The domain's mail server doesn't exist. !")
                    .build();
        }


        // step 3 - SMTP Check

        String mxHost = dnsCheckService.getMxHost(email);
        boolean hasMailBox = smtpCheckService.verifyEmail(email,mxHost);

        return EmailVerificationResult.builder()
                .email(email)
                .syntaxValid(true)
                .domainExists(true)
                .mailboxExists(hasMailBox)
                .isDeliverable(hasMailBox)
                .status(hasMailBox ? "ACTIVE" : "UNKNOWN")
                .message(hasMailBox
                        ? "Email active aur deliverable haie"
                        : "⚠ Domain valid hai par mailbox verify nahi ho saka (Gmail/Yahoo SMTP block karte hain)")
                .build();

    }


}
