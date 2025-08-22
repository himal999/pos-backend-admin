package com.dtech.admin.mapper.dtoToEntity;

import com.dtech.admin.dto.request.CashierBalanceActionRequestDTO;
import com.dtech.admin.enums.Status;
import com.dtech.admin.model.CashInOut;
import com.dtech.admin.model.CashierUser;
import lombok.extern.log4j.Log4j2;
@Log4j2
public class CashInOutMapper {

    public static CashInOut mapCashInout(CashierBalanceActionRequestDTO cashierBalanceActionRequestDTO, CashierUser cashierUser) {
        try {
            log.info("Cash in out mapper start dto to entity");
            CashInOut cashInOut = new CashInOut();
            cashInOut.setCashInOut(com.dtech.admin.enums.CashInOut.valueOf(cashierBalanceActionRequestDTO.getCashInOut()));
            cashInOut.setStatus(Status.ACTIVE);
            cashInOut.setCashierUser(cashierUser);
            cashInOut.setRemark(cashierBalanceActionRequestDTO.getRemark());
            cashInOut.setAmount(cashierBalanceActionRequestDTO.getAmount());
            log.info("Cashier balance cash in out  dto to entity {} ", cashierUser);
            return cashInOut;
        } catch (Exception e) {
            log.error(e);
            throw e;
        }
    }

}
