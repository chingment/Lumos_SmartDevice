package com.caterbao.lumos.locals.dal.mapper;

import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.pojo.BookBorrowFlowData;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookBorrowFlowDataMapper {
    BookBorrowFlowData findOne(LumosSelective selective);

    List<BookBorrowFlowData> find(LumosSelective selective);

    long insert(BookBorrowFlowData bookBorrowFlowData);

    long update(BookBorrowFlowData bookBorrowFlowData);
}
