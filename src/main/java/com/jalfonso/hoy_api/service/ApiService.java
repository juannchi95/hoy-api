package com.jalfonso.hoy_api.service;

import java.util.List;
import org.springframework.http.ResponseEntity;
import com.jalfonso.hoy_api.model.Noticia;

public interface ApiService {
    public ResponseEntity<List<Noticia>>getNews(String searchValue, Boolean returnPhoto) throws Exception;
}
