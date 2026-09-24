package ru.incubator.security.provisioning;

public class NoSuchUserException extends SecurityProvisioningException {
    public NoSuchUserException(String message) {
        super(message);
    }
}
