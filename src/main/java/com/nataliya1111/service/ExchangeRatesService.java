package com.nataliya1111.service;

import com.nataliya1111.dao.CurrencyDao;
import com.nataliya1111.dao.ExchangeRatesDao;
import com.nataliya1111.dto.ExchangeRateDto;
import com.nataliya1111.entity.Currency;
import com.nataliya1111.entity.ExchangeRate;
import com.nataliya1111.exception.DataNotFoundException;
import com.nataliya1111.exception.InvalidRequestException;

import java.util.ArrayList;
import java.util.List;

public class ExchangeRatesService {

    private static final ExchangeRatesService INSTANCE = new ExchangeRatesService();

    private final ExchangeRatesDao exchangeRatesDao = ExchangeRatesDao.getInstance();
    private final CurrencyDao currencyDao = CurrencyDao.getInstance();

    private ExchangeRatesService(){
    }

    public List<ExchangeRateDto> getAll(){

        List<ExchangeRate> exchangeRateList = exchangeRatesDao.getAll();
        List<ExchangeRateDto> exchangeRateDtoList = new ArrayList<>();

        for (ExchangeRate exchangeRate : exchangeRateList){
            ExchangeRateDto exchangeRatesDto = new ExchangeRateDto(
                    exchangeRate.getId(),
                    (currencyDao.getById(exchangeRate.getBaseCurrencyId())).get(),
                    (currencyDao.getById(exchangeRate.getTargetCurrencyId())).get(),
                    exchangeRate.getRate()
            );
            exchangeRateDtoList.add(exchangeRatesDto);
        }
        return exchangeRateDtoList;
    }

    public ExchangeRateDto getByCodes(String codesPair) throws InvalidRequestException, DataNotFoundException{
        String baseCurrencyCode = codesPair.substring(0, 3);
        String targetCurrencyCode = codesPair.substring(3);

        CurrencyDao currencyDao = CurrencyDao.getInstance();
        Currency baseCurrency = currencyDao.getByCode(baseCurrencyCode)
                .orElseThrow(() -> new InvalidRequestException("Invalid request: Currency is not found"));   //400
        Currency targetCurrency = currencyDao.getByCode(targetCurrencyCode)
                .orElseThrow(() -> new InvalidRequestException("Invalid request: Currency is not found"));   //400

        ExchangeRate exchangeRate = exchangeRatesDao.getByBaseAndTargetId(baseCurrency.getId(), targetCurrency.getId())
                .orElseThrow(() -> new DataNotFoundException("Exchange rate is not found"));   //404

        return new ExchangeRateDto(
                exchangeRate.getId(),
                baseCurrency,
                targetCurrency,
                exchangeRate.getRate()
        );
    }

    public static ExchangeRatesService getInstance(){
        return INSTANCE;
    }
}
