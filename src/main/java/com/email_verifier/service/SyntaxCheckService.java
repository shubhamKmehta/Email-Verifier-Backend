package com.email_verifier.service;

import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class SyntaxCheckService {

    // Standard email regex
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$"
    );

    public boolean isValidSyntax(String email){
        if(email == null || email.isBlank())return false;
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }
}
