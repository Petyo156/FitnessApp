package com.softuni.project.workoutschedule.service;

import com.softuni.project.common.DayOfWeek;
import com.softuni.project.program.model.Program;
import com.softuni.project.web.dto.ProgramFormRequest;
import com.softuni.project.workout.model.Workout;
import com.softuni.project.workout.service.WorkoutService;
import com.softuni.project.workoutschedule.model.WorkoutSchedule;
import com.softuni.project.workoutschedule.repository.WorkoutScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WorkoutScheduleService {
    private final WorkoutScheduleRepository workoutScheduleRepository;
    private final WorkoutService workoutService;

    @Autowired
    public WorkoutScheduleService(WorkoutScheduleRepository workoutScheduleRepository, WorkoutService workoutService) {
        this.workoutScheduleRepository = workoutScheduleRepository;
        this.workoutService = workoutService;
    }

    public void createWorkoutSchedules(ProgramFormRequest programFormRequest, Program program) {
        Map<DayOfWeek, String> workoutDays = getWorkoutDaysMap(programFormRequest);

        int countCreatedSchedules = 0;

        for (Map.Entry<DayOfWeek, String> entry : workoutDays.entrySet()) {
            DayOfWeek day = entry.getKey();
            String workoutId = entry.getValue();

            if (workoutId != null) {
                Workout workout = workoutService.findById(UUID.fromString(workoutId));
                createScheduleForDay(day, workout, program);
                countCreatedSchedules++;
            }
        }

        if (countCreatedSchedules == 0) {
            throw new RuntimeException("No workout schedules, set at least one workout day");
        }
    }

    private void createScheduleForDay(DayOfWeek day, Workout workout, Program program) {
        WorkoutSchedule workoutSchedule = initializeWorkoutSchedule(day, workout, program);
        workoutScheduleRepository.save(workoutSchedule);
    }

    private Map<DayOfWeek, String> getWorkoutDaysMap(ProgramFormRequest programFormRequest) {
        Map<DayOfWeek, String> workoutDays = new HashMap<>();
        workoutDays.put(DayOfWeek.MONDAY, programFormRequest.getMondayWorkoutId());
        workoutDays.put(DayOfWeek.TUESDAY, programFormRequest.getTuesdayWorkoutId());
        workoutDays.put(DayOfWeek.WEDNESDAY, programFormRequest.getWednesdayWorkoutId());
        workoutDays.put(DayOfWeek.THURSDAY, programFormRequest.getThursdayWorkoutId());
        workoutDays.put(DayOfWeek.FRIDAY, programFormRequest.getFridayWorkoutId());
        workoutDays.put(DayOfWeek.SATURDAY, programFormRequest.getSaturdayWorkoutId());
        workoutDays.put(DayOfWeek.SUNDAY, programFormRequest.getSundayWorkoutId());
        return workoutDays;
    }

    private WorkoutSchedule initializeWorkoutSchedule(DayOfWeek day, Workout workout, Program program) {
        return WorkoutSchedule.builder()
                .dayOfWeek(day)
                .workout(workout)
                .program(program)
                .build();
    }

    public List<WorkoutSchedule> getAllByProgramId(UUID id) {
        return workoutScheduleRepository.findAllByProgram_Id(id);
    }
}
