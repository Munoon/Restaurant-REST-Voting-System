package com.train4game.munoon.web.vote;

import com.train4game.munoon.model.Restaurant;
import com.train4game.munoon.model.Vote;
import com.train4game.munoon.service.RestaurantService;
import com.train4game.munoon.service.VoteService;
import com.train4game.munoon.to.VoteTo;
import com.train4game.munoon.web.SecurityUtil;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.List;

import static com.train4game.munoon.utils.ValidationUtils.assureIdConsistent;
import static com.train4game.munoon.utils.ValidationUtils.checkNew;

abstract public class AbstractVoteController {
    private static final Logger log = LoggerFactory.getLogger(AbstractVoteController.class);
    private static Type mapperType = new TypeToken<List<VoteTo>>() {}.getType();

    private VoteService service;
    private ModelMapper modelMapper;
    private TypeMap<VoteTo, Vote> toVote;
    private TypeMap<Vote, VoteTo> toVoteTo;

    public AbstractVoteController(VoteService service, RestaurantService restaurantService, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;

        Converter<Integer, Restaurant> converter = num -> restaurantService.get(num.getSource());
        toVote = modelMapper.createTypeMap(VoteTo.class, Vote.class)
                .addMappings(mapper -> mapper.using(converter).map(VoteTo::getRestaurantId, Vote::setRestaurant));

        toVoteTo = modelMapper.createTypeMap(Vote.class, VoteTo.class);
    }

    public VoteTo create(VoteTo voteTo) {
        int userId = SecurityUtil.authUserId();
        log.info("Create {}", voteTo);
        checkNew(voteTo);
        return toVoteTo.map(service.create(toVote.map(voteTo), userId));
    }

    public void delete(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("Delete vote {} with user {}", id, userId);
        service.delete(id, userId);
    }

    public VoteTo get(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("Get vote {} with user {}", id, userId);
        return toVoteTo.map(service.get(id, userId));
    }

    public List<VoteTo> getAll() {
        int userId = SecurityUtil.authUserId();
        log.info("Get all votes of user {}", userId);
        return modelMapper.map(service.getAll(userId), mapperType);
    }

    public void update(VoteTo voteTo, int id) {
        int userId = SecurityUtil.authUserId();
        assureIdConsistent(voteTo, id);
        log.info("Update {} with id {} from user {}", voteTo, id, userId);
        service.update(toVote.map(voteTo), userId);
    }
}
