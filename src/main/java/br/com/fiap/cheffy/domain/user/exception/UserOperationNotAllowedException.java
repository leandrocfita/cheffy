package br.com.fiap.cheffy.domain.user.exception;

public class UserOperationNotAllowedException extends RuntimeException{
    public UserOperationNotAllowedException(String message){
        super(message);
    }
}
