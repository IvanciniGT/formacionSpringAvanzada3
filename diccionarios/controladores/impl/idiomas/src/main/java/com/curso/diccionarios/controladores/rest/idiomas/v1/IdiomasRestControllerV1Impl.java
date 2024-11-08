package com.curso.diccionarios.controladores.rest.idiomas.v1;


import com.curso.diccionarios.controladores.rest.idiomas.api.IdiomasRestControllerV1;
import com.curso.diccionarios.controladores.rest.idiomas.api.dtos.GetIdiomasRestV1DTO;
import com.curso.diccionarios.controladores.rest.idiomas.api.dtos.IdiomaCreadoRestV1DTO;
import com.curso.diccionarios.controladores.rest.idiomas.api.dtos.IdiomaRestV1DTO;
import com.curso.diccionarios.controladores.rest.idiomas.api.dtos.IdiomasRestV1DTO;
import com.curso.diccionarios.controladores.rest.idiomas.v1.mappers.IdiomasRestV1Mapper;
import com.curso.diccionarios.services.idiomas.IdiomasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class IdiomasRestControllerV1Impl implements IdiomasRestControllerV1 {

    private final IdiomasService servicio;
    private final IdiomasRestV1Mapper mapeador;

    public GetIdiomasRestV1DTO obtenerIdiomas(){
        /*
        List<IdiomaDTO> idiomasDelServicio = servicio.recuperarTodosLosIdiomas();
        List<IdiomaRestV1DTO> idiomas = idiomasDelServicio.stream().map(mapeador::idiomaDTO2idiomaRestV1DTO).toList();
        // Programación imperativa
        //List<IdiomaRestV1DTO> idiomas = new ArrayList<>();
        //for(IdiomaDTO idioma: idiomasDelServicio){
        //    idiomas.add(mapeador.idiomaDTO2idiomaRestV1DTO(idioma));
        //}
        //

        return new IdiomasRestV1DTO(idiomas);
        */
        return new IdiomasRestV1DTO(
                servicio.recuperarTodosLosIdiomas().stream()
                        .map(mapeador::idiomaDTO2idiomaRestV1DTO)
                        .toList()
        );
    }
    public ResponseEntity<IdiomaCreadoRestV1DTO> crearIdioma(IdiomaRestV1DTO datosIdioma){
        /*IdiomaDTO nuevoIdiomaACrear = mapeador.idiomaRestV1DTO2idiomaDTO(datosIdioma);
        IdiomaDTO idiomaCreado = servicio.nuevoIdioma(nuevoIdiomaACrear);
        return ResponseEntity.ok(mapeador.idiomaDTO2idiomaRestV1DTO(idiomaCreado));*/
        return new ResponseEntity<>(
                mapeador.idiomaDTO2idiomaRestV1DTO(
                        servicio.nuevoIdioma(
                                mapeador.idiomaRestV1DTO2idiomaDTO(datosIdioma)
                        )
                ), HttpStatus.CREATED
        );
    }
    public ResponseEntity<IdiomaCreadoRestV1DTO> recuperarIdioma(
            String codigoIdioma){
        /*
        Optional<IdiomaDTO> posibleIdioma = servicio.recuperarIdiomaPorCodigo(codigoIdioma);
        if(posibleIdioma.isEmpty())
            return ResponseEntity.notFound().build();
        IdiomaDTO idiomaDevuelto = posibleIdioma.get();
        return ResponseEntity.ok(mapeador.idiomaDTO2idiomaRestV1DTO(idiomaDevuelto));
        */

        return servicio.recuperarIdiomaPorCodigo(codigoIdioma)
                .map( idioma-> (IdiomaCreadoRestV1DTO)(mapeador.idiomaDTO2idiomaRestV1DTO(idioma)))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
