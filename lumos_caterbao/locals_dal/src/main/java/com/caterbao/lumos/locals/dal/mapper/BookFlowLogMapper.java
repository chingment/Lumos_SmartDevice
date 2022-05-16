package com.caterbao.lumos.locals.dal.mapper;

import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.pojo.BookFlowLog;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookFlowLogMapper {
    long insert(BookFlowLog bookFlowLog);
    long update(BookFlowLog bookFlowLog);
    BookFlowLog findLastByFlowId(String flowId);
    BookFlowLog findOne(LumosSelective selective);
    List<BookFlowLog> find(LumosSelective selective);
}
