package ua.javawebinar.topjava.util;

import ua.javawebinar.topjava.model.Meal;
import ua.javawebinar.topjava.model.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class MealsUtil {
    private static final List<Meal> meals = new ArrayList<>();

    static {
        meals.add(new Meal(LocalDateTime.of(2019, Month.MAY, 30, 10, 0), "Завтрак", 500));
        meals.add(new Meal(LocalDateTime.of(2019, Month.MAY, 30, 13, 0), "Обед", 1000));
        meals.add(new Meal(LocalDateTime.of(2019, Month.MAY, 30, 20, 0), "Ужин", 500));
        meals.add(new Meal(LocalDateTime.of(2019, Month.MAY, 31, 10, 0), "Завтрак", 1000));
        meals.add(new Meal(LocalDateTime.of(2019, Month.MAY, 31, 13, 0), "Обед", 500));
        meals.add(new Meal(LocalDateTime.of(2019, Month.MAY, 31, 20, 0), "Ужин", 510));
    }

    public static List<Meal> getMeals() {
        return meals;
    }

    public static List<MealTo> getWithExcess(List<Meal> meals, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
                );

        return meals.stream()
                .map(meal -> createWithExcess(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static List<MealTo> getFilteredWithExcess(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
                );

        return meals.stream()
                .filter(meal -> TimeUtil.isBetween(meal.getTime(), startTime, endTime))
                .map(meal -> createWithExcess(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    private static MealTo createWithExcess(Meal meal, boolean excess) {
        return new MealTo(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}