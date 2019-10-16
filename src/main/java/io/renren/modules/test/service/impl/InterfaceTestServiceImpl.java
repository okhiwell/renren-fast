package io.renren.modules.test.service.impl;

import io.renren.common.exception.RRException;
import io.renren.modules.test.dao.InterfaceTestDao;
import io.renren.modules.test.dao.ApiTestReportsDao;
import io.renren.modules.test.entity.InterfaceTestEntity;
import io.renren.modules.test.service.InterfaceTestService;
import io.renren.modules.test.utils.ApiTestUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("InterfaceTestService")
public class InterfaceTestServiceImpl implements InterfaceTestService {

    @Autowired
    private InterfaceTestDao interfaceTestDao;
    
    @Autowired
    private ApiTestReportsDao apiTestReportsDao;
    

    @Autowired
    private ApiTestUtils apiTestUtils;

    @Override
    public InterfaceTestEntity queryObject(Long caseId) {
        return interfaceTestDao.queryObject(caseId);
    }

    @Override
    public List<InterfaceTestEntity> queryList(Map<String, Object> map) {
        return interfaceTestDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return interfaceTestDao.queryTotal();
    }

    @Override
    public void save(InterfaceTestEntity apiCase) {
        interfaceTestDao.save(apiCase);
    }

    @Override
    public void update(InterfaceTestEntity apiCase) {
        interfaceTestDao.update(apiCase);
    }

    @Override
    @Transactional
    public void deleteBatch(Long[] caseIds) {
        for (Long caseId : caseIds) {
            // 先删除所属文件
            InterfaceTestEntity apiCase = queryObject(caseId);
            String casePath = apiTestUtils.getCasePath();
            String caseFilePath = casePath + File.separator + apiCase.getCaseDir();
            try {
                FileUtils.forceDelete(new File(caseFilePath));
            } catch (FileNotFoundException e) {
                //doNothing
            } catch (IOException e) {
                throw new RRException("删除文件异常失败", e);
            }
        }
        // 删除数据库内容
        // 脚本文件的删除调用file的自身方法，在controller中调用。因为file包含了分布式节点的数据。
        // 测试报告的内容都在master服务器端，所以直接删除case文件夹即可。
        apiTestReportsDao.deleteBatchByCaseIds(caseIds);
        interfaceTestDao.deleteBatch(caseIds);
    }

    @Override
    public int updateBatch(Long[] caseIds, int status) {
        Map<String, Object> map = new HashMap<>();
        map.put("list", caseIds);
        map.put("status", status);
        return interfaceTestDao.updateBatch(map);
    }

    @Override
    public List<InterfaceTestEntity> queryByApiType(String apiType) {
        return interfaceTestDao.queryByApiType(apiType);
    }

    @Override
    public int queryTotalByApiType(String apiType) {
        return interfaceTestDao.queryTotalByApiType(apiType);
    }

}