package com.caterbao.lumos.locals.dal.mapper;

import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.pojo.BookBorrowReturnFlow;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookBorrowReturnFlowMapper {
    BookBorrowReturnFlow findOne(LumosSelective selective);
    List<BookBorrowReturnFlow> find(LumosSelective selective);
    long insert(BookBorrowReturnFlow bookBorrowReturnFlow);
    long update(BookBorrowReturnFlow bookBorrowReturnFlow);
}
