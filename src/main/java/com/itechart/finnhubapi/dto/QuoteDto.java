package com.itechart.finnhubapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.itechart.finnhubapi.model.QuoteEntity;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuoteDto {
    private double c;//Current price
    private double d;//Change
    private double dp;//Percent change
    private double h;//High price of the day
    private double l;//Low price of the day
    private double o;//Open price of the day
    private double pc;//Previous close price
    private double t;//????

    public QuoteEntity toQuoteEntity() {
        QuoteEntity quoteEntity = new QuoteEntity();
        quoteEntity.setC(c);
        quoteEntity.setD(d);
        quoteEntity.setDp(dp);
        quoteEntity.setH(h);
        quoteEntity.setL(l);
        quoteEntity.setO(o);
        quoteEntity.setPc(pc);
        quoteEntity.setT(t);
        return quoteEntity;
    }

    public static QuoteDto fromQuoteEntity(QuoteEntity quoteEntity) {
        QuoteDto quoteDto = new QuoteDto();
        quoteDto.setC(quoteEntity.getC());
        quoteDto.setD(quoteEntity.getD());
        quoteDto.setDp(quoteEntity.getDp());
        quoteDto.setH(quoteEntity.getH());
        quoteDto.setL(quoteEntity.getL());
        quoteDto.setO(quoteEntity.getO());
        quoteDto.setPc(quoteEntity.getPc());
        quoteDto.setT(quoteEntity.getT());
        return quoteDto;
    }
}
