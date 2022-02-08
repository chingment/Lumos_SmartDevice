package com.caterbao.lumos.locals.dal.mapper;

import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.pojo.BookBorrower;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookBorrowerMapper {
    List<BookBorrower> find(LumosSelective selective);
}
