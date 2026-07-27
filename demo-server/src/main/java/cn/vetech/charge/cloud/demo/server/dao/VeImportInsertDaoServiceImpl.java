package cn.vetech.charge.cloud.demo.server.dao;

import cn.vetech.charge.cloud.demo.server.entity.VeImport4849;
import cn.vetech.charge.cloud.demo.server.mapper.VeImportMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VeImportInsertDaoServiceImpl {

    @Autowired
    private VeImportMapper veImportMapper;

    public void save(VeImport4849 entity) {
        veImportMapper.insert(entity);
    }

    public void update(VeImport4849 entity) {
        veImportMapper.updateVeImportExport(entity);
    }

    public VeImport4849 getById(String id) {
        return veImportMapper.selectByTaskId(id);
    }

    public List<VeImport4849> queryList(String qybh, String taskType) {
        return veImportMapper.queryList(qybh, taskType);
    }
}