package au.dispochat.common.exception;


public class RoomNotExistException extends Exception {

    public RoomNotExistException(Long roomId) {
        super("Room with id %d does not exist!".formatted(roomId));
    }
}
