package br.com.fiap.cheffy.presentation.controller;

import br.com.fiap.cheffy.application.user.dto.LoginResultPort;
import br.com.fiap.cheffy.domain.user.port.input.LoginInput;
import br.com.fiap.cheffy.presentation.dto.LoginRequestDTO;
import br.com.fiap.cheffy.presentation.dto.TokenResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final LoginInput loginInput;

    public AuthController(LoginInput loginInput) {

        this.loginInput = loginInput;
    }

    @PostMapping("/login")
    @Operation(
            summary = "Autenticar usuário",
            description = "Realiza autenticação do usuário e retorna token JWT para acesso aos recursos protegidos"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Autenticação realizada com sucesso - Retorna token de acesso",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TokenResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Dados de login inválidos ou malformados"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas - Usuário ou senha incorretos"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    public LoginResultPort login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        return loginInput.execute(loginRequestDTO.login(), loginRequestDTO.password());
    }
}
