package com.vacinas.ap1.exceptions;

import java.io.Serial;

public class LoteRepetidoException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public LoteRepetidoException(String mensagem) {
        super(mensagem);
    }
}