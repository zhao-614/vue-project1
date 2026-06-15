package com.itheima;

import org.junit.jupiter.api.Test;

public class LombokTest {
    @Test
    public void main() {
        InterfaceLog log = new InterfaceLog();
        log.setUrl("http://test.com");
        log.setMethod("GET");
        System.out.println(log.getUrl());
        System.out.println(log.getMethod());
    }
}
