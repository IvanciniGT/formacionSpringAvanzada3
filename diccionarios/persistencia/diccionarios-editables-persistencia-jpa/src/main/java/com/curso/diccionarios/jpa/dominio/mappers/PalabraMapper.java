package com.curso.diccionarios.jpa.dominio.mappers;

import com.curso.diccionarios.dominio.model.*;
import com.curso.diccionarios.jpa.dominio.entity.*;
import lombok.NonNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PalabraMapper {

    PalabraEditable entityToModel(PalabraEntity entity);

    PalabraEntity modelToEntity(PalabraEditable model);


    VarianteEntity modelToEntity(Variante model);

    SignificadoEditable significadoEntityToModel(SignificadoEntity entity);

    SignificadoEntity modelToEntity(SignificadoEditable model);

    DiccionarioEntity diccionarioModelToEntity(Diccionario model);

    TipoMorfologicoEntity tipoMorfologicoModelToEntity(@NonNull TipoMorfologico tipoMorfologico);


    Contexto contextoEntityToModel(ContextoEntity contextoEntity);

    TipoMorfologico tipoMorfologicoEntityToModel(TipoMorfologicoEntity tipoMorfologicoEntity);

    // Mapeo de SignificadoEditable a SignificadoEntity, ignorando el campo numero
    @Mapping(target = "numero", ignore = true)  // Ignoramos el campo numero
    void updateSignificadoFromEditable(SignificadoEditable dto, @MappingTarget SignificadoEntity entity);

}
