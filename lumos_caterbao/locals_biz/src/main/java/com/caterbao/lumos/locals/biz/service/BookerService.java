package com.caterbao.lumos.locals.biz.service;


import com.caterbao.lumos.locals.biz.model.BookerCalculateOverdueFineResult;

public interface BookerService {
    BookerCalculateOverdueFineResult CalculateOverdueFine(String clientUserId);
}
