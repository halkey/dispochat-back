package au.dispochat.common.exception;


public class AlreadyOwnerException extends Exception {

    public AlreadyOwnerException(Long roomId) {
        super("You are already own a chat room with id %d.".formatted(roomId));
    }
}
