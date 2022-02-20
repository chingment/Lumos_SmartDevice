package com.caterbao.lumos.locals.dal.mapper;

import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.pojo.BookBorrowReturnFlowBook;
import org.springframework.stereotype.Repository;

@Repository
public interface BookBorrowReturnFlowBookMapper {
    BookBorrowReturnFlowBook findOne(LumosSelective selective);
    long insert(BookBorrowReturnFlowBook bookBorrowReturnFlowBook);
}
