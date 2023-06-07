package com.optiontrader.controller;

import com.optiontrader.model.StockPrice;
import com.optiontrader.service.StockPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class StockPriceController {
    /**
     * Controller for the stockprices list
     */
    @Autowired
    private StockPriceService stockPriceService;
    @RequestMapping("stockprices")
    public String getAllStockPrices(Model model) {
        List<StockPrice> stockPrices = stockPriceService.getAllStockPricesToToday(); //Calling the relevant service layer method to get all stock prices

        model.addAttribute("stockprices", stockPrices);

        return "stockprices.html";
    }

}
