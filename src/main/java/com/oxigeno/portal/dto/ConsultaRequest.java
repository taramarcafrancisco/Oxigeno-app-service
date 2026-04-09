package com.oxigeno.portal.dto;

import lombok.Data;

@Data
public class ConsultaRequest {

    private String consulta;
    private String direccionNormalizada;
    private Double latitud;
    private Double longitud;
    private Double confianza;
    private Boolean validada;
    private String respuestaCompleta;
    private String tipoProducto;
}
