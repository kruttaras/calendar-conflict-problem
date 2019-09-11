import static org.hamcrest.CoreMatchers.is;

import java.util.Arrays;
import java.util.List;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

public class CalendarToolTest {

    /**
     *  { 1, 2, "a"}
     *  { 3, 4, "b"}
     *  { 6, 7, "c"}
     */
    @Test
    public void testNoConflict() {
        List<Event> events = Arrays.asList(
                new Event(1, 2, "a"),
                new Event(3, 4, "b"),
                new Event(6, 7, "c"));

        List<List<Event>> conflicts = CalendarTool.findConflicts(events);

        Assert.assertThat(conflicts.size(), is(0));
    }


    /**
     *  { 1, 2, "a"}
     *  { 3, 4, "b"}
     *  { 6, 7, "c"}
     *  { 7, 14, "d"} ┐ conflicting
     *  { 7, 15, "e"} ┘ events
     */
    @Test
    public void testSingleConflict() {
        Event conflictingEvent1FromSequence1 = new Event(7, 14, "d");
        Event conflictingEvent2FromSequence1 = new Event(7, 15, "e");
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

    /**
     *  { 1, 2, "a"}
     *  { 3, 4, "b"}
     *  { 7, 10, "e"}  ┐conflicting
     *  { 8, 11, "d"}  │events set
     *  { 10, 12, "f"} ┘
     *  { 13, 14, "x"}
     */
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

    /**
     *  { 1, 2, "a"}
     *  { 3, 5, "b"}   ┐conflicting
     *  { 4, 6, "c"}   ┘events set #1
     *  { 7, 10, "d"}  ┐conflicting
     *  { 8, 11, "e"}  │events set #2
     *  { 10, 12, "f"} ┘
     *  { 13, 14, "g"} ┐conflicting
     *  { 13, 14, "h"} ┘events set #3
     */
    @Test
    public void testComplexConflict() {
        List<Event> events = Arrays.asList(
                new Event(1, 2, "a"),
                new Event(3, 5, "b"),
                new Event(4, 6, "c"),

                new Event(7, 10, "d"),
                new Event(8, 11, "e"),
                new Event(10, 12, "f"),

                new Event(13, 14, "g"),
                new Event(13, 14, "h"));

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
