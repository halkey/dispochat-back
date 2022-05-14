package au.dispochat.common.exception;


public class AlreadyRequestedToJoinException extends Exception {

    public AlreadyRequestedToJoinException(Long roomId) {
        super("You have already requested to join a chat room with id %d as a guest.".formatted(roomId));
    }
}
