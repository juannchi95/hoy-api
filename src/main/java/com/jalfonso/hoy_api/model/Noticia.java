package com.jalfonso.hoy_api.model;

import lombok.Data;

@Data
public class Noticia {
    private String fecha;
    private String enlace;
    private String enlaceFoto;
    private String titulo;
    private String foto;
    private String tipoFoto;
}
