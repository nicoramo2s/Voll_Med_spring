package med.voll.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import med.voll.api.domain.paciente.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteRepository pacienteRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<DatosDetalladoPaciente> registrar(@RequestBody @Valid DatosRegistroPaciente datos, UriComponentsBuilder uriComponentsBuilder) {
        Paciente paciente = pacienteRepository.save(new Paciente(datos));
        URI url = uriComponentsBuilder.path("/pacientes/{id}").buildAndExpand(paciente.getId()).toUri();
        return ResponseEntity.created(url).body(new DatosDetalladoPaciente(paciente));
    }

    @GetMapping
    public ResponseEntity<Page<DatosListaPaciente>> listarPacientes(
            @PageableDefault(page = 0, size = 10, sort = {"nombre"}) Pageable paginacion) {
        var page = pacienteRepository.findByActivoTrue(paginacion).map(DatosListaPaciente::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    public ResponseEntity<DatosDetalladoPaciente> actualizar(@RequestBody @Valid DatosActualizacionPaciente datos) {
        var paciente = pacienteRepository.getReferenceById(datos.id());
        paciente.actualizarInformacion(datos);
        return ResponseEntity.ok(new DatosDetalladoPaciente(paciente));

    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity remover(@PathVariable Long id) {
        var paciente = pacienteRepository.getReferenceById(id);
        paciente.eliminar();
        return ResponseEntity.noContent().build();

    }

}