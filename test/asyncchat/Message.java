package asyncchat;

public class Message {
 
    public final String message;
    public final String username;
 
    public Message(final String message, final String username) {
        this.message = message;
        this.username = username;
    }
}
