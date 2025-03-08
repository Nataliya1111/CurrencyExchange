package com.nataliya1111.service;

import com.nataliya1111.dao.CurrencyDao;
import com.nataliya1111.dao.ExchangeRatesDao;
import com.nataliya1111.dto.ExchangeRateDto;
import com.nataliya1111.entity.Currency;
import com.nataliya1111.entity.ExchangeRate;
import com.nataliya1111.exception.NoSuchCurrencyException;
import com.nataliya1111.exception.NoSuchExchangeRateException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public ExchangeRateDto getByCodes(String codesPair) throws NoSuchCurrencyException, NoSuchExchangeRateException{
        String baseCurrencyCode = codesPair.substring(0, 3);
        String targetCurrencyCode = codesPair.substring(3);

        CurrencyDao currencyDao = CurrencyDao.getInstance();
        Currency baseCurrency = currencyDao.getByCode(baseCurrencyCode)
                .orElseThrow(() -> new NoSuchCurrencyException("Invalid request: Currency is not found"));
        Currency targetCurrency = currencyDao.getByCode(targetCurrencyCode)
                .orElseThrow(() -> new NoSuchCurrencyException("Invalid request: Currency is not found"));

        ExchangeRate exchangeRate = exchangeRatesDao.getByBaseAndTargetId(baseCurrency.getId(), targetCurrency.getId())
                .orElseThrow(() -> new NoSuchExchangeRateException("Exchange rate is not found"));

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
