package au.dispochat.common.exception;


public class AlreadyRegisteredException extends Exception {

    public AlreadyRegisteredException() {
        super("You are already registered!");
    }
}
