//package com.kingdee.blockchainquery.model;
//
//
//import com.kingdee.blockchainquery.util.FabricUtil;
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.Iterator;
//
///**
// * Created by Administrator on 2018/3/6.
// */
//public class TestTest {
//
//    private static Logger logger = LoggerFactory.getLogger(TestTest.class);
//
//    private static final  String  payload="[\n" +
//            "\n" +
//            "{\n" +
//            "\t\"namespace\": \"document\",\n" +
//            "\t\"reads\": [\n" +
//            "\t\n" +
//            "\t{\n" +
//            "\t\t\"version\": {\n" +
//            "\t\t\t\"txNum\": 0,\n" +
//            "\t\t\t\"blockNum\": 0\n" +
//            "\t\t},\n" +
//            "\t\t\"key\": \"dbae772db29058a88f9bd830e957c695347c41b6162a7eb9a9ea13def34be56b\"\n" +
//            "\t}\n" +
//            "\t\n" +
//            "\t\n" +
//            "\t],\n" +
//            "\t\"writes\": [\n" +
//            "\t\n" +
//            "\t\n" +
//            "\t{\n" +
//            "\t\t\"delete\": false,\n" +
//            "\t\t\"value\": {\n" +
//            "\t\t\t\"owner\": \"Test\",\n" +
//            "\t\t\t\"filename\": \"Jmeter-test-140\",\n" +
//            "\t\t\t\"opTime\": 1518518720,\n" +
//            "\t\t\t\"createTime\": 1518518720,\n" +
//            "\t\t\t\"docType\": \"FILE\",\n" +
//            "\t\t\t\"oldHash\": \"\",\n" +
//            "\t\t\t\"description\": \"Jmeter测试\",\n" +
//            "\t\t\t\"hashType\": \"SHA256\",\n" +
//            "\t\t\t\"version\": 0,\n" +
//            "\t\t\t\"hash\": \"dbae772db29058a88f9bd830e957c695347c41b6162a7eb9a9ea13def34be56b\",\n" +
//            "\t\t\t\"status\": \"LATEST\"\n" +
//            "\t\t},\n" +
//            "\t\t\"key\": \"dbae772db29058a88f9bd830e957c695347c41b6162a7eb9a9ea13def34be56b\"\n" +
//            "\t}\n" +
//            "\t\n" +
//            "\t]\n" +
//            "}\n" +
//            "\n" +
//            "\n" +
//            "\n" +
//            "\n" +
//            "]";
//
//    @Test
//    public void print(){
//
//        logger.info("haha:{}","haha");
//    }
//
//
//    @org.junit.Test
//    public void main() throws Exception {
//
//        //取出第一层
//        JSONArray jsonArray =JSONArray.fromObject(payload);
//        Iterator<JSONObject> json = jsonArray.iterator();
//        JSONArray jsonArray1 =new JSONArray();
//
//        while (json.hasNext()){
//            //对某个特定的数组元素进行解析
//            JSONObject jsonObject =json.next();
//
//            //解析namespace字段
//            jsonObject.put("链码调用",jsonObject.get("namespace"));
//            jsonObject.remove("namespace");
//
//            //解析reads字段
//            String reads =jsonObject.get("reads").toString();
//
//            JSONArray jsonArrayReads= FabricUtil.parseReads(reads);
//            jsonObject.put("读取",jsonArrayReads);
//            jsonObject.remove("reads");
//
//            //解析writes
//            jsonObject.put("写入",jsonObject.get("writes"));
//            jsonObject.remove("writes");
//
//            jsonArray1.add(jsonObject);
//        }
//    }
//}