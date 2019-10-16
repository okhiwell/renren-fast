package io.renren.modules.test.service.impl;

import io.renren.common.exception.RRException;
import io.renren.modules.test.dao.MockApiInfoFullDao;
import io.renren.modules.test.entity.MockApiInfoFullEntity;
import io.renren.modules.test.service.MockApiInfoFullService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;


@Service("MockApiInfoFullService")
public class MockApiInfoFullServiceImpl implements MockApiInfoFullService {

    @Autowired
    private MockApiInfoFullDao mockApiInfoFullDao;


    @Override
    public MockApiInfoFullEntity queryObject(String url) {
        return mockApiInfoFullDao.queryObject(url);
    }

    @Override
    public List<MockApiInfoFullEntity> queryList(Map<String, Object> map) {
        return mockApiInfoFullDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return mockApiInfoFullDao.queryTotal();
    }

    @Override
    public void save(MockApiInfoFullEntity apiMockEntity) {
        mockApiInfoFullDao.save(apiMockEntity);
    }

    @Override
    public void update(MockApiInfoFullEntity apiMockEntity) {
        mockApiInfoFullDao.update(apiMockEntity);
    }

    @Override
    public void insertOrUpdate(MockApiInfoFullEntity apiMockEntity) {
        mockApiInfoFullDao.insertOrUpdate(apiMockEntity);
    }

    @Override
    public void deleteBatch(String[] urls) {
        mockApiInfoFullDao.deleteBatch(urls);
    }

    @Override
    public void updateBatch(List<MockApiInfoFullEntity> apiMockEntity) {
        mockApiInfoFullDao.updateBatch(apiMockEntity);
    }

    @Override
    public List<MockApiInfoFullEntity> queryByMethod(String method) {
        return mockApiInfoFullDao.queryByMethod(method);
    }

    @Override
    public int queryTotalByMethod(String method) {
        return mockApiInfoFullDao.queryTotalByMethod(method);
    }

    @Override
    public List<MockApiInfoFullEntity> queryByUser(String user) {
        return mockApiInfoFullDao.queryByUser(user);
    }

    @Override
    public List<MockApiInfoFullEntity> queryAll() {
        return mockApiInfoFullDao.queryAll();
    }
}