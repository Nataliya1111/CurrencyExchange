package com.nataliya1111.service;

import com.nataliya1111.dao.CurrencyDao;
import com.nataliya1111.dao.ExchangeRatesDao;
import com.nataliya1111.dto.ExchangeRatesDto;
import com.nataliya1111.entity.Currency;
import com.nataliya1111.entity.ExchangeRate;

import java.util.ArrayList;
import java.util.List;

public class ExchangeRatesService {

    private static final ExchangeRatesService INSTANCE = new ExchangeRatesService();

    private ExchangeRatesService(){
    }

    public List<ExchangeRatesDto> getAll(){
        ExchangeRatesDao exchangeRatesDao = ExchangeRatesDao.getInstance();
        List<ExchangeRate> exchangeRateList = exchangeRatesDao.getAll();

        CurrencyDao currencyDao = CurrencyDao.getInstance();
        List<ExchangeRatesDto> exchangeRateDtoList = new ArrayList<>();

        for (ExchangeRate exchangeRate : exchangeRateList){
            ExchangeRatesDto exchangeRatesDto = new ExchangeRatesDto(
                    exchangeRate.getId(),
                    (currencyDao.getById(exchangeRate.getBaseCurrencyId())).get(),
                    (currencyDao.getById(exchangeRate.getTargetCurrencyId())).get(),
                    exchangeRate.getRate()
            );
            exchangeRateDtoList.add(exchangeRatesDto);
        }

        return exchangeRateDtoList;
    }

    public static ExchangeRatesService getInstance(){
        return INSTANCE;
    }
}
