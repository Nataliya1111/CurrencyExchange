package com.nataliya1111.service;

import com.nataliya1111.dao.CurrencyDao;
import com.nataliya1111.dao.ExchangeRatesDao;
import com.nataliya1111.dto.ExchangeRateRequestDto;
import com.nataliya1111.dto.ExchangeRateResponseDto;
import com.nataliya1111.entity.Currency;
import com.nataliya1111.entity.ExchangeRate;
import com.nataliya1111.exception.DataExistsException;
import com.nataliya1111.exception.DataNotFoundException;
import com.nataliya1111.exception.InvalidRequestException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRatesService {

    private static final ExchangeRatesService INSTANCE = new ExchangeRatesService();

    private final ExchangeRatesDao exchangeRatesDao = ExchangeRatesDao.getInstance();
    private final CurrencyDao currencyDao = CurrencyDao.getInstance();

    private ExchangeRatesService(){
    }

    public ExchangeRateResponseDto add(ExchangeRateRequestDto exchangeRateRequestDto){
        Currency baseCurrency = currencyDao.getByCode(exchangeRateRequestDto.getBaseCurrencyCode()).
                orElseThrow(() -> new InvalidRequestException("Invalid request: Currency is not found"));   //400
        Currency targetCurrency = currencyDao.getByCode(exchangeRateRequestDto.getTargetCurrencyCode()).
                orElseThrow(() -> new InvalidRequestException("Invalid request: Currency is not found"));   //400

        if (baseCurrency.getCode().equals(targetCurrency.getCode())){
            throw new InvalidRequestException("Invalid request: Currency codes have to be different");
        }

        ExchangeRate newExchangeRate = new ExchangeRate();

        newExchangeRate.setBaseCurrencyId(baseCurrency.getId());
        newExchangeRate.setTargetCurrencyId(targetCurrency.getId());
        newExchangeRate.setRate(new BigDecimal(exchangeRateRequestDto.getRate()));

        Optional<ExchangeRate> optionalExchangeRate = exchangeRatesDao.getByBaseAndTargetId(baseCurrency.getId(), targetCurrency.getId());
        if (optionalExchangeRate.isPresent()){
            throw new DataExistsException("Exchange rate with such currencies already exists");   //409
        };

        ExchangeRate addedExchangeRate = exchangeRatesDao.add(newExchangeRate);

        return new ExchangeRateResponseDto(
                addedExchangeRate.getId(),
                (currencyDao.getById(addedExchangeRate.getBaseCurrencyId())).get(),
                (currencyDao.getById(addedExchangeRate.getTargetCurrencyId())).get(),
                addedExchangeRate.getRate()
        );
    }

    public List<ExchangeRateResponseDto> getAll(){

        List<ExchangeRate> exchangeRateList = exchangeRatesDao.getAll();
        List<ExchangeRateResponseDto> exchangeRateResponseDtoList = new ArrayList<>();

        for (ExchangeRate exchangeRate : exchangeRateList){
            ExchangeRateResponseDto exchangeRatesDto = new ExchangeRateResponseDto(
                    exchangeRate.getId(),
                    (currencyDao.getById(exchangeRate.getBaseCurrencyId())).get(),
                    (currencyDao.getById(exchangeRate.getTargetCurrencyId())).get(),
                    exchangeRate.getRate()
            );
            exchangeRateResponseDtoList.add(exchangeRatesDto);
        }
        return exchangeRateResponseDtoList;
    }

    public ExchangeRateResponseDto getByCodes(String codesPair) throws InvalidRequestException, DataNotFoundException{
        String baseCurrencyCode = codesPair.substring(0, 3);
        String targetCurrencyCode = codesPair.substring(3);

        Currency baseCurrency = currencyDao.getByCode(baseCurrencyCode)
                .orElseThrow(() -> new InvalidRequestException("Invalid request: Currency is not found"));   //400
        Currency targetCurrency = currencyDao.getByCode(targetCurrencyCode)
                .orElseThrow(() -> new InvalidRequestException("Invalid request: Currency is not found"));   //400

        ExchangeRate exchangeRate = exchangeRatesDao.getByBaseAndTargetId(baseCurrency.getId(), targetCurrency.getId())
                .orElseThrow(() -> new DataNotFoundException("Exchange rate is not found"));   //404

        return new ExchangeRateResponseDto(
                exchangeRate.getId(),
                baseCurrency,
                targetCurrency,
                exchangeRate.getRate()
        );
    }

    public ExchangeRateResponseDto updateExchangeRate(ExchangeRateRequestDto exchangeRateRequestDto){

        Currency baseCurrency = currencyDao.getByCode(exchangeRateRequestDto.getBaseCurrencyCode())
                .orElseThrow(() -> new InvalidRequestException("Invalid request: Currency is not found"));   //400
        Currency targetCurrency = currencyDao.getByCode(exchangeRateRequestDto.getTargetCurrencyCode())
                .orElseThrow(() -> new InvalidRequestException("Invalid request: Currency is not found"));   //400

        Optional<ExchangeRate> optionalExchangeRate = exchangeRatesDao.getByBaseAndTargetId(baseCurrency.getId(), targetCurrency.getId());
        ExchangeRate exchangeRate = optionalExchangeRate.orElseThrow(() -> new DataNotFoundException("Exchange rate with such currencies does not exist"));

        exchangeRate.setRate(new BigDecimal(exchangeRateRequestDto.getRate()));

        exchangeRatesDao.updateExchangeRate(exchangeRate);

        return new ExchangeRateResponseDto(
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
