package au.dispochat.common.exception;


public class AlreadyGuestException extends Exception {

    public AlreadyGuestException(Long roomId) {
        super("You are already in a chat room with id %d as a guest.".formatted(roomId));
    }
}
