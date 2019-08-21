import static org.hamcrest.CoreMatchers.is;

import java.util.Arrays;
import java.util.List;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

public class CalendarToolTest {

    @Test
    public void testNoConflict() {
        List<Event> events = Arrays.asList(
                new Event(1, 2, "a"),
                new Event(3, 4, "b"),
                new Event(6, 7, "c"));

        List<List<Event>> conflicts = CalendarTool.findConflicts(events);

        Assert.assertThat(conflicts.size(), is(0));
    }

    @Test
    public void testSingleConflict() {
        Event conflictingEvent1FromSequence1 = new Event(7, 14, "b");
        Event conflictingEvent2FromSequence1 = new Event(7, 15, "b");
        List<Event> events = Arrays.asList(
                new Event(1, 2, "a"),
                new Event(3, 4, "b"),
                new Event(6, 7, "c"),
                conflictingEvent1FromSequence1,
                conflictingEvent2FromSequence1);

        List<List<Event>> conflicts = CalendarTool.findConflicts(events);

        Assert.assertThat(conflicts.size(), is(1));

        List<Event> conflictSet1 = conflicts.get(0);

        Assert.assertThat(conflictSet1.size(), is(2));
        Assert.assertThat(conflictSet1, CoreMatchers.hasItems(conflictingEvent1FromSequence1, conflictingEvent2FromSequence1));
    }

    @Test
    public void testSequenceConflict() {
        Event conflictingEvent1FromSequence1 = new Event(7, 10, "e");
        Event conflictingEvent2FromSequence1 = new Event(8, 11, "d");
        Event conflictingEvent3FromSequence1 = new Event(10, 12, "f");
        List<Event> events = Arrays.asList(
                new Event(1, 2, "a"),
                new Event(3, 4, "b"),
                conflictingEvent1FromSequence1,
                conflictingEvent2FromSequence1,
                conflictingEvent3FromSequence1,
                new Event(13, 14, "x"));

        List<List<Event>> conflicts = CalendarTool.findConflicts(events);

        Assert.assertThat(conflicts.size(), is(1));

        List<Event> conflictSet1 = conflicts.get(0);
        Assert.assertThat(conflictSet1.size(), is(3));
        Assert.assertThat(conflictSet1, CoreMatchers.hasItems(
                conflictingEvent1FromSequence1,
                conflictingEvent2FromSequence1,
                conflictingEvent3FromSequence1));
    }

    @Test
    public void testComplexConflict() {
        List<Event> events = Arrays.asList(
                new Event(1, 2, "a"),
                new Event(3, 5, "b conflict#1"),
                new Event(4, 6, "c conflict#1"),

                new Event(7, 10, "d conflict#2"),
                new Event(8, 11, "e conflict#2"),
                new Event(10, 12, "f conflict#2"),

                new Event(13, 14, "g conflict#3"),
                new Event(13, 14, "h conflict#3"));

        List<List<Event>> conflicts = CalendarTool.findConflicts(events);

        Assert.assertThat(conflicts.size(), is(3));

        List<Event> conflictSet1 = conflicts.get(0);
        Assert.assertThat(conflictSet1.size(), is(2));
        Assert.assertThat(conflictSet1, CoreMatchers.hasItems(events.get(1), events.get(2)));

        List<Event> conflictSet2 = conflicts.get(1);
        Assert.assertThat(conflictSet2.size(), is(3));
        Assert.assertThat(conflictSet2, CoreMatchers.hasItems(events.get(3), events.get(4), events.get(5)));


        List<Event> conflictSet3 = conflicts.get(2);
        Assert.assertThat(conflictSet3.size(), is(2));
        Assert.assertThat(conflictSet3, CoreMatchers.hasItems(events.get(6), events.get(7)));

    }


}
