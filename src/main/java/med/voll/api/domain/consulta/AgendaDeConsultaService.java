package med.voll.api.domain.consulta;

import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.PacienteRepository;
import med.voll.api.infra.errores.ValidacionDeIntegridad;
import org.springframework.stereotype.Service;

@Service
public class AgendaDeConsultaService {

    private final ConsultaRepository consultaRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;

    public AgendaDeConsultaService(ConsultaRepository consultaRepository, PacienteRepository pacienteRepository, MedicoRepository medicoRepository) {
        this.consultaRepository = consultaRepository;
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
    }


    public void agendar(DatosAgendarConsulta datos) {
        if (pacienteRepository.findById(datos.idPaciente()).isPresent()) {
            throw new ValidacionDeIntegridad("Id de paciente no encontrado");
        }
        if (datos.idMedico() != null && medicoRepository.existsById(datos.idMedico())) {
            throw new ValidacionDeIntegridad("Id de medico no encontrado");

        }
        var paciente = pacienteRepository.findById(datos.idPaciente()).get();

        var medico = seleccionarMedico(datos);

        var consulta = new Consulta(null, medico, paciente, datos.fecha());

        consultaRepository.save(consulta);
    }

    private Medico seleccionarMedico(DatosAgendarConsulta datos) {
        if (datos.idMedico() != null) {
            return medicoRepository.getReferenceById(datos.idMedico());
        }
        if (datos.especialidad() == null) {
            throw new ValidacionDeIntegridad("Debe seleccionar una especialidad para el medico");
        }
        return medicoRepository.seleccionarMedicoConEspecialidadEnFecha(datos.especialidad(), datos.fecha());
    }
}
