package com.nataliya1111;

import com.nataliya1111.dao.CurrencyDao;
import com.nataliya1111.dao.ExchangeRatesDao;
import com.nataliya1111.entity.Currency;
import com.nataliya1111.entity.ExchangeRate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class DaoTest {

    public static void main(String[] args) {

        //addCurrency();
        //deleteCurrency();

        CurrencyDao currencyDao = CurrencyDao.getInstance();
        Optional<Currency> currency = currencyDao.getByCode("EUR");
        System.out.println(currency);
        List<Currency> currencyList = currencyDao.getAll();
        System.out.println(currencyList);

        List<ExchangeRate> exchangeRateList = ExchangeRatesDao.getInstance().getAll();
        System.out.println(exchangeRateList);

        String qwe = "";
        System.out.println(qwe.isBlank());

        BigDecimal bigDecimal = new BigDecimal("0006000.00000");
        System.out.println(bigDecimal);


    }

    private static void addCurrency() {
        CurrencyDao currencyDao = CurrencyDao.getInstance();
        Currency currency = new Currency();
        currency.setCode("QWE");
        currency.setFullName("Qwerty");
        currency.setSign("Q");

        Currency newCurrency = currencyDao.add(currency);

        System.out.println(currency);
    }

//    private static void deleteCurrency(){
//        CurrencyDao currencyDao = CurrencyDao.getInstance();
//        System.out.println(currencyDao.delete(12L));
//    }
}
