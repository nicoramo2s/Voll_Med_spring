package med.voll.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import med.voll.api.domain.consulta.AgendaDeConsultaService;
import med.voll.api.domain.consulta.DatosAgendarConsulta;
import med.voll.api.domain.consulta.DatosDetalleConsulta;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@ResponseBody
@RequiredArgsConstructor
@RequestMapping("/consultas")
public class ConsultaController {

    private final AgendaDeConsultaService consultaService;

    @PostMapping
    @Transactional
    public ResponseEntity agendar(@RequestBody @Valid DatosAgendarConsulta datos) {
        System.out.println(datos);

        consultaService.agendar(datos);

        return ResponseEntity.ok(new DatosDetalleConsulta(null, null, null, null));
    }
}
