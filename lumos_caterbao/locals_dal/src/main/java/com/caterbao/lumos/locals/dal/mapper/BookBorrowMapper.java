package com.caterbao.lumos.locals.dal.mapper;

import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.pojo.BookBorrow;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookBorrowMapper {
    BookBorrow findOne(LumosSelective selective);

    List<BookBorrow> find(LumosSelective selective);

    long insert(BookBorrow bookBorrow);

    long update(BookBorrow bookBorrow);
}
