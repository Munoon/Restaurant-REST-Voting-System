package com.train4game.munoon.utils;

import com.train4game.munoon.model.Restaurant;
import com.train4game.munoon.model.Vote;
import com.train4game.munoon.to.VoteTo;
import com.train4game.munoon.web.SecurityUtil;

import java.util.ArrayList;
import java.util.List;

public class VoteUtil {
    public static Vote createVote(VoteTo voteTo, Restaurant restaurant) {
        return new Vote(voteTo.getId(), SecurityUtil.getUser(), restaurant, voteTo.getDate());
    }

    public static VoteTo parseVote(Vote vote) {
        return new VoteTo(vote.getId(), vote.getRestaurant().getId(), vote.getDate());
    }

    public static List<VoteTo> parseVote(List<Vote> votes) {
        List<VoteTo> result = new ArrayList<>();
        votes.forEach(vote -> result.add(parseVote(vote)));
        return result;
    }

    public static List<VoteTo> parseVote(Vote... votes) {
        return parseVote(List.of(votes));
    }
}
