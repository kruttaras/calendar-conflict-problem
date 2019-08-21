import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Event {
    private int startTime;
    private int endTime;
    private String description;
}
