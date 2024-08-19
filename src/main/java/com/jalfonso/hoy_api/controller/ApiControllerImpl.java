package com.jalfonso.hoy_api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jalfonso.hoy_api.model.Noticia;
import com.jalfonso.hoy_api.service.ApiServiceImpl;

@RestController
public class ApiControllerImpl implements ApiController {
    ApiServiceImpl apiService = new ApiServiceImpl();
    
    @GetMapping("/consulta")
    public ResponseEntity<List<Noticia>> Noticia(@RequestParam(value = "search", required = true) String searchParameter,
                                @RequestParam(value = "photo", required = false, defaultValue = "false") Boolean returnPhoto) throws Exception {
        
        return apiService.getNews(searchParameter, returnPhoto);
    
    }
}
