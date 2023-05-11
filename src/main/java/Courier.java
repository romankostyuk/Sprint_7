import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Courier {
    private String login;
    private String password;
    private String firstName;
}
