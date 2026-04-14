package com.gaisoft.repair.service.impl;

import com.gaisoft.common.utils.DateUtils;
import com.gaisoft.repair.domain.IrIssueRecode;
import com.gaisoft.repair.mapper.IrIssueRecodeMapper;
import com.gaisoft.repair.service.IIrIssueRecodeService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IrIssueRecodeServiceImpl
implements IIrIssueRecodeService {
    @Autowired
    private IrIssueRecodeMapper irIssueRecodeMapper;

    @Override
    public IrIssueRecode selectIrIssueRecodeByIssueId(Long issueId) {
        return this.irIssueRecodeMapper.selectIrIssueRecodeByIssueId(issueId);
    }

    @Override
    public List<IrIssueRecode> selectIrIssueRecodeList(IrIssueRecode irIssueRecode) {
        return this.irIssueRecodeMapper.selectIrIssueRecodeList(irIssueRecode);
    }

    @Override
    public int insertIrIssueRecode(IrIssueRecode irIssueRecode) {
        irIssueRecode.setCreateTime(DateUtils.getNowDate());
        return this.irIssueRecodeMapper.insertIrIssueRecode(irIssueRecode);
    }

    @Override
    public int updateIrIssueRecode(IrIssueRecode irIssueRecode) {
        irIssueRecode.setUpdateTime(DateUtils.getNowDate());
        return this.irIssueRecodeMapper.updateIrIssueRecode(irIssueRecode);
    }

    @Override
    public int deleteIrIssueRecodeByIssueIds(Long[] issueIds) {
        return this.irIssueRecodeMapper.deleteIrIssueRecodeByIssueIds(issueIds);
    }

    @Override
    public int deleteIrIssueRecodeByIssueId(Long issueId) {
        return this.irIssueRecodeMapper.deleteIrIssueRecodeByIssueId(issueId);
    }
}
