package io.renren.modules.test.service.impl;

import io.renren.modules.test.dao.FireworkInformationDao;
import io.renren.modules.test.entity.FireworkInformationEntity;
import io.renren.modules.test.service.FireworkInformationService;
import io.renren.modules.test.utils.StressTestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("fireworkInformationService")
public class FireworkInformationServiceImpl implements FireworkInformationService {


    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FireworkInformationDao fireworkInformationDao;

    @Autowired
    private StressTestUtils stressTestUtils;
    
    @Override
    public FireworkInformationEntity queryObject(Long nodeIp) {
        return fireworkInformationDao.queryObject(nodeIp);
    }


    @Override
    public FireworkInformationEntity queryObjectFromAllIP(String ipall) {
        return fireworkInformationDao.queryObjectFromAllIP(ipall);
    }

    @Override
    public List<FireworkInformationEntity> queryList(Map<String, Object> map) {
        return fireworkInformationDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return fireworkInformationDao.queryTotal(map);
    }

    @Override
    public void save(FireworkInformationEntity node) {
        fireworkInformationDao.save(node);

    }

    @Override
    public void update(FireworkInformationEntity node) {
        fireworkInformationDao.update(node);
    }

    @Override
    public void deleteBatch(String[] nodeIPs) {
        fireworkInformationDao.deleteBatch(nodeIPs);
    }
}