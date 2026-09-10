package ru.yandex.practicum.gym;

import ru.yandex.practicum.gym.model.DayOfWeek;
import ru.yandex.practicum.gym.model.TimeOfDay;
import ru.yandex.practicum.gym.model.TrainingSession;

import java.util.*;

public class Timetable {

    private final Map<DayOfWeek, TreeMap<TimeOfDay, ArrayList<TrainingSession>>> timetable;

    public Timetable() {
        this.timetable = new HashMap<>(7);
    }

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek dayOfWeek = trainingSession.getDayOfWeek();
        TimeOfDay timeOfDay = trainingSession.getTimeOfDay();
        TreeMap<TimeOfDay, ArrayList<TrainingSession>> timetableDay;
        ArrayList<TrainingSession> trainingSessions;
        if (timetable.containsKey(dayOfWeek)) {
            timetableDay = timetable.get(dayOfWeek);
            trainingSessions = timetableDay.getOrDefault(timeOfDay, new ArrayList<>());
        } else {
            timetableDay = new TreeMap<>();
            trainingSessions = new ArrayList<>();
        }
        trainingSessions.add(trainingSession);
        timetableDay.put(timeOfDay, trainingSessions);
        timetable.put(dayOfWeek, timetableDay);
    }

    public TreeMap<TimeOfDay, ArrayList<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        return timetable.get(dayOfWeek);
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        //как реализовать, тоже непонятно, но сложность должна быть О(1)
        if (!timetable.containsKey(dayOfWeek)) {
            return null;
        }
        return timetable.get(dayOfWeek).get(timeOfDay);
    }


}
