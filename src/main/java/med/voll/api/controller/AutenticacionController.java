package med.voll.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import med.voll.api.domain.usuarios.DatosAutenticacionUsuario;
import med.voll.api.domain.usuarios.Usuario;
import med.voll.api.infra.security.DatosJwtToken;
import med.voll.api.infra.security.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class AutenticacionController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @PostMapping
    public ResponseEntity autenticacionUsuario(@RequestBody @Valid DatosAutenticacionUsuario datosAutenticacionUsuario) {
        Authentication autenticationToken = new UsernamePasswordAuthenticationToken(datosAutenticacionUsuario.login(), datosAutenticacionUsuario.clave());
        var usuarioAutenticado =  authenticationManager.authenticate(autenticationToken);
        var jwtToken = tokenService.generarTokenJwt((Usuario) usuarioAutenticado.getPrincipal());
        return ResponseEntity.ok(new DatosJwtToken(jwtToken));
    }
}
