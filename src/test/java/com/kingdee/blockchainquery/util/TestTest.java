//package com.kingdee.blockchainquery.util;
//
//import com.kingdee.blockchainquery.service.ScheduleService;
//import com.kingdee.kchain.fabric.sdk.FabricClientFactory;
//import com.kingdee.kchain.fabric.sdk.chaincode.ChaincodeTemplate;
//import com.kingdee.kchain.fabric.sdk.chaincode.ChaincodeTemplateBuilder;
//import com.kingdee.kchain.fabric.sdk.channel.Block;
//import com.kingdee.kchain.fabric.sdk.channel.ChannelAccessor;
//import com.kingdee.kchain.fabric.sdk.channel.Envelope;
//import com.kingdee.kchain.fabric.sdk.properties.ClientProperties;
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//import org.junit.Test;
//import org.yaml.snakeyaml.Yaml;
//
//import java.io.FileInputStream;
//import java.io.InputStream;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//
//
///**
// * Created by Administrator on 2018/3/6.
// */
//public class TestTest {
//
//    private static FabricClientFactory factory;
//    private static ChannelAccessor channel;
//    private static ChaincodeTemplate chaincodeTemplate;
//
//
//    @Test
//    public static void main() throws Exception {
//        try {
////            FabricSDK.init("/opt/data/prop/configSDK.properties");
//            InputStream stream = new FileInputStream("src/configSDK.yaml");
//            ClientProperties properties = new Yaml().loadAs(stream, ClientProperties.class);
//            // 使用配置实例化 ClientFactory
//            factory = new FabricClientFactory(properties);
//            ChaincodeTemplateBuilder builder = factory.createChaincodeTemplateBuilder(); // (1)
//            chaincodeTemplate = builder
//                    .channel("mychannel")  // (2)
//                    .chaincodeId("marbles", "1.0", "/chaincode/src/") // (3)
//                    .build();
//            channel = factory.createChannelAccessor("mychannel");
//
//            System.out.println(JSONArray.fromObject(channel.peerStatus()));
//            Block block= channel.queryBlockByNumber(10);
//            System.out.println(block);
//            JSONObject jo= new JSONObject();
//            jo.put("channelId",block.getChannelId());
//            jo.put("blockNumber",block.getBlockNumber());
//            jo.put("dataHash",block.getDataHash());
//            jo.put("currentHash",block.getHash());
//            jo.put("previousHash",block.getPreviousHash());
//            jo.put("envelopeCount",block.getEnvelopeCount());
//            JSONArray re=new JSONArray();
//            List<Envelope> temp=block.getEnvelopes();
//            for(Envelope t:temp){
//                JSONObject tem=new JSONObject();
//                tem.put("transactionId",t.getId());
//                tem.put("epoch",t.getEpoch());
//                tem.put("type",t.getType());
//                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//                Date date = format.parse(t.getTimestamp());
//                tem.put("timeStampS",date.getTime());
//                re.add(tem);
//            }
//            jo.put("transactionInfos",re);
//            System.out.println(jo);
//
//            net.sf.json.JSONObject trans = jo;
//            trans.remove("transactionInfos");
//            net.sf.json.JSONObject en = net.sf.json.JSONObject.fromObject(channel.queryTransactionById("e6b1a7c88e052d87e1ce3c57d40cd9304f525c8004444dbfad7cc5be4090ceab"));
//            net.sf.json.JSONObject transactionInfo = new net.sf.json.JSONObject();
//            transactionInfo.put("transactionId", en.getString("id"));
//            transactionInfo.put("epoch", en.get("epoch"));
//            transactionInfo.put("type", en.get("type"));
//            transactionInfo.put("transactionInfo", net.sf.json.JSONArray.fromObject(en.get("transactionActions")));
//            trans.put("transactionInfo", transactionInfo);
//
//            System.out.println("trans"+trans);
//
//            ScheduleService.getChannel();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//}