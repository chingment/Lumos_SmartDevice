package com.caterbao.lumos.locals.dal.mapper;

import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.pojo.BookBorrowFlow;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookBorrowFlowMapper {
    BookBorrowFlow findOne(LumosSelective selective);
    List<BookBorrowFlow> find(LumosSelective selective);
    long insert(BookBorrowFlow bookBorrowFlow);
    long update(BookBorrowFlow bookBorrowFlow);
}
