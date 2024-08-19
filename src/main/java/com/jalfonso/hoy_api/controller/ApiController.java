package com.jalfonso.hoy_api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import com.jalfonso.hoy_api.model.Noticia;

public interface ApiController {
    public ResponseEntity<List<Noticia>> Noticia(@RequestParam(value = "search", required = true) String searchParameter,
                                @RequestParam(value = "photo", required = false, defaultValue = "false") Boolean returnPhoto) throws Exception;
}
