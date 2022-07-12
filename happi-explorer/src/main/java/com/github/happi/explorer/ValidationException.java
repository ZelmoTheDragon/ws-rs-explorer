package com.github.happi.explorer;

import java.io.Serial;
import java.util.Set;
import jakarta.validation.ConstraintViolation;

public class ValidationException extends ExplorerException {


    /**
     * Serial number.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Violations rules.
     */
    private final Set<ConstraintViolation<Object>> violations;

    /**
     * {@inheritDoc}
     */
    public ValidationException(final String message) {
        super(message);
        this.violations = Set.of();
    }

    /**
     * Construct an exception for validation object.
     *
     * @param message    Validation message
     * @param violations Violations rules
     */
    public ValidationException(final String message, final Set<ConstraintViolation<Object>> violations) {
        super(message);
        this.violations = Set.copyOf(violations);
    }

    // Getters...

    public Set<ConstraintViolation<?>> getViolations() {
        return Set.copyOf(violations);
    }
}
