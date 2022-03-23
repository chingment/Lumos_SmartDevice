package com.caterbao.lumos.api.device.service.impl;

import com.caterbao.lumos.api.device.rop.RetIdentityInfo;
import com.caterbao.lumos.api.device.rop.RetIdentityVerify;
import com.caterbao.lumos.api.device.rop.RopIdentityInfo;
import com.caterbao.lumos.api.device.rop.RopIdentityVerify;
import com.caterbao.lumos.api.device.service.IdentityService;
import com.caterbao.lumos.locals.biz.model.BookerCountBorrowBookResult;
import com.caterbao.lumos.locals.biz.service.BookerService;
import com.caterbao.lumos.locals.common.CustomResult;
import com.caterbao.lumos.locals.common.vo.FieldVo;
import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.mapper.IcCardMapper;
import com.caterbao.lumos.locals.dal.pojo.IcCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class IdentityServiceImpl implements IdentityService {

    private IcCardMapper icCardMapper;
    private com.caterbao.lumos.locals.biz.service.BookerService bizBookerService;

    @Autowired(required = false)
    public void setIcCardMapper(IcCardMapper icCardMapper) {
        this.icCardMapper = icCardMapper;
    }


    @Autowired(required = false)
    public void setBizBookerService(BookerService bizBookerService) {
        this.bizBookerService = bizBookerService;
    }

    @Override
    public CustomResult<RetIdentityVerify> verify(String operater, RopIdentityVerify rop) {

        CustomResult<RetIdentityVerify> result = new CustomResult<>();


        if (rop.getDataType().equals("1")) {

            LumosSelective selective_IcCard = new LumosSelective();
            selective_IcCard.setFields("*");
            selective_IcCard.addWhere("CardNo", rop.getPayload());

            IcCard d_IcCard = icCardMapper.findOne(selective_IcCard);

            if (d_IcCard == null)
                return result.fail("验证失败");

            RetIdentityVerify ret = new RetIdentityVerify();

            ret.setClientUserId(d_IcCard.getClientUserId());
            ret.setIdentityType(1);
            ret.setIdentityId(d_IcCard.getId());

            return result.success("验证成功", ret);
        }

        return result.fail("验证失败");
    }


    @Override
    public CustomResult<RetIdentityInfo> info(String operater, RopIdentityInfo rop) {
        CustomResult<RetIdentityInfo> result = new CustomResult<>();

        if (rop.getIdentityType() == 1) {

            LumosSelective selective_IcCard = new LumosSelective();
            selective_IcCard.setFields("*");
            selective_IcCard.addWhere("IcCardId", rop.getIdentityId());

            IcCard d_IcCard = icCardMapper.findOne(selective_IcCard);
            if (d_IcCard == null)
                return result.fail("获取失败");

            RetIdentityInfo ret = new RetIdentityInfo();

            ret.setSceneMode(rop.getSceneMode());

            HashMap<String, Object> info = new HashMap<>();

            info.put("fullName", d_IcCard.getFullName());
            info.put("cardNo", d_IcCard.getCardNo());

            BookerCountBorrowBookResult rt_CountBorrowBook = bizBookerService.countBorrowBookResult(rop.getClientUserId());

            info.put("overdueFine", rt_CountBorrowBook.getOverdueFine());

            int borrowedQuantity = rt_CountBorrowBook.getBorrowedQuantity();

            int maxBorrowQuantity = 5;//最大可借书的数量

            int canBorrowQuantity = maxBorrowQuantity - borrowedQuantity;
            info.put("borrowedQuantity", borrowedQuantity);
            info.put("canBorrowQuantity", canBorrowQuantity);
            info.put("willdueQuantity", rt_CountBorrowBook.getWilldueQuantity());//即将到期数量
            info.put("overdueQuantity", rt_CountBorrowBook.getOverdueQuantity());//逾期数量

            FieldVo status=new FieldVo(1,"正常");

            info.put("status",status);

            ret.setInfo(info);

            return result.success("获取成功", ret);
        }

        return result.fail("获取失败");
    }

}
