package com.gaisoft.repair.service;

import com.gaisoft.repair.domain.IrIssueRecode;
import java.util.List;

public interface IIrIssueRecodeService {
    public IrIssueRecode selectIrIssueRecodeByIssueId(Long var1);

    public List<IrIssueRecode> selectIrIssueRecodeList(IrIssueRecode var1);

    public int insertIrIssueRecode(IrIssueRecode var1);

    public int updateIrIssueRecode(IrIssueRecode var1);

    public int deleteIrIssueRecodeByIssueIds(Long[] var1);

    public int deleteIrIssueRecodeByIssueId(Long var1);
}
