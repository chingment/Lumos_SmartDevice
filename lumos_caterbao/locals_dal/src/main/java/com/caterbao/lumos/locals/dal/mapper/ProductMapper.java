package com.caterbao.lumos.locals.dal.mapper;

import com.caterbao.lumos.locals.dal.pojo.PrdSpu;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductMapper {
    List<PrdSpu> getSpus();
}
