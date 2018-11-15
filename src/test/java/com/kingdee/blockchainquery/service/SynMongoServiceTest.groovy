package com.kingdee.blockchainquery.service

import org.junit.Test

import java.text.SimpleDateFormat

/**
 * Created by Administrator on 2018/3/5.
 */
class SynMongoServiceTest  {

    @Test
    void testGetNumber() {

        String date="2018-03-02T11:12:15Z";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        System.out.print(format.parse(date).time);
    }
}
