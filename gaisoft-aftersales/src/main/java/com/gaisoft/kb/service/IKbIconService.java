package com.gaisoft.kb.service;

import com.gaisoft.kb.domain.KbIcon;
import java.util.List;

public interface IKbIconService {
    public KbIcon selectKbIconById(Long var1);

    public List<KbIcon> selectKbIconList(KbIcon var1);

    public int insertKbIcon(KbIcon var1);

    public int updateKbIcon(KbIcon var1);

    public int deleteKbIconByIds(Long[] var1);

    public int deleteKbIconById(Long var1);
}
