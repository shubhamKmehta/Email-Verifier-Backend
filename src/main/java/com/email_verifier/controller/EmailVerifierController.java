package com.email_verifier.controller;

import com.email_verifier.model.EmailRequest;
import com.email_verifier.model.EmailVerificationResult;
import com.email_verifier.service.EmailVerifierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // fronted ke liye
public class EmailVerifierController {
    private final EmailVerifierService emailVerifierService;

    @PostMapping("/verify")
    public ResponseEntity<EmailVerificationResult> verify(@RequestBody EmailRequest request){
        EmailVerificationResult result = emailVerifierService.verify(request.getEmail());
        return ResponseEntity.ok(result);
    }

    // Single email GET se bhi check kro
    @GetMapping("/verify")
    public ResponseEntity<EmailVerificationResult> verifyGet(@RequestParam String email){
        EmailVerificationResult result = emailVerifierService.verify(email);
        return ResponseEntity.ok(result);
    }
}
