package com.caterbao.lumos.locals.dal.mapper;

import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.pojo.BookBorrowReturnFlowData;
import org.springframework.stereotype.Repository;

@Repository
public interface BookBorrowReturnFlowDataMapper {
    BookBorrowReturnFlowData findOne(LumosSelective selective);
    long insert(BookBorrowReturnFlowData bookBorrowReturnFlowData);
    long update(BookBorrowReturnFlowData bookBorrowReturnFlowData);
}
