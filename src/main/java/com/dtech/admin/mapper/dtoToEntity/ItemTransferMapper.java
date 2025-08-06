package com.dtech.admin.mapper.dtoToEntity;

import com.dtech.admin.dto.request.TransferRequestDTO;
import com.dtech.admin.dto.request.TransferRequestListDTO;
import com.dtech.admin.enums.Status;
import com.dtech.admin.enums.Transfer;
import com.dtech.admin.model.ItemTransfer;
import com.dtech.admin.model.ItemTransferDetails;
import com.dtech.admin.model.Location;
import com.dtech.admin.model.Stock;
import lombok.extern.log4j.Log4j2;


@Log4j2
public class ItemTransferMapper {

    public static ItemTransferDetails mapItemTransferDetails(TransferRequestDTO dto, Stock stock) {
        log.info("mapItemTransferDetails");

        ItemTransferDetails itemTransferDetails = new ItemTransferDetails();

        itemTransferDetails.setQty(dto.getQty());
        itemTransferDetails.setItem(stock.getItem());
        itemTransferDetails.setLablePrice(stock.getLablePrice());
        itemTransferDetails.setTotCost(dto.getTotCost());
        itemTransferDetails.setRetailPrice(stock.getRetailPrice());
        itemTransferDetails.setRetailPrice(stock.getRetailPrice());
        itemTransferDetails.setWholesalePrice(stock.getWholesalePrice());
        itemTransferDetails.setRetailDiscount(stock.getRetailDiscount());
        itemTransferDetails.setWholesaleDiscount(stock.getWholesaleDiscount());
        return itemTransferDetails;
    }
    public static ItemTransfer mapItemTransfer(TransferRequestListDTO dto,Location fromLocation,Location toLocation) {
        log.info("mapItemTransfer");
        ItemTransfer itemTransfer = new ItemTransfer();
        itemTransfer.setFromLocation(fromLocation);
        itemTransfer.setToLocation(toLocation);
        itemTransfer.setSenderRemark(dto.getSenderRemark());
        itemTransfer.setTransferStatus(Transfer.RECEIVED);
        itemTransfer.setStatus(Status.ACTIVE);
        return itemTransfer;
    }

}
