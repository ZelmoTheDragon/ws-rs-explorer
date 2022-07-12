package com.github.happi.explorer;

import jakarta.validation.Validation;

/**
 * A validator class facility using <i>Bean Validation</i>.
 */
public final class Validations {

    /**
     * Internal constructor.
     * Instance not allowed.
     */
    private Validations() {
        throw new UnsupportedOperationException("Instance not allowed");
    }

    /**
     * Validate any object using <i>Bean Validation</i>.
     *
     * @param bean Any object to valide
     * @throws ValidationException if the parameter is invalid
     */
    public static void validate(final Object bean) {

        var validatorFactory = Validation.buildDefaultValidatorFactory();

        try (validatorFactory) {
            var validator = validatorFactory.getValidator();
            var violations = validator.validate(bean);

            if (!violations.isEmpty()) {
                var typeName = bean.getClass().getSimpleName();
                var exception = new ValidationException(
                        "Validation failed for object type : " + typeName + " ",
                        violations
                );

                for (var v : violations) {
                    var p = String.valueOf(v.getPropertyPath());
                    var m = v.getMessage();
                    var ex = new ValidationException("Field: [" + p + "], details: " + m);
                    exception.addSuppressed(ex);
                }

                throw exception;
            }
        }
    }

}
