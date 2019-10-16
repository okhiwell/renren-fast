package io.renren.modules.test.service.impl;


import io.renren.modules.test.dao.NodeDao;
import io.renren.modules.test.entity.NodeEntity;
import io.renren.modules.test.service.NodeService;
import io.renren.modules.test.utils.StressTestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;

@Service("nodeService")
public class NodeServiceImpl implements NodeService {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private NodeDao nodeDao;

    @Autowired
    private StressTestUtils stressTestUtils;


    @Override
    public NodeEntity queryObject(String nodeIp) {
        return nodeDao.queryObject(nodeIp);
    }

    @Override
    public NodeEntity queryObjectFromVIP(String vip) {
        return nodeDao.queryObjectFromVIP(vip);
    }


    @Override
    public List<NodeEntity> queryList(Map<String, Object> map) {
        return nodeDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return nodeDao.queryTotal(map);
    }

    @Override
    public void save(NodeEntity node) {
        nodeDao.save(node);
    }

    @Override
    public void update(NodeEntity node) {
        nodeDao.update(node);
    }

    @Override
    public void insertOrUpdate(NodeEntity node) {
        nodeDao.insertOrUpdate(node);
    }

    @Override
    public void deleteBatch(String[] nodeIPs) {
        nodeDao.deleteBatch(nodeIPs);
    }

}
