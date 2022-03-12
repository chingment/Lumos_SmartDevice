package com.caterbao.lumos.locals.dal.mapper;

import com.caterbao.lumos.locals.dal.pojo.BookBorrowFlowLog;
import org.springframework.stereotype.Repository;

@Repository
public interface BookBorrowFlowLogMapper {
    long insert(BookBorrowFlowLog bookBorrowFlowLog);
    long update(BookBorrowFlowLog bookBorrowFlowLog);
}
