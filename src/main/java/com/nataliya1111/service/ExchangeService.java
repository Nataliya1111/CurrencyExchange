package com.nataliya1111.service;

import com.nataliya1111.dao.CurrencyDao;
import com.nataliya1111.dao.ExchangeRatesDao;
import com.nataliya1111.dto.ExchangeRequestDto;
import com.nataliya1111.dto.ExchangeResponseDto;
import com.nataliya1111.entity.Currency;
import com.nataliya1111.entity.ExchangeRate;
import com.nataliya1111.exception.DataNotFoundException;
import com.nataliya1111.exception.InvalidRequestException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.NoSuchElementException;
import java.util.Optional;

public class ExchangeService {

    private final static ExchangeService INSTANCE = new ExchangeService();
    private final static String USD_CODE = "USD";

    private final CurrencyDao currencyDao = CurrencyDao.getInstance();
    private final ExchangeRatesDao exchangeRatesDao = ExchangeRatesDao.getInstance();

    private ExchangeService(){
    }

    public static ExchangeService getInstance(){
        return INSTANCE;
    }

    public ExchangeResponseDto exchange(ExchangeRequestDto exchangeRequestDto){

        Currency baseCurrency = currencyDao.getByCode(exchangeRequestDto.getBaseCurrencyCode())
                .orElseThrow (() -> new InvalidRequestException("Invalid request: Currency is not found"));
        Currency targetCurrency = currencyDao.getByCode(exchangeRequestDto.getTargetCurrencyCode()).
                orElseThrow(() -> new InvalidRequestException("Invalid request: Currency is not found"));

        BigDecimal rate = findRate(baseCurrency.getId(), targetCurrency.getId())
                .orElseThrow(() -> new DataNotFoundException("Exchange rate for these currencies not found"));
        BigDecimal amount = exchangeRequestDto.getAmount();
        BigDecimal convertedAmount = rate.multiply(amount).setScale(6, RoundingMode.HALF_UP);
        return new ExchangeResponseDto(baseCurrency, targetCurrency, rate, amount, convertedAmount);

    }

    private Optional<BigDecimal> findRate (Long baseCurrencyId, Long targetCurrencyId) {

        Optional<BigDecimal> rateOptional = findByDirectCourse(baseCurrencyId, targetCurrencyId);
        if (rateOptional.isPresent()){
            return rateOptional;
        }

        rateOptional = findByReverseCourse(baseCurrencyId, targetCurrencyId);
        if (rateOptional.isPresent()){
            return rateOptional;
        }

        rateOptional = findByCrossCourse(baseCurrencyId, targetCurrencyId);
        return rateOptional;

    }

    private Optional<BigDecimal> findByDirectCourse(Long baseCurrencyId, Long targetCurrencyId){
        try {
            ExchangeRate directExchangeRate = exchangeRatesDao.getByBaseAndTargetId(baseCurrencyId, targetCurrencyId).orElseThrow();
            return Optional.of(directExchangeRate.getRate());
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }

    private Optional<BigDecimal> findByReverseCourse(Long baseCurrencyId, Long targetCurrencyId){
        try {
            ExchangeRate reverseExchangeRate = exchangeRatesDao.getByBaseAndTargetId(targetCurrencyId, baseCurrencyId).orElseThrow();
            return Optional.of(BigDecimal.ONE.divide(reverseExchangeRate.getRate(), 10, RoundingMode.HALF_UP));
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }

    private Optional<BigDecimal> findByCrossCourse(Long baseCurrencyId, Long targetCurrencyId){
        Optional<Currency> usdCurrencyOptional = currencyDao.getByCode(USD_CODE);
        if (usdCurrencyOptional.isEmpty()){
            return Optional.empty();
        }
        Currency usdCurrency = usdCurrencyOptional.get();
        Long usdCurrencyId = usdCurrency.getId();

        Optional<BigDecimal> baseToUsdRateOptional = findByDirectCourse(baseCurrencyId, usdCurrencyId);
        if (baseToUsdRateOptional.isEmpty()){
            baseToUsdRateOptional = findByReverseCourse(baseCurrencyId, usdCurrencyId);
            if (baseToUsdRateOptional.isEmpty()){
                return Optional.empty();
            }
        }
        BigDecimal baseToUsdRate = baseToUsdRateOptional.get();

        Optional<BigDecimal> usdToTargetRateOptional = findByDirectCourse(usdCurrencyId, targetCurrencyId);
        if (usdToTargetRateOptional.isEmpty()){
            usdToTargetRateOptional = findByReverseCourse(usdCurrencyId, targetCurrencyId);
            if (usdToTargetRateOptional.isEmpty()){
                return Optional.empty();
            }
        }
        BigDecimal usdToTargetRate = usdToTargetRateOptional.get();

        return Optional.of(baseToUsdRate.multiply(usdToTargetRate).setScale(10, RoundingMode.HALF_UP));
    }

}
