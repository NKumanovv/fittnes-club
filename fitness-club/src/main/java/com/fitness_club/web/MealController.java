package com.fitness_club.web;

import com.fitness_club.meal.model.Meal;
import com.fitness_club.meal.service.MealService;
import com.fitness_club.security.AuthenticationMetadata;
import com.fitness_club.user.model.User;
import com.fitness_club.user.service.UserService;
import com.fitness_club.web.dto.CreateMealRequest;
import com.fitness_club.web.mapper.DtoMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/meals")
public class MealController {

    private final UserService userService;
    private final MealService mealService;

    @Autowired
    public MealController(UserService userService, MealService mealService) {
        this.userService = userService;
        this.mealService = mealService;
    }


    @GetMapping
    public ModelAndView getMealsPage(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata){

        User user = userService.getById(authenticationMetadata.getUserId());
        List<Meal> meals = user.getMeals();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("meals");
        modelAndView.addObject("user", user);
        modelAndView.addObject("meals", meals);

        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView getMealPage(@PathVariable UUID id){

        Meal meal = mealService.getById(id);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("meal");
        modelAndView.addObject("meal", meal);
        modelAndView.addObject("createMealRequest", DtoMapper.mapMealToCreateMealRequest(meal));

        return modelAndView;
    }

    @PutMapping("/{id}")
    public ModelAndView updateMeal(@PathVariable  UUID id, @Valid CreateMealRequest createMealRequest, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            Meal meal = mealService.getById(id);
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("meal");
            modelAndView.addObject("meal", meal);
            modelAndView.addObject("createMealRequest", DtoMapper.mapMealToCreateMealRequest(meal));
            return modelAndView;
        }

        mealService.editMealDetails(id, createMealRequest);


        return new ModelAndView("redirect:/home");
    }

}









