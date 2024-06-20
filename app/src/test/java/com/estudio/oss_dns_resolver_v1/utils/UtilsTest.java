package com.estudio.oss_dns_resolver_v1.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class UtilsTest {

    @Test
    public void getURLScheme() {
        String host = "http://google.com:2782/api/v1/login";
        String scheme = Utils.getURLScheme(host);

        assertEquals("http", scheme);

    }

    @Test
    public void getURLPort() {
        String host = "https://google.com:8463/api/v1/login";
        int port = Utils.getURLPort(host);

        assertEquals(8463, port);
    }

    @Test
    public void getURLHost() {
        String host = "https://170.33.13.254:7360/09/error";
        String hostName = Utils.getURLHost(host);

        assertEquals("170.33.13.254", hostName);
    }

    @Test
    public void replaceURLHost() {
        String host = "http://google.com/api/v1/login";
        String newHost = Utils.replaceURLHost(host, "facebook.com");

        assertEquals("http://facebook.com/api/v1/login", newHost);
    }



}