package com.curso.diccionarios.jpa.dominio.repository;

import com.curso.diccionarios.dominio.exception.AlreadyExistsEntityException;
import com.curso.diccionarios.dominio.exception.InvalidArgumentException;
import com.curso.diccionarios.dominio.exception.NonExistentEntityException;
import com.curso.diccionarios.dominio.model.*;
import com.curso.diccionarios.dominio.repository.PalabrasEditableRepository;
import com.curso.diccionarios.jpa.dominio.entity.*;
import com.curso.diccionarios.jpa.dominio.jparepository.*;
import com.curso.diccionarios.jpa.dominio.mappers.PalabraMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PalabrasEditableRepositoryImpl implements PalabrasEditableRepository {

    private final PalabraJpaRepository palabraJpaRepository;
    private final VarianteJpaRepository varianteJpaRepository;
    private final SignificadoJpaRepository significadoJpaRepository;
    private final ContextoJpaRepository contextoJpaRepository;
    private final TipoMorfologicoJpaRepository tipoMorfologicoJpaRepository;
    private final PalabraMapper palabraMapper;

    // Obtener una palabra en un diccionario
    @Override
    public Optional<PalabraEditable> getPalabra(@NonNull Diccionario diccionario, @NonNull String palabra) {
        return palabraJpaRepository.findByPalabraAndDiccionarioNombre(palabra, diccionario.getNombre())
                .map(palabraMapper::entityToModel);
    }

    // Obtener todas las palabras
    @Override
    public List<PalabraEditable> getPalabras() {
        return palabraJpaRepository.findAll().stream()
                .map(palabraMapper::entityToModel)
                .toList();
    }

    // Actualizar una palabra existente
    @Override
    public void updatePalabra(@NonNull PalabraEditable palabraEditable) throws InvalidArgumentException, NonExistentEntityException {
        Optional<PalabraEntity> existingPalabra = palabraJpaRepository.findByPalabraAndDiccionarioNombre(
                palabraEditable.getPalabra(),
                palabraEditable.getDiccionario().getNombre());

        if (existingPalabra.isEmpty()) {
            throw new NonExistentEntityException("La palabra no existe en el diccionario");
        }

        PalabraEntity palabraEntity = palabraMapper.modelToEntity(palabraEditable);
        palabraJpaRepository.save(palabraEntity);
    }

    // Crear una nueva palabra
    @Override
    public PalabraEditable newPalabra(@NonNull Diccionario diccionario, @NonNull String palabra) throws InvalidArgumentException, AlreadyExistsEntityException {
        if (palabraJpaRepository.findByPalabraAndDiccionarioNombre(palabra, diccionario.getNombre()).isPresent()) {
            throw new AlreadyExistsEntityException("La palabra ya existe en el diccionario");
        }

        PalabraEntity palabraEntity = new PalabraEntity();
        palabraEntity.setPalabra(palabra);
        palabraEntity.setDiccionario(palabraMapper.diccionarioModelToEntity(diccionario));

        return palabraMapper.entityToModel(palabraJpaRepository.save(palabraEntity));
    }

    // Eliminar una palabra
    @Override
    public Optional<PalabraEditable> deletePalabra(@NonNull String palabra) {
        Optional<PalabraEntity> existingPalabra = palabraJpaRepository.findByPalabra(palabra);

        existingPalabra.ifPresent(palabraJpaRepository::delete);
        return existingPalabra.map(palabraMapper::entityToModel);
    }

    // Gestión de variantes
    @Override
    public void addVariante(@NonNull PalabraEditable palabra, @NonNull String variante, @NonNull TipoMorfologico tipoMorfologico) throws InvalidArgumentException, NonExistentEntityException, AlreadyExistsEntityException {
        Optional<PalabraEntity> existingPalabra = palabraJpaRepository.findByPalabraAndDiccionarioNombre(
                palabra.getPalabra(),
                palabra.getDiccionario().getNombre());

        if (existingPalabra.isEmpty()) {
            throw new NonExistentEntityException("La palabra no existe en el diccionario");
        }

        // Uso de VarianteJpaRepository para buscar la variante
        Optional<VarianteEntity> existingVariante = varianteJpaRepository.findByVarianteAndPalabraId(variante, existingPalabra.get().getId());

        if (existingVariante.isPresent()) {
            throw new AlreadyExistsEntityException("La variante ya existe para esta palabra");
        }

        VarianteEntity varianteEntity = new VarianteEntity();
        varianteEntity.setVariante(variante);
        varianteEntity.setPalabra(existingPalabra.get());
        varianteEntity.setTipoMorfologico(palabraMapper.tipoMorfologicoModelToEntity(tipoMorfologico));

        varianteJpaRepository.save(varianteEntity);
    }

    @Override
    public Optional<Variante> removeVariante(@NonNull PalabraEditable palabra, @NonNull String variante) {
        Optional<PalabraEntity> existingPalabra = palabraJpaRepository.findByPalabraAndDiccionarioNombre(
                palabra.getPalabra(),
                palabra.getDiccionario().getNombre());

        if (existingPalabra.isEmpty()) {
            return Optional.empty();
        }

        // Eliminación directa de la variante por palabra y nombre
        varianteJpaRepository.deleteByVarianteAndPalabraId(variante, existingPalabra.get().getId());

        return Optional.empty();
    }

    // Gestión de significados
    @Override
    public SignificadoEditable addSignificado(@NonNull PalabraEditable palabra, @NonNull String definicion) throws InvalidArgumentException, NonExistentEntityException, AlreadyExistsEntityException {
        Optional<PalabraEntity> existingPalabra = palabraJpaRepository.findByPalabraAndDiccionarioNombre(
                palabra.getPalabra(),
                palabra.getDiccionario().getNombre());

        if (existingPalabra.isEmpty()) {
            throw new NonExistentEntityException("La palabra no existe en el diccionario");
        }

        // Verificación de existencia de significado
        Optional<SignificadoEntity> existingSignificado = significadoJpaRepository.findByDefinicionAndPalabraId(definicion, existingPalabra.get().getId());

        if (existingSignificado.isPresent()) {
            throw new AlreadyExistsEntityException("El significado ya existe para esta palabra");
        }

        SignificadoEntity significadoEntity = new SignificadoEntity();
        significadoEntity.setDefinicion(definicion);
        significadoEntity.setPalabra(existingPalabra.get());
        significadoEntity.setNumero(existingPalabra.get().getSignificados().size() + 1); // Número de orden

        significadoJpaRepository.save(significadoEntity);

        return palabraMapper.significadoEntityToModel(significadoEntity);
    }
    @Override
    public void updateSignificado(@NonNull SignificadoEditable significado) throws InvalidArgumentException, NonExistentEntityException {
        // Buscar la entidad existente en la base de datos por publicId
        Optional<SignificadoEntity> existingSignificadoOptional = significadoJpaRepository.findByPublicId(significado.getPublicId());

        if (existingSignificadoOptional.isEmpty()) {
            throw new NonExistentEntityException("El significado no existe");
        }

        SignificadoEntity existingSignificado = existingSignificadoOptional.get();

        // Verificar que la posición (numero) no haya cambiado
        if (!existingSignificado.getNumero().equals(significado.getNumero())) {
            throw new InvalidArgumentException("No se permite modificar la posición del significado directamente.");
        }

        // Usamos el mapper para actualizar los demás campos, excepto el campo "numero" y manteniendo el internalId intacto
        palabraMapper.updateSignificadoFromEditable(significado, existingSignificado);

        // Persistimos la entidad con el internalId correctamente asignado
        significadoJpaRepository.save(existingSignificado);
    }

    @Override
    public Optional<SignificadoEditable> removeSignificado(@NonNull String publicIdSignificado) {
        // Buscar el significado por su ID público
        Optional<SignificadoEntity> existingSignificado = significadoJpaRepository.findByPublicId(publicIdSignificado);

        if (existingSignificado.isEmpty()) {
            return Optional.empty();
        }

        // Obtener la palabra a la que pertenece el significado
        PalabraEntity palabra = existingSignificado.get().getPalabra();

        // Obtener el número del significado a eliminar
        Integer numeroEliminado = existingSignificado.get().getNumero();

        // Eliminar el significado
        significadoJpaRepository.delete(existingSignificado.get());

        // Obtener todos los significados restantes de la palabra (una sola query)
        List<SignificadoEntity> significadosRestantes = significadoJpaRepository.findByPalabraId(palabra.getId());

        // Reordenar los significados restantes en memoria, decrementando el número
        significadosRestantes.stream()
                .filter(s -> s.getNumero() > numeroEliminado)  // Solo aquellos que están después del eliminado
                .forEach(s -> s.setNumero(s.getNumero() - 1));  // Decrementar el número

        // Guardar los cambios en una sola operación
        significadoJpaRepository.saveAll(significadosRestantes);

        // Devolver el significado eliminado como modelo
        return Optional.of(palabraMapper.significadoEntityToModel(existingSignificado.get()));
    }


    @Override
    public List<SignificadoEditable> rearrangeSignificados(@NonNull PalabraEditable palabra, @NonNull List<SignificadoEditable> significados) throws NonExistentEntityException, InvalidArgumentException {
        // Buscar la palabra en la base de datos
        Optional<PalabraEntity> palabraEntityOptional = palabraJpaRepository.findByPalabraAndDiccionarioNombre(palabra.getPalabra(), palabra.getDiccionario().getNombre());

        if (palabraEntityOptional.isEmpty()) {
            throw new NonExistentEntityException("La palabra no existe en el diccionario");
        }

        PalabraEntity palabraEntity = palabraEntityOptional.get();

        // Recuperamos todos los significados asociados a la palabra en una única consulta
        List<SignificadoEntity> existingSignificados = palabraEntity.getSignificados();

        // Validamos que todos los significados recibidos pertenecen a la palabra
        List<String> publicIds = significados.stream().map(SignificadoEditable::getPublicId).toList();
        List<SignificadoEntity> significadosAReordenar = existingSignificados.stream()
                .filter(s -> publicIds.contains(s.getPublicId()))
                .toList();

        if (significadosAReordenar.size() != significados.size()) {
            throw new InvalidArgumentException("Uno o más significados no pertenecen a la palabra especificada");
        }

        // Paso 1: Asignar valores temporales negativos para evitar conflictos de unicidad
        for (int i = 0; i < significadosAReordenar.size(); i++) {
            SignificadoEntity significadoEntity = significadosAReordenar.get(i);
            significadoEntity.setNumero(-1 * (i + 1)); // Asignamos números temporales negativos
        }
        significadoJpaRepository.saveAll(significadosAReordenar); // Guardamos con los números temporales

        // Paso 2: Asignar los nuevos números de orden basados en el nuevo orden proporcionado
        for (int i = 0; i < significados.size(); i++) {
            String publicId = significados.get(i).getPublicId();
            SignificadoEntity significadoEntity = significadosAReordenar.stream()
                    .filter(s -> s.getPublicId().equals(publicId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Significado no encontrado: " + publicId));
            significadoEntity.setNumero(i + 1); // Asignamos el nuevo orden (empezando en 1)
        }

        // Persistimos todos los cambios en bloque con una sola operación
        significadoJpaRepository.saveAll(significadosAReordenar);

        // Devolvemos la lista de significados reordenados en el nuevo orden
        return significados;
    }

    // Añadir un ejemplo a un significado
    public void addEjemplo(@NonNull SignificadoEditable significado, @NonNull String ejemplo) throws NonExistentEntityException {
        // Busca el significado por su ID
        Optional<SignificadoEntity> existingSignificado = significadoJpaRepository.findByPublicId(significado.getPublicId());
        if (existingSignificado.isEmpty()) {
            throw new NonExistentEntityException("El significado no existe");
        }

        // Añadir el ejemplo si no existe ya en la lista
        List<String> ejemplos = existingSignificado.get().getEjemplos();
        if (!ejemplos.contains(ejemplo)) {
            ejemplos.add(ejemplo);
            existingSignificado.get().setEjemplos(ejemplos);
            significadoJpaRepository.save(existingSignificado.get());
        } else {
            throw new IllegalArgumentException("El ejemplo ya existe para este significado");
        }
    }

    // Eliminar un ejemplo de un significado
    public Optional<String> removeEjemplo(@NonNull SignificadoEditable significado, @NonNull String ejemplo) throws NonExistentEntityException {
        // Busca el significado por su ID
        Optional<SignificadoEntity> existingSignificado = significadoJpaRepository.findByPublicId(significado.getPublicId());
        if (existingSignificado.isEmpty()) {
            throw new NonExistentEntityException("El significado no existe");
        }

        // Eliminar el ejemplo si existe en la lista
        List<String> ejemplos = existingSignificado.get().getEjemplos();
        if (ejemplos.remove(ejemplo)) {
            existingSignificado.get().setEjemplos(ejemplos);
            significadoJpaRepository.save(existingSignificado.get());
            return Optional.of(ejemplo);
        }
        return Optional.empty();
    }

    // Añadir un sinónimo a un significado
    public void addSinonimo(@NonNull SignificadoEditable significado, @NonNull SignificadoEditable sinonimo) throws NonExistentEntityException {
        // Buscar el significado principal por PublicId
        Optional<SignificadoEntity> existingSignificado = significadoJpaRepository.findByPublicId(significado.getPublicId());
        if (existingSignificado.isEmpty()) {
            throw new NonExistentEntityException("El significado no existe");
        }

        // Buscar el sinónimo por PublicId
        Optional<SignificadoEntity> existingSinonimo = significadoJpaRepository.findByPublicId(sinonimo.getPublicId());
        if (existingSinonimo.isEmpty()) {
            throw new NonExistentEntityException("El sinónimo no existe");
        }

        // Añadir el sinónimo si no está ya relacionado
        List<SignificadoEntity> sinonimos = existingSignificado.get().getSinonimos();
        if (!sinonimos.contains(existingSinonimo.get())) {
            sinonimos.add(existingSinonimo.get());
            existingSignificado.get().setSinonimos(sinonimos);
            significadoJpaRepository.save(existingSignificado.get());
        } else {
            throw new IllegalArgumentException("El sinónimo ya está relacionado con este significado");
        }
    }

    // Eliminar un sinónimo de un significado
    public Optional<SignificadoEditable> removeSinonimo(@NonNull SignificadoEditable significado, @NonNull SignificadoEditable sinonimo) throws NonExistentEntityException {
        // Buscar el significado principal por PublicId
        Optional<SignificadoEntity> existingSignificado = significadoJpaRepository.findByPublicId(significado.getPublicId());
        if (existingSignificado.isEmpty()) {
            throw new NonExistentEntityException("El significado no existe");
        }

        // Eliminar el sinónimo si está presente
        List<SignificadoEntity> sinonimos = existingSignificado.get().getSinonimos();
        if (sinonimos.removeIf(s -> s.getPublicId().equals(sinonimo.getPublicId()))) {
            existingSignificado.get().setSinonimos(sinonimos);
            significadoJpaRepository.save(existingSignificado.get());
            return Optional.of(sinonimo);
        }
        return Optional.empty();
    }

    // Añadir un contexto a un significado
    public void addContexto(@NonNull SignificadoEditable significado, @NonNull String contexto) throws NonExistentEntityException {
        // Buscar el significado por PublicId (evitamos múltiples consultas)
        SignificadoEntity significadoEntity = significadoJpaRepository.findByPublicId(significado.getPublicId())
                .orElseThrow(() -> new NonExistentEntityException("El significado no existe"));

        // Buscar el contexto por nombre
        ContextoEntity contextoEntity = contextoJpaRepository.findByContexto(contexto)
                .orElseThrow(() -> new NonExistentEntityException("El contexto no existe"));

        // Añadir el contexto si no está ya relacionado
        List<ContextoEntity> contextos = significadoEntity.getContextos();
        if (!contextos.contains(contextoEntity)) {
            contextos.add(contextoEntity);
            significadoJpaRepository.save(significadoEntity); // Persistimos los cambios solo después de modificar
        }
    }

    // Eliminar un contexto de un significado
    public Optional<Contexto> removeContexto(@NonNull SignificadoEditable significado, @NonNull String contexto) throws NonExistentEntityException {
        // Buscar el significado por PublicId (evitamos múltiples consultas)
        SignificadoEntity significadoEntity = significadoJpaRepository.findByPublicId(significado.getPublicId())
                .orElseThrow(() -> new NonExistentEntityException("El significado no existe"));

        // Buscar el contexto por nombre
        ContextoEntity contextoEntity = contextoJpaRepository.findByContexto(contexto)
                .orElseThrow(() -> new NonExistentEntityException("El contexto no existe"));

        // Eliminar el contexto si está relacionado
        List<ContextoEntity> contextos = significadoEntity.getContextos();
        if (contextos.remove(contextoEntity)) {
            significadoJpaRepository.save(significadoEntity); // Persistimos los cambios después de la eliminación
            return Optional.of(palabraMapper.contextoEntityToModel(contextoEntity));
        }
        return Optional.empty();
    }

    // Añadir un tipo morfológico a un significado
    public void addTipoMorfologico(@NonNull SignificadoEditable significado, @NonNull String tipoMorfologico) throws NonExistentEntityException {
        // Buscar el significado por PublicId
        SignificadoEntity significadoEntity = significadoJpaRepository.findByPublicId(significado.getPublicId())
                .orElseThrow(() -> new NonExistentEntityException("El significado no existe"));

        // Buscar el tipo morfológico por su nombre
        TipoMorfologicoEntity tipoMorfologicoEntity = tipoMorfologicoJpaRepository.findByTipo(tipoMorfologico)
                .orElseThrow(() -> new NonExistentEntityException("El tipo morfológico no existe"));

        // Añadir el tipo morfológico si no está ya relacionado
        List<TipoMorfologicoEntity> tiposMorfologicos = significadoEntity.getTiposMorfologicos();
        if (!tiposMorfologicos.contains(tipoMorfologicoEntity)) {
            tiposMorfologicos.add(tipoMorfologicoEntity);
            significadoEntity.setTiposMorfologicos(tiposMorfologicos);
            significadoJpaRepository.save(significadoEntity); // Persistimos los cambios después de modificar
        }
    }

    // Eliminar un tipo morfológico de un significado
    public Optional<TipoMorfologico> removeTipoMorfologico(@NonNull SignificadoEditable significado, @NonNull String tipoMorfologico) throws NonExistentEntityException {
        // Buscar el significado por PublicId
        SignificadoEntity significadoEntity = significadoJpaRepository.findByPublicId(significado.getPublicId())
                .orElseThrow(() -> new NonExistentEntityException("El significado no existe"));

        // Buscar el tipo morfológico por su nombre
        TipoMorfologicoEntity tipoMorfologicoEntity = tipoMorfologicoJpaRepository.findByTipo(tipoMorfologico)
                .orElseThrow(() -> new NonExistentEntityException("El tipo morfológico no existe"));

        // Eliminar el tipo morfológico si está relacionado
        List<TipoMorfologicoEntity> tiposMorfologicos = significadoEntity.getTiposMorfologicos();
        if (tiposMorfologicos.remove(tipoMorfologicoEntity)) {
            significadoEntity.setTiposMorfologicos(tiposMorfologicos);
            significadoJpaRepository.save(significadoEntity); // Persistimos los cambios después de eliminar
            return Optional.of(palabraMapper.tipoMorfologicoEntityToModel(tipoMorfologicoEntity));
        }
        return Optional.empty();
    }
}

