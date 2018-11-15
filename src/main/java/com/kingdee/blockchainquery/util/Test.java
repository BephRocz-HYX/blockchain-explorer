package com.kingdee.blockchainquery.util;

import com.kingdee.blockchainquery.service.MongoService;
import com.kingdee.blockchainquery.service.ScheduleService;
import com.kingdee.kchain.fabric.sdk.FabricClientFactory;
import com.kingdee.kchain.fabric.sdk.chaincode.ChaincodeManager;
import com.kingdee.kchain.fabric.sdk.chaincode.ChaincodeTemplate;
import com.kingdee.kchain.fabric.sdk.chaincode.ChaincodeTemplateBuilder;
import com.kingdee.kchain.fabric.sdk.chaincode.policy.EndorsementPolicy;
import com.kingdee.kchain.fabric.sdk.chaincode.policy.Identity;
import com.kingdee.kchain.fabric.sdk.chaincode.policy.NOf;
import com.kingdee.kchain.fabric.sdk.channel.Block;
import com.kingdee.kchain.fabric.sdk.channel.ChannelAccessor;
import com.kingdee.kchain.fabric.sdk.channel.Envelope;
import com.kingdee.kchain.fabric.sdk.channel.PeerStatus;
import com.kingdee.kchain.fabric.sdk.properties.ClientProperties;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.hyperledger.fabric.sdk.BlockchainInfo;
import org.hyperledger.fabric.sdk.ChaincodeEndorsementPolicy;
import org.hyperledger.fabric.sdk.Peer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Test {
    private static FabricClientFactory factory;
    private static ChannelAccessor channel;
    private static ChaincodeTemplate chaincodeTemplate;

    public static void main(String[] args) throws Exception {

//            FabricSDK.init("/opt/data/prop/configSDK.properties");
        InputStream stream = new FileInputStream("src/configSDK.yaml");
        ClientProperties properties = new Yaml().loadAs(stream, ClientProperties.class);
        // 使用配置实例化 ClientFactory
        factory = new FabricClientFactory(properties);
        ChaincodeTemplateBuilder builder = factory.createChaincodeTemplateBuilder(); // (1)
//        chaincodeTemplate = builder
//                .channel("mychannel")  // (2)
//                .chaincodeId("marbles", "1.0", "/chaincode/src/") // (3)
//                .build();
        channel = factory.createChannelAccessor("mychannel");

        System.out.println(JSONArray.fromObject(channel.peerStatus()));

        Block block = channel.queryBlockByNumber(10);
        ChaincodeManager manager = factory.createChaincodeManager("mychannel");
//        manager.prepareInstall("marbles", "chaincode/src", "2.1") // (1)
//                .install(new File("E:\\娱乐\\专利"));

//        Identity user1 = new Identity("user1", Role.newMember("Org1MSP")); // (1)
//        Identity user2 = new Identity("user2", Role.newMember("Org2MSP"));
//        Identity admin1 = new Identity("admin1", Role.newAdmin("Org1MSP"));
//        Identity admin2 = new Identity("admin2", Role.newAdmin("Org2MSP"));
//
//        List<Identity> identities = Arrays.asList(user1, user2, admin1, admin2); // (2)
//
//        NOf oneOfOrg1 = new NOf(1, Arrays.asList(user1, admin1), null); // (3)
//        NOf oneOfOrg2 = new NOf(1, Arrays.asList(user2, admin2), null);
//
//        NOf policy = new NOf(1, null, Arrays.asList(oneOfOrg1, oneOfOrg2)); // (4)
//
//        EndorsementPolicy endorsementPolicy = new EndorsementPolicy(identities, policy); // (5)
//
//        ChaincodeEndorsementPolicy chaincodeEndorsementPolicy = endorsementPolicy.toPolicy(); // (6)
        manager.prepareInstantiate("marbles", "2.1") // (1)

                .instantiate(); // (7)



    }
}
