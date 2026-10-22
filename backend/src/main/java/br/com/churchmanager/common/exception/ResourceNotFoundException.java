package br.com.churchmanager.common.exception;

/**
 * Lançada quando uma entidade solicitada não existe.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resource, Object id) {
        super("%s com id '%s' não encontrado(a)".formatted(resource, id));
    }
}
