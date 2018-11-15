package com.kingdee.blockchainquery.util;

import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.Test;

import java.net.URLDecoder;

/**
 * Created by Administrator on 2018/3/6.
 */
public class UtilTest {

    @Test
    public void main() throws Exception {
        String test = "\n" +
                "{\\\"id\\\":\\\"o151511639459648579\\\",\\\"username\\\":\\\"\\346\\235\\216\\345\\260\\217\\345\\247\\220\\\",\\\"company\\\":\\\"kdCloud\\\"}";
        JSONObject t = JSONObject.fromObject(StringEscapeUtils.unescapeJava(test));

        System.out.println(t);
        System.out.println(new String(StringEscapeUtils.unescapeJava("\351\207\221\350\235\266\347\240\224\347\251\266\351\231\242").getBytes(), "utf-8"));
        System.out.println(new String(URLDecoder.decode(StringEscapeUtils.unescapeJava("351"), "utf-8").getBytes(), "utf-8"));
        System.out.println(Integer.valueOf("\151"));
    }

}