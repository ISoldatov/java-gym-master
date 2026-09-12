package ru.yandex.practicum.gym;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static ru.yandex.practicum.gym.model.DayOfWeek.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.gym.model.*;

import java.util.*;

public class TimetableTestGetTrainingsSessions {

    private static Timetable timetable;
    public static final Group GROUP_CHILD = new Group("Акробатика для детей", Age.CHILD, 60);
    public static final Group GROUP_ADULT = new Group("Акробатика для взрослых", Age.ADULT, 90);
    public static final Coach COACH_1 = new Coach("Васильев", "Николай", "Сергеевич");
    public static final Coach COACH_2 = new Coach("Петрова", "Валентина", "Ивановна");
    public static final TimeOfDay TIME_10_00 = new TimeOfDay(10, 0);
    public static final TimeOfDay TIME_13_00 = new TimeOfDay(13, 0);
    public static final TimeOfDay TIME_14_00 = new TimeOfDay(14, 0);
    public static final TimeOfDay TIME_20_00 = new TimeOfDay(20, 0);

    @BeforeEach
    void beforeEach() {
        timetable = new Timetable();
    }

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        TrainingSession singleTrainingSession = new TrainingSession(GROUP_CHILD, COACH_1, MONDAY, TIME_13_00);
        timetable.addNewTrainingSession(singleTrainingSession);
        //Проверить, что за понедельник вернулось одно занятие
        assertEquals(1, timetable.getTrainingSessionsForDay(MONDAY).get(TIME_13_00).size());
        //Проверить, что за вторник не вернулось занятий
        assertNull(timetable.getTrainingSessionsForDay(TUESDAY));
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(GROUP_ADULT, COACH_2, THURSDAY, TIME_20_00);
        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        TrainingSession mondayChildTrainingSession = new TrainingSession(GROUP_CHILD, COACH_1, MONDAY, TIME_13_00);
        TrainingSession thursdayChildTrainingSession = new TrainingSession(GROUP_CHILD, COACH_1, THURSDAY, TIME_13_00);
        TrainingSession saturdayChildTrainingSession = new TrainingSession(GROUP_CHILD, COACH_1, SATURDAY, TIME_13_00);
        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        assertEquals(1, timetable.getTrainingSessionsForDay(MONDAY).get(TIME_13_00).size());

        TrainingSession actualMondayChildTrainingSession = timetable.getTrainingSessionsForDay(MONDAY).get(TIME_13_00).getFirst();
        assertEquals(mondayChildTrainingSession, actualMondayChildTrainingSession);

        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        assertEquals(2, timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY).size());

        Set<TimeOfDay> expectedSortedTime = new LinkedHashSet<>(2);
        expectedSortedTime.add(thursdayChildTrainingSession.getTimeOfDay());
        expectedSortedTime.add(thursdayAdultTrainingSession.getTimeOfDay());
        TreeMap<TimeOfDay, ArrayList<TrainingSession>> actualThursdayTrainingSession = timetable.getTrainingSessionsForDay(THURSDAY);
        assertIterableEquals(expectedSortedTime, actualThursdayTrainingSession.navigableKeySet());

        // Проверить, что за вторник не вернулось занятий
        assertNull(timetable.getTrainingSessionsForDay(TUESDAY));
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        TrainingSession singleTrainingSession = new TrainingSession(GROUP_CHILD, COACH_1, MONDAY, TIME_13_00);
        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        assertEquals(1, timetable.getTrainingSessionsForDayAndTime(MONDAY, TIME_13_00).size());

        List<TrainingSession> actualSingleTrainingSessionList = timetable.getTrainingSessionsForDayAndTime(MONDAY, TIME_13_00);
        assertEquals(singleTrainingSession, actualSingleTrainingSessionList.getFirst());

        //Проверить, что за понедельник в 14:00 не вернулось занятий
        assertNull(timetable.getTrainingSessionsForDayAndTime(MONDAY, TIME_14_00));
    }

    @Test
    void testGetTrainingSessionsForDayAndTimeMultipleSessions() {
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(GROUP_ADULT, COACH_1, THURSDAY, TIME_20_00);
        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        TrainingSession thursdayChildTrainingSession = new TrainingSession(GROUP_CHILD, COACH_1, THURSDAY, TIME_20_00);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);

        //Проверить, что за четверг в 20.00 вернулось два занятия
        List<TrainingSession> expectedMultiTrainingSection = List.of(thursdayAdultTrainingSession, thursdayChildTrainingSession);
        assertIterableEquals(expectedMultiTrainingSection, timetable.getTrainingSessionsForDayAndTime(THURSDAY, TIME_20_00));
    }

    @Test
    void testGetCounterOfTrainingsForMultipleCoachOneDayDiffTime() {
        TrainingSession thursdayTrainingSessionCoach_1_13_00 = new TrainingSession(GROUP_ADULT, COACH_1, THURSDAY, TIME_13_00);
        TrainingSession thursdayTrainingSessionCoach_2_14_00 = new TrainingSession(GROUP_ADULT, COACH_2, THURSDAY, TIME_14_00);
        TrainingSession thursdayTrainingSessionCoach_2_20_00 = new TrainingSession(GROUP_ADULT, COACH_2, THURSDAY, TIME_20_00);
        timetable.addNewTrainingSession(thursdayTrainingSessionCoach_1_13_00);
        timetable.addNewTrainingSession(thursdayTrainingSessionCoach_2_14_00);
        timetable.addNewTrainingSession(thursdayTrainingSessionCoach_2_20_00);

        List<CounterOfTrainings> actualCounterOfTrainings = timetable.getCountByCoaches();
        List<CounterOfTrainings> expectedCounterOfTrainings = List.of(new CounterOfTrainings(COACH_2, 2),
                new CounterOfTrainings(COACH_1, 1));

        assertIterableEquals(expectedCounterOfTrainings, actualCounterOfTrainings);
    }


    @Test
    void testGetCounterOfTrainingsForMultipleCoachOneDayOneTime() {
        TrainingSession thursdayTrainingSessionCoach_1_13_00 = new TrainingSession(GROUP_ADULT, COACH_1, THURSDAY, TIME_13_00);
        TrainingSession thursdayTrainingSessionCoach_2_14_00 = new TrainingSession(GROUP_ADULT, COACH_2, THURSDAY, TIME_14_00);
        TrainingSession thursdayTrainingSessionCoach_2_20_00 = new TrainingSession(GROUP_ADULT, COACH_2, THURSDAY, TIME_20_00);
        timetable.addNewTrainingSession(thursdayTrainingSessionCoach_1_13_00);
        timetable.addNewTrainingSession(thursdayTrainingSessionCoach_2_14_00);
        timetable.addNewTrainingSession(thursdayTrainingSessionCoach_2_20_00);

        List<CounterOfTrainings> actualCounterOfTrainings = timetable.getCountByCoaches();
        List<CounterOfTrainings> expectedCounterOfTrainings = List.of(new CounterOfTrainings(COACH_2, 2),
                new CounterOfTrainings(COACH_1, 1));

        assertIterableEquals(expectedCounterOfTrainings, actualCounterOfTrainings);
    }
}
