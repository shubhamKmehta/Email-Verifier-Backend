package com.email_verifier.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.net.Socket;

@Service
public class SmtpCheckService {
    private static final int SMTP_PORT = 25;
    private static final int TIMEOUT_MS = 5000; // 5 seconds

    public boolean verifyEmail(String email,String mxHosts){
        // MX Host = Mail Server ka Address
        if(mxHosts == null) return false;

        // mxHost me trailing dot remove kro
        mxHosts = mxHosts.endsWith(".")?mxHosts.substring(0,mxHosts.length() -1) : mxHosts;
        System.out.println("📨 SMTP check: " + mxHosts + " for " + email);

        try (Socket socket = new Socket()){
            // SMTP server se connect kro
            socket.connect(new java.net.InetSocketAddress(mxHosts,SMTP_PORT),TIMEOUT_MS);
            socket.setSoTimeout(TIMEOUT_MS);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(
                    new OutputStreamWriter(socket.getOutputStream()),true
            );
            readResponse(bufferedReader);

            writer.println("HELO emailverifier.com");
            readResponse(bufferedReader);

            writer.println("MAIL FROM:<verify@emailverifier.com>");
            String mailFromResp = readResponse(bufferedReader);

            writer.println("RCPT TO:<" + email + ">");
            String rcptResp = readResponse(bufferedReader);

            writer.println("QUIT");



            return rcptResp.startsWith("250") || rcptResp.startsWith("251");

        } catch (Exception e) {
            // ✅ SMTP fail = UNKNOWN nahi = false
            System.out.println("⚠️ SMTP connect nahi hua: " + e.getMessage());
            return false;
        }

    }

    private String readResponse(BufferedReader bufferedReader) throws IOException {
        StringBuilder response = new StringBuilder();
        String line;
        while((line = bufferedReader.readLine()) != null){
            response.append(line);

            if(line.length() >= 4 && line.charAt(3) == ' ')break;
        }
        return response.toString();
    }
}
