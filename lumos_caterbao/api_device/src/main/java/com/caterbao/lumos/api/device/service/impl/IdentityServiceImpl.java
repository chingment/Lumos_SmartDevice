package com.caterbao.lumos.api.device.service.impl;

import com.caterbao.lumos.api.device.rop.RetIdentityInfo;
import com.caterbao.lumos.api.device.rop.RetIdentityVerify;
import com.caterbao.lumos.api.device.rop.RopIdentityInfo;
import com.caterbao.lumos.api.device.rop.RopIdentityVerify;
import com.caterbao.lumos.api.device.service.IdentityService;
import com.caterbao.lumos.locals.common.CustomResult;
import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.mapper.IcCardMapper;
import com.caterbao.lumos.locals.dal.pojo.IcCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IdentityServiceImpl implements IdentityService {

    private IcCardMapper icCardMapper;

    @Autowired(required = false)
    public void setIcCardMapper(IcCardMapper icCardMapper) {
        this.icCardMapper = icCardMapper;
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

            ret.setClientUserId("1");
            ret.setIdentityType("1");
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

            ret.setSignName(d_IcCard.getFullName());
            ret.setCardNo(d_IcCard.getCardNo());
            ret.setCanBorrowQuantity(2);
            ret.setBorrowedQuantity(1);
            ret.setFine(10);

            return result.success("获取成功", ret);
        }

        return result.fail("获取失败");
    }

}
