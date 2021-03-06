package com.caterbao.lumos.locals.dal.mapper;

import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.pojo.BookFlow;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookFlowMapper {
    BookFlow findOne(LumosSelective selective);
    long checkDeviceHasException(String merchId,String storeId,String shopId, String deviceId);
    List<BookFlow> find(LumosSelective selective);
    long insert(BookFlow bookFlow);
    long update(BookFlow bookFlow);
}
