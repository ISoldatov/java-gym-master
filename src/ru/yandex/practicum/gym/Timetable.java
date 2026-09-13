package ru.yandex.practicum.gym;

import ru.yandex.practicum.gym.model.*;

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
        if (!timetable.containsKey(dayOfWeek)) {
            return null;
        }
        return timetable.get(dayOfWeek).get(timeOfDay);
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        Map<Coach, Integer> countByCoaches = new HashMap<>();
        for (TreeMap<TimeOfDay, ArrayList<TrainingSession>> dayTrainingSessions : timetable.values()) {
            for (ArrayList<TrainingSession> timeTrainingSessions : dayTrainingSessions.values()) {
                for (TrainingSession ts : timeTrainingSessions) {
                    Coach coach = ts.getCoach();
                    if (countByCoaches.containsKey(coach)) {
                        countByCoaches.compute(coach, (c, i) -> i + 1);
                    } else {
                        countByCoaches.put(coach, 1);
                    }
                }
            }
        }

        ArrayList<CounterOfTrainings> counterOfTrainings = new ArrayList<>(countByCoaches.size());
        for (Map.Entry<Coach, Integer> entry : countByCoaches.entrySet()) {
            counterOfTrainings.add(new CounterOfTrainings(entry.getKey(), entry.getValue()));
        }
        counterOfTrainings.sort(Collections.reverseOrder());
        return counterOfTrainings;
    }
}
