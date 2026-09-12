package ru.yandex.practicum.gym;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.gym.model.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.gym.model.DayOfWeek.*;

public class TimetableCounterOfTrainigsTest {

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
    void testGetCounterOfTrainingsForCoachOneDayDiffTime() {
        TrainingSession thursdayTrainingSessionCoach_1_13_00 = new TrainingSession(GROUP_ADULT, COACH_1, THURSDAY, TIME_13_00);
        TrainingSession thursdayTrainingSessionCoach_2_14_00 = new TrainingSession(GROUP_CHILD, COACH_2, THURSDAY, TIME_14_00);
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
    void testGetCounterOfTrainingsForCoachOneDayOneTime() {
        TrainingSession thursdayTrainingSessionCoach_1_13_00 = new TrainingSession(GROUP_ADULT, COACH_1, THURSDAY, TIME_13_00);
        TrainingSession thursdayTrainingSessionCoach_2_13_00 = new TrainingSession(GROUP_CHILD, COACH_2, THURSDAY, TIME_13_00);
        TrainingSession thursdayTrainingSessionCoach_1_14_00 = new TrainingSession(GROUP_CHILD, COACH_1, THURSDAY, TIME_14_00);
        TrainingSession thursdayTrainingSessionCoach_2_14_00 = new TrainingSession(GROUP_CHILD, COACH_2, THURSDAY, TIME_14_00);
        TrainingSession thursdayTrainingSessionCoach_1_20_00 = new TrainingSession(GROUP_ADULT, COACH_1, THURSDAY, TIME_20_00);

        timetable.addNewTrainingSession(thursdayTrainingSessionCoach_1_13_00);
        timetable.addNewTrainingSession(thursdayTrainingSessionCoach_1_14_00);
        timetable.addNewTrainingSession(thursdayTrainingSessionCoach_1_20_00);
        timetable.addNewTrainingSession(thursdayTrainingSessionCoach_2_13_00);
        timetable.addNewTrainingSession(thursdayTrainingSessionCoach_2_14_00);

        List<CounterOfTrainings> actualCounterOfTrainings = timetable.getCountByCoaches();
        List<CounterOfTrainings> expectedCounterOfTrainings = List.of(new CounterOfTrainings(COACH_1, 3),
                new CounterOfTrainings(COACH_2, 2));

        assertIterableEquals(expectedCounterOfTrainings, actualCounterOfTrainings);

    }

    @Test
    void testGetCounterOfTrainingsForCoachDiffDayAndTime() {
        TrainingSession mondayTrainingSessionCoach_1_13_00 = new TrainingSession(GROUP_ADULT, COACH_1, MONDAY, TIME_13_00);
        TrainingSession saturdayTrainingSessionCoach_1_14_00 = new TrainingSession(GROUP_CHILD, COACH_1, SATURDAY, TIME_14_00);
        TrainingSession fridayTrainingSessionCoach_1_10_00 = new TrainingSession(GROUP_ADULT, COACH_2, FRIDAY, TIME_10_00);
        TrainingSession fridayTrainingSessionCoach_2_10_00 = new TrainingSession(GROUP_ADULT, COACH_2, FRIDAY, TIME_13_00);
        TrainingSession thursdayTrainingSessionCoach_2_13_00 = new TrainingSession(GROUP_CHILD, COACH_2, THURSDAY, TIME_13_00);

        timetable.addNewTrainingSession(mondayTrainingSessionCoach_1_13_00);
        timetable.addNewTrainingSession(saturdayTrainingSessionCoach_1_14_00);
        timetable.addNewTrainingSession(fridayTrainingSessionCoach_1_10_00);
        timetable.addNewTrainingSession(fridayTrainingSessionCoach_2_10_00);
        timetable.addNewTrainingSession(thursdayTrainingSessionCoach_2_13_00);

        List<CounterOfTrainings> actualCounterOfTrainings = timetable.getCountByCoaches();
        List<CounterOfTrainings> expectedCounterOfTrainings = List.of(new CounterOfTrainings(COACH_2, 3),
                new CounterOfTrainings(COACH_1, 2));

        assertIterableEquals(expectedCounterOfTrainings, actualCounterOfTrainings);
    }
}
