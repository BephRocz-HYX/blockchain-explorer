package com.kingdee.blockchainquery.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class FabricUtil {
    private static Logger logger = LoggerFactory.getLogger(FabricUtil.class);

    public List<JSONObject> getPayloadMessage(String message) {
        List<JSONObject> lsData = new ArrayList();
        Pattern p = Pattern.compile("\\{[^\\{\\}]+\\}");
        Matcher m = p.matcher(message);
        while (m.find()) {

            try {
                lsData.add(JSONObject.fromObject(StringEscapeUtils.unescapeJava(m.group())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return lsData;
    }

    public static <T> String objectToJson(T obj) throws JSONException, IOException {

        ObjectMapper mapper = new ObjectMapper();

        String jsonStr = "";
        try {
            jsonStr = new String(mapper.writeValueAsBytes(obj), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw e;
        }
        return JSONObject.fromObject(obj).toString();
    }

}
