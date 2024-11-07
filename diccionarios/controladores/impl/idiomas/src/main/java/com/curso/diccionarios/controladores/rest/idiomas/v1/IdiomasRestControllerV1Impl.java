package com.curso.diccionarios.controladores.rest.idiomas.v1;


import com.curso.diccionarios.controladores.rest.idiomas.api.IdiomasRestControllerV1;
import com.curso.diccionarios.controladores.rest.idiomas.api.dtos.GetIdiomasRestV1DTO;
import com.curso.diccionarios.controladores.rest.idiomas.api.dtos.IdiomaCreadoRestV1DTO;
import com.curso.diccionarios.controladores.rest.idiomas.api.dtos.IdiomaRestV1DTO;
import org.springframework.http.ResponseEntity;

public class IdiomasRestControllerV1Impl implements IdiomasRestControllerV1 {

    public GetIdiomasRestV1DTO obtenerIdiomas(){
        return null;
    }
    public ResponseEntity<IdiomaCreadoRestV1DTO> crearIdioma(IdiomaRestV1DTO datosIdioma){
        return null;
    }
    public ResponseEntity<IdiomaCreadoRestV1DTO> recuperarIdioma(String codigoIdioma){
        return null;
    }

}
