package com.fuochuy.spring_boot.springboot.helper;

import lombok.extern.slf4j.Slf4j;

import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

@Slf4j
public class IpAddressHelper {

    private IpAddressHelper() {
    }

    public static String getIpAddress() {
        var ip = "127.0.0.1";

        Enumeration<NetworkInterface> networkInterfaces;
        List<String> ipAddresses = new ArrayList<>();
        try {
            networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    var address = addresses.nextElement();
                    if (address instanceof Inet4Address && address.isSiteLocalAddress()) {
                        ipAddresses.add(address.getHostAddress());
                    }
                }
            }
            if (ipAddresses.isEmpty()) {
                return ip;
            }
            Collections.sort(ipAddresses);
            return ipAddresses.get(0);
        } catch (SocketException e) {
            log.error("Get Ip is failed", e);
            return ip;
        }
    }

    public static String getApp() {
        String ipAddress = getIpAddress();
        InetAddress ip;
        String hostname = "local";
        try {
            ip = InetAddress.getLocalHost();
            hostname = ip.getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return String.format("%s:%s", hostname, ipAddress);
    }
}
