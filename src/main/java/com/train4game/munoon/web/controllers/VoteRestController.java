package com.train4game.munoon.web.controllers;

import com.train4game.munoon.model.Restaurant;
import com.train4game.munoon.model.Vote;
import com.train4game.munoon.service.RestaurantService;
import com.train4game.munoon.service.VoteService;
import com.train4game.munoon.to.VoteTo;
import com.train4game.munoon.utils.ParserUtil;
import com.train4game.munoon.web.SecurityUtil;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.List;

import static com.train4game.munoon.utils.ParserUtil.VOTE_LIST_MAPPER;
import static com.train4game.munoon.utils.ValidationUtils.assureIdConsistent;
import static com.train4game.munoon.utils.ValidationUtils.checkNew;

@RestController
@RequestMapping(value = VoteRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteRestController {
    public static final String REST_URL = "/vote";
    private static final Logger log = LoggerFactory.getLogger(VoteRestController.class);

    private final VoteService service;
    private final ModelMapper modelMapper;
    private final TypeMap<VoteTo, Vote> toVote;
    private final TypeMap<Vote, VoteTo> toVoteTo;

    @Autowired
    public VoteRestController(VoteService service, RestaurantService restaurantService, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;

        Converter<Integer, Restaurant> converter = num -> restaurantService.get(num.getSource());
        toVote = modelMapper.createTypeMap(VoteTo.class, Vote.class)
                .addMappings(mapper -> mapper.using(converter).map(VoteTo::getRestaurantId, Vote::setRestaurant));

        toVoteTo = modelMapper.createTypeMap(Vote.class, VoteTo.class);
    }

    @GetMapping
    public List<VoteTo> getAll() {
        int userId = SecurityUtil.authUserId();
        log.info("Get all votes of user {}", userId);
        return modelMapper.map(service.getAll(userId), VOTE_LIST_MAPPER);
    }

    @GetMapping("/{id}")
    public VoteTo get(@PathVariable int id) {
        int userId = SecurityUtil.authUserId();
        log.info("Get vote {} with user {}", id, userId);
        return toVoteTo.map(service.get(id, userId));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        int userId = SecurityUtil.authUserId();
        log.info("Delete vote {} with user {}", id, userId);
        service.delete(id, userId);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody VoteTo voteTo, @PathVariable int id) {
        int userId = SecurityUtil.authUserId();
        assureIdConsistent(voteTo, id);
        log.info("Update {} with id {} from user {}", voteTo, id, userId);
        service.update(toVote.map(voteTo), userId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VoteTo> createWithLocation(@RequestBody VoteTo vote) {
        VoteTo created = create(vote);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }


    private VoteTo create(VoteTo voteTo) {
        int userId = SecurityUtil.authUserId();
        log.info("Create {}", voteTo);
        checkNew(voteTo);
        return toVoteTo.map(service.create(toVote.map(voteTo), userId));
    }
}
