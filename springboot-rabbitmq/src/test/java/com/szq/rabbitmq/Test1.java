package com.szq.rabbitmq;

import com.szq.rabbitmq.test.jsd;
import org.junit.jupiter.api.Test;

public class Test1 {
    @Test
    void testjsd() {
        jsd jsd = new jsd();
        System.out.println(jsd.isPalindrome(-121));
    }
}
