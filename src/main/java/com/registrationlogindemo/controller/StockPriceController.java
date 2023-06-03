package com.registrationlogindemo.controller;

import com.registrationlogindemo.model.StockPrice;
import com.registrationlogindemo.service.StockPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
public class StockPriceController {
    @Autowired
    private StockPriceService stockPriceService;
    @RequestMapping("stockprices")
    public String getAllStockPrices(Model model) {
        List<StockPrice> stockPrices = stockPriceService.getAllStockPricesToToday(); //Calling the relevant service layer method

        model.addAttribute("stockprices", stockPrices);

        return "stockprices.html";
    }

}
