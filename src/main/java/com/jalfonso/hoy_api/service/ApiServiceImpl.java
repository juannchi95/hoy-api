package com.jalfonso.hoy_api.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jalfonso.hoy_api.exceptions.BadRequestException;
import com.jalfonso.hoy_api.exceptions.InternalServerExcepcion;
import com.jalfonso.hoy_api.exceptions.NotFoundException;
import com.jalfonso.hoy_api.model.Noticia;
import com.jalfonso.hoy_api.util.Util;

@Service
public class ApiServiceImpl implements ApiService {
    private static final Logger log = LoggerFactory.getLogger(ApiServiceImpl.class);
    
    public ResponseEntity<List<Noticia>> getNews(String searchParameter, Boolean returnPhoto) throws Exception {
        List<Noticia> responseDto = new ArrayList<>();
        try {
            if (searchParameter == null || searchParameter.isEmpty() || searchParameter.equals("") ) {
                throw new BadRequestException("g268", Util.BAD_REQUEST);
            }
    
            responseDto = getDataFromNews(searchParameter, returnPhoto);
        } catch (Exception e) {
            log.error("Ocurrio un error al obtener las noticias: {}", e.getMessage());
            if(e instanceof BadRequestException){
                throw new BadRequestException("g268", Util.BAD_REQUEST);
            } else if(e instanceof NotFoundException){
                throw new NotFoundException("g267", Util.NOT_FOUND + searchParameter);
            } 
            throw new InternalServerExcepcion("g100", Util.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Noticia>>(responseDto, HttpStatus.OK);
    }

    /**
     * @param searchParameter
     * @param returnPhoto
     * @return
     */
    public List<Noticia> getDataFromNews(String searchParameter, Boolean returnPhoto) {        
        String url = "https://www.hoy.com.py/search/" + searchParameter;       
        Document document;
        Connection connection;
        try {
            connection = Jsoup.connect(url).timeout(300000);
            document = connection.get();
        } catch (IOException e) {
            log.error("Error al obtener datos de la web: {}", e.getMessage());
            throw new InternalServerExcepcion("g100", Util.INTERNAL_SERVER_ERROR);
        }

        if(connection.response().statusCode() != HttpStatus.OK.value()){
            log.error("Error al obtener datos de la web: {}", connection.response().statusCode());
            throw new InternalServerExcepcion("g100", Util.INTERNAL_SERVER_ERROR);
        }

        Element resultData = document.getElementsByClass("columns-content").first();

        if(resultData == null){
            log.error("No se encuentran noticias para el texto: {}", searchParameter);
            throw new NotFoundException("g267", Util.NOT_FOUND + searchParameter);
        }

        Elements articleList = resultData.select("article");
        log.info("cantidad de articulos: {} " , articleList.size());
        
        return getNewsList(articleList, returnPhoto);
    }

    private List<Noticia> getNewsList(Elements articleList, Boolean returnPhoto) {
        List<Noticia> listaNoticias = new ArrayList<>();
        Noticia noticia;
        HashMap<String, String> photoData;
        
        for (Element article : articleList) {
            noticia = new Noticia();
            String title = article.select("h3").text();
            String link = article.select("h3").select("a").attr("href");
            String date = Util.formatDateTime(article.select("p").text());
            
            noticia.setTitulo(title);
            noticia.setFecha(date);
            noticia.setEnlace(link);

            if(returnPhoto){
                photoData = new HashMap<>();
                Element photo = article.select("img").first();
                String photoUrl = photo.attr("src");
                photoData = Util.getPhotoDataMap(photoUrl);
                
                noticia.setEnlaceFoto(photoUrl);
                noticia.setFoto(photoData.get("photoEncode"));
                noticia.setTipoFoto(photoData.get("type"));
            }else{
                noticia.setEnlaceFoto("");
                noticia.setFoto("");
                noticia.setTipoFoto("");
            }
            listaNoticias.add(noticia);
        }
        return listaNoticias;
    }
}

