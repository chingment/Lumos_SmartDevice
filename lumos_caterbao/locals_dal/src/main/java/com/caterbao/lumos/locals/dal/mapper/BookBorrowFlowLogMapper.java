package com.caterbao.lumos.locals.dal.mapper;

import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.pojo.BookBorrowFlowLog;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookBorrowFlowLogMapper {
    long insert(BookBorrowFlowLog bookBorrowFlowLog);
    List<BookBorrowFlowLog> find(LumosSelective selective);
}
