package com.email_verifier.service;

import org.springframework.stereotype.Service;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.MXRecord;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

@Service
public class DnsCheckService {

    // ye domain ka MX Record check krega...
    public boolean hasMxRecord(String email){
        try{
            String domain = email.substring(email.indexOf('@')+1);
            System.out.println("Domain check kr rha hun " + domain);
            Record[] records = new Lookup(domain, Type.MX).run();

            System.out.println("📋 Records: " +
                    (records == null ? "NULL" : records.length + " found"));

            return  records != null && records.length > 0;
        } catch (Exception e) {
            System.out.println("❌ Exception: " + e.getMessage());
            e.printStackTrace(); // ← full error dikhega console mein
            return  false;
        }
    }

    // MX server ka hostname nikalo (SMTP ke liye)
    public String getMxHost(String email){
        try{
            String domain = email.substring(email.indexOf('@')+1);
            Record[] records = new Lookup(domain,Type.MX).run();

            if(records == null || records.length == 0){
                System.out.println("❌ MX host nahi mila for: " + domain);
                return null;
            }


            // sbse low priority wala MX server lo
            MXRecord mxRecord = (MXRecord) records[0];
            String host = mxRecord.getTarget().toString();
            System.out.println("✅ MX Host mila: " + host);
            return mxRecord.getTarget().toString();
        } catch (Exception e) {
            System.out.println("❌ getMxHost Exception: " + e.getMessage());
            return null;
        }
    }
}
