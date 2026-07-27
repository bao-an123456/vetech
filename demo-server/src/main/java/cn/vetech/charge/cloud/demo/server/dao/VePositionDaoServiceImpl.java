package cn.vetech.charge.cloud.demo.server.dao;

import cn.vetech.charge.cloud.database.base.BaseServiceImpl;
import cn.vetech.charge.cloud.demo.server.entity.VePosition4849;
import cn.vetech.charge.cloud.demo.server.mapper.VePositionMapper;
import cn.vetech.charge.cloud.demo.server.service.dto.position.VePositionQryDTO;
import cn.vetech.charge.cloud.demo.server.service.vo.position.VePositionQryVO;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VePositionDaoServiceImpl extends BaseServiceImpl<VePositionMapper, VePosition4849> {

    public void insertPosition(VePosition4849 entity) {
        baseMapper.insert(entity);
    }

    public void insertPositionBatch(List<VePosition4849> list) {
        baseMapper.insertBatch(list);
    }

    public void deleteById(String id) {
        baseMapper.deleteById(id);
    }

    public void updateVePosition(VePosition4849 entity) {
        baseMapper.updateVePosition(entity);
    }

    public void updatePositionById(VePosition4849 entity) {
        baseMapper.updateById(entity);
    }

    public VePosition4849 selectById(String id) {
        return baseMapper.selectById(id);
    }

    public VePosition4849 selectByQybhAndYgid(String qybh, String ygid) {
        return baseMapper.selectByQybhAndYgid(qybh, ygid);
    }

    public List<VePositionQryVO> queryList(Page<VePositionQryVO> page, VePositionQryDTO dto) {
        return baseMapper.queryList(page, dto);
    }
}