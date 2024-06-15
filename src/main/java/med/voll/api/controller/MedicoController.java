package med.voll.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import med.voll.api.domain.direccion.DatosDireccion;
import med.voll.api.domain.medico.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/medicos")
@RequiredArgsConstructor
public class MedicoController {

    private final MedicoRepository medicoRepository;

    @PostMapping
    public ResponseEntity<DatosRespuestaMedico> registrarMedicos(@RequestBody @Valid DatosRegistroMedico datosRegistroMedico, UriComponentsBuilder uriComponentsBuilder) {
        Medico medico = medicoRepository.save(new Medico(datosRegistroMedico));
        DatosRespuestaMedico datosRespuestaMedico = new DatosRespuestaMedico(medico.getId(), medico.getNombre(), medico.getEmail(), medico.getTelefono(), medico.getDocumento(), medico.getEspecialidad().toString(),
                new DatosDireccion(medico.getDireccion().getCalle(),
                        medico.getDireccion().getDistrito(),
                        medico.getDireccion().getCiudad(),
                        medico.getDireccion().getNumero(),
                        medico.getDireccion().getComplemento()));
        URI url = uriComponentsBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();
        return ResponseEntity.created(url).body(datosRespuestaMedico);
        // Return 201 created
        // URL donde encontrar al medico
        // http://localhost:8080/medico/xx

    }

    @GetMapping
    public ResponseEntity<Page<DatosListadoMedicos>> listarMedicos(@PageableDefault(size = 10) Pageable paginacion) {
        return ResponseEntity.ok(medicoRepository.findByActivoTrue(paginacion).map(DatosListadoMedicos::new));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaMedico> retornarDatosMedico(@PathVariable Long id) {
        Medico medico = medicoRepository.getReferenceById(id);
        var datosMedico = new DatosRespuestaMedico(medico.getId(), medico.getNombre(), medico.getEmail(), medico.getTelefono(), medico.getDocumento(), medico.getEspecialidad().toString(),
                new DatosDireccion(medico.getDireccion().getCalle(),
                        medico.getDireccion().getDistrito(),
                        medico.getDireccion().getCiudad(),
                        medico.getDireccion().getNumero(),
                        medico.getDireccion().getComplemento()));
        return ResponseEntity.ok(datosMedico);
    }

    @PutMapping
    @Transactional
    public ResponseEntity actualizarMedicos(@RequestBody DatosActualizarMedicos datosActualizarMedicos) {
        Medico medico = medicoRepository.getReferenceById(datosActualizarMedicos.id());
        medico.actualizarDatos(datosActualizarMedicos);
        return ResponseEntity.ok(new DatosRespuestaMedico(medico.getId(), medico.getNombre(), medico.getEmail(), medico.getTelefono(), medico.getDocumento(), medico.getEspecialidad().toString(),
                new DatosDireccion(medico.getDireccion().getCalle(),
                        medico.getDireccion().getDistrito(),
                        medico.getDireccion().getCiudad(),
                        medico.getDireccion().getNumero(),
                        medico.getDireccion().getComplemento())));
    }

    // DELETE LOGICO
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity eliminarMedico(@PathVariable Long id) {
        Medico medico = medicoRepository.getReferenceById(id);
        medico.desactivarMedico();
        return ResponseEntity.noContent().build();
    }
//    DELETE EN LA BASE DE DATOS
//    @DeleteMapping("/{id}")
//    @Transactional
//    public String deleteMedico(@PathVariable Long id) {
//        Medico medico = medicoRepository.getReferenceById(id);
//        medicoRepository.delete(medico);
//        return "Se elimino al medico/a: " + medico.getNombre();
//    }
}
