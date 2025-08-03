package com.url.backend.util;


import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CountryResponse;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.InetAddress;


@Component
public class GeoIpUtil {

    private static DatabaseReader dbReader;

    static {
        try {
            File database = new File("src/main/resources/geoIp/GeoLite2-Country.mmdb");
            dbReader = new DatabaseReader.Builder(database).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getCountryFromIp(String ip) {
        try {
            InetAddress ipAddress = InetAddress.getByName(ip);
            CountryResponse response = dbReader.country(ipAddress);
            return response.getCountry().getName(); // e.g., "United States"
        } catch (Exception e) {
            return "Unknown";
        }
    }
}