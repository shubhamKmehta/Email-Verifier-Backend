package com.email_verifier;

import org.xbill.DNS.*;
import org.xbill.DNS.Record;

public class DnsTest {
    public static void main(String[] args) throws Exception {

        String domain = "gmail.com";
        System.out.println("Testing DNS for: " + domain);

        Lookup lookup = new Lookup(domain, Type.MX);
        Record[] records = lookup.run();

        if (records == null) {
            System.out.println("❌ Records NULL — Result: "
                    + lookup.getResult());
            System.out.println("Error: " + lookup.getErrorString());
        } else {
            System.out.println("✅ MX Records found: " + records.length);
            for (Record r : records) {
                System.out.println("   → " + r);
            }
        }
    }
}
