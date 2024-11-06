public class ServicioAnimalitosImpl{
    private RepositorioAnimalitos repositorioAnimalitos;
    private ServicioEmilios servicioEmilios;
    public ServicioAnimalitosImpl(RepositorioAnimalitos repositorioAnimalitos,
                                  ServicioEmilios servicioEmilios){
        this.repositorioAnimalitos = repositorioAnimalitos;
        this.servicioEmilios = servicioEmilios;
    }

    public Animalito altaAnimalito(DatosDelAnimalito datos){
        servicioEmilios.enviarCorreo("subs@fermin.com",
                                    "Nuevo animalito",
                                    "Se ha dado de alta un nuevo animalito: " + datos.getNombre()); 
        Animalito animalito =  Animalito.builder()
                                .nombre(datos.getNombre())
                                .edad(datos.getEdad())
                                .build();
        return repositorioAnimalitos.guardar(animalito);

    }
}
public interface RepositorioAnimalitos{
    public Animalito guardar(Animalito animalito);
}
public class RepositorioAnimalitosMock implements RepositorioAnimalitos{

    @Getter private Animalito datosConLosQueDeberianLlamarte;
    @Getter private teHanLlamado = false;

    public RepositorioAnimalitosMock(Animalito datosConLosQueDeberianLlamarte){
        this.datosConLosQueDeberianLlamarte = datosConLosQueDeberianLlamarte;
    }

    public Animalito guardar(Animalito animalito){
        teHanLlamado = true;
        if(animalito != datosConLosQueDeberianLlamarte){
            throw new RuntimeException("No se llamó con los datos esperados");
        }
        return Animalito.builder()
                .id(33)
                .nombre(animalito.getNombre())
                .edad(animalito.getEdad())
                .build();
    }
}
public interface ServicioEmilios {
    public void enviarCorreo(String destinatario, String asunto, String cuerpo);
}
public class ServicioEmiliosSpy implementsServicioEmilios {
    @Getter private String destinatario;
    @Getter private String asunto;
    @Getter private String cuerpo;

    public void enviarCorreo(String destinatario, String asunto, String cuerpo){
        this.destinatario = destinatario;
        this.asunto = asunto;
        this.cuerpo = cuerpo;
    }
}
---
DatosDelAnimalito datos = DatosDelAnimalito.builder()
                            .nombre("Firulais")
                            .edad(3)
                            .build();
Animalito datosQueDebeRecibirElMock = Animalito.builder()
.nombre("Firulais")
.edad(3)
.build();
RepositorioAnimalitosMock repositorioAnimalitos = new RepositorioAnimalitosMock(datosQueDebeRecibirElMock);
ServicioEmiliosSpy servicioEmilios = new ServicioEmiliosSpy();
ServicioAnimalitos servicioAnimalitos = new ServicioAnimalitosImpl(repositorioAnimalitos, servicioEmilios);
Animalito animalito = servicioAnimalitos.altaAnimalito(datos);
Assertions.assertEquals("Firulais", animalito.getNombre());
Assertions.assertEquals(3, animalito.getEdad());
Assertions.assertEquals(33, animalito.getId());

Assertions.assertEquals("subs@fermin.com", servicioEmilios.getDestinatario());
Assertions.assertEquals("Nuevo animalito", servicioEmilios.getAsunto());
Assertions.assertTrue(servicioEmilios.getCuerpo().contains("Firulais"));

Assertions.assertTrue(repositorioAnimalitos.isTeHanLlamado());