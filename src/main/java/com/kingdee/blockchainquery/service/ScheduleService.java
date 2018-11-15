package com.kingdee.blockchainquery.service;

import com.kingdee.blockchainquery.dao.FabricDao;
import com.kingdee.blockchainquery.dao.SynFabricDao;
import com.kingdee.blockchainquery.socket.FabricWebSocket;
import com.kingdee.kchain.fabric.sdk.channel.ChannelAccessor;
import com.kingdee.kchain.fabric.sdk.channel.PeerStatus;
import lombok.AllArgsConstructor;
import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ScheduleService {

    private static final Logger log = LoggerFactory.getLogger(ScheduleService.class);

    private List<PeerStatus> peerData = new ArrayList();
    private final ChannelAccessor channel;
    private final FabricDao mongoService;
    private final FabricWebSocket fabricWebSocket;
    private final SynFabricDao synFabricDao;

    @Scheduled(cron = "0/5 * * * * ?")
    public void blockService() {
        boolean flag = false;
        boolean isPeerEquals = true;
        List<PeerStatus> result = new ArrayList();
        try {
            if (channel == null) {
                log.info("");
            }
            result = channel.peerStatus();
        } catch (Exception e) {
            log.error("获取节点状态错误：{}", e);
        }
        if (!result.isEmpty()) {
            try {
                isPeerEquals = result.hashCode() == this.peerData.hashCode();
                if (!isPeerEquals) {
                    this.peerData = result;
                    mongoService.setJsonArrayData("peerinfo", JSONArray.fromObject(result));
                    log.info("{}", "向前端推送节点状态更新请求");
                    this.fabricWebSocket.send("peerUpdate");
                }
            } catch (Exception e) {
                log.error("保存节点信息失败：{}", e);
            }
            try {
                flag = synFabricDao.saveBlockToMongo();
                log.info("同步保存数据到mongo状态：{}", Boolean.valueOf(flag));
                log.info("保存区块交易信息成功");
            } catch (Exception e) {
                log.error("保存信息", e);
            }
            try {
                if (flag) {
                    log.info("{}", "向前端推送链码更新请求");
                    this.fabricWebSocket.send("chainUpdate");
                    log.info("{}", "myWebSocket执行结束");
                }
            } catch (Exception e) {
                log.error("向前端推送消息失败", e);
            }
        }
    }
}
