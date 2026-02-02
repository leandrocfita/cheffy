package br.com.fiap.cheffy.domain.profile.port.input;

public interface PasswordEncoderPort {

    String encode(CharSequence rawPassword);
}
