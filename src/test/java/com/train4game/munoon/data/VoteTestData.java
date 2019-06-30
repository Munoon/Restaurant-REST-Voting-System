package com.train4game.munoon.data;

import com.train4game.munoon.model.Vote;

import java.time.LocalDateTime;

import static com.train4game.munoon.data.RestaurantTestData.FIRST_RESTAURANT;
import static com.train4game.munoon.data.RestaurantTestData.SECOND_RESTAURANT;
import static com.train4game.munoon.data.UserTestData.FIRST_USER;
import static com.train4game.munoon.data.UserTestData.SECOND_USER;

public class VoteTestData {
    public static final int FIRST_VOTE_ID = 109;
    public static final Vote FIRST_VOTE = new Vote(FIRST_VOTE_ID, FIRST_USER, SECOND_RESTAURANT, LocalDateTime.now());
    public static final Vote SECOND_VOTE = new Vote(FIRST_VOTE_ID + 1, SECOND_USER, FIRST_RESTAURANT, LocalDateTime.now());

    private VoteTestData() {
    }
}
