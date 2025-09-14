package com.fitness_club.web.mapper;

import com.fitness_club.meal.model.Meal;
import com.fitness_club.user.model.User;
import com.fitness_club.web.dto.CreateMealRequest;
import com.fitness_club.web.dto.UserEditRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DtoMapper {

    public static UserEditRequest mapUserToUserEditRequest(User user){

        return UserEditRequest.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .age(user.getAge())
                .height(user.getHeight())
                .weight(user.getWeight())
                .email(user.getEmail())
                .profilePicture(user.getProfilePicture())
                .build();
    }

    public static CreateMealRequest mapMealToCreateMealRequest(Meal meal){
        return CreateMealRequest.builder()
                .name(meal.getName())
                .calories(meal.getCalories())
                .protein(meal.getProtein())
                .carbs(meal.getCarbs())
                .fats(meal.getFats())
                .isPublic(meal.isPublic())
                .build();
    }



}
