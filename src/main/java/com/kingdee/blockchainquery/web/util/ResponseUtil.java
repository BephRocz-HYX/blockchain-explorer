package com.kingdee.blockchainquery.web.util;

import net.sf.json.JSONObject;

/**
 * Created by Administrator on 2018/3/19.
 */
public class ResponseUtil {


    public static JSONObject jsonResponse(JSONObject json, int errCode, Object object) {

        switch (errCode) {

            case 0:
                json.put("errcode", errCode);
                json.put("description", "操作执行成功！");
                json.put("data", object);
                break;
            case 1001:
                json.put("errcode", errCode);
                json.put("description", "操作执行失败！");
                json.put("data", object);
                break;
        }
        return json;
    }
}
