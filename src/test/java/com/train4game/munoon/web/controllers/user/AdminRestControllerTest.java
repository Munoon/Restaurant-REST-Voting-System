package com.train4game.munoon.web.controllers.user;

import com.train4game.munoon.model.Roles;
import com.train4game.munoon.model.User;
import com.train4game.munoon.repository.JpaUtil;
import com.train4game.munoon.service.UserService;
import com.train4game.munoon.to.VoteTo;
import com.train4game.munoon.utils.JsonUtil;
import com.train4game.munoon.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.train4game.munoon.TestUtil.readFromJson;
import static com.train4game.munoon.TestUtil.userAuth;
import static com.train4game.munoon.data.UserTestData.*;
import static com.train4game.munoon.data.VoteTestData.*;
import static com.train4game.munoon.model.Roles.ROLE_USER;
import static com.train4game.munoon.utils.ParserUtil.VOTE_LIST_MAPPER;
import static com.train4game.munoon.utils.exceptions.ErrorType.VALIDATION_ERROR;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = AdminRestController.REST_URL + "/";

    @Autowired
    public AdminRestControllerTest(UserService userService, ModelMapper modelMapper, JpaUtil jpaUtil, CacheManager cacheManager, WebApplicationContext webApplicationContext) {
        super(userService, modelMapper, jpaUtil, cacheManager, webApplicationContext);
    }

    @Test
    void testGetAll() throws Exception {
        mockMvc.perform(get(REST_URL)
                .with(userAuth(FIRST_USER)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(FIRST_USER, SECOND_USER));
    }

    @Test
    void testGet() throws Exception {
        mockMvc.perform(get(REST_URL + FIRST_USER_ID)
                .with(userAuth(FIRST_USER)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(FIRST_USER));
    }

    @Test
    void testCreate() throws Exception {
        User expected = new User(null, "new", "new@gmail.com", "newPass", new Date(), true, Set.of(ROLE_USER));
        ResultActions actions = mockMvc.perform(post(REST_URL)
                .with(userAuth(FIRST_USER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(expected, expected.getPassword())))
                .andExpect(status().isCreated());

        User returned = readFromJson(actions, User.class);
        expected.setId(returned.getId());

        assertMatch(returned, expected);
        assertMatch(userService.getAll(), expected, FIRST_USER, SECOND_USER);
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + FIRST_USER_ID)
                .with(userAuth(FIRST_USER)))
                .andExpect(status().isNoContent());
        assertMatch(userService.getAll(), SECOND_USER);
    }

    @Test
    void testUpdate() throws Exception {
        User updated = new User(FIRST_USER);
        updated.setName("update name");
        updated.setEmail("newemail@email.com");

        mockMvc.perform(put(REST_URL + FIRST_USER_ID)
                .with(userAuth(FIRST_USER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(updated, updated.getPassword())))
                .andExpect(status().isNoContent());

        assertMatch(userService.get(FIRST_USER_ID), updated);
    }

    @Test
    void testGetByEmail() throws Exception {
        mockMvc.perform(get(REST_URL + "by?email=" + FIRST_USER_EMAIL)
                .with(userAuth(FIRST_USER)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(FIRST_USER));
    }

    @Test
    void getAllVotesByUserId() throws Exception {
        List<VoteTo> expected = modelMapper.map(Arrays.asList(SECOND_VOTE, FIRST_VOTE), VOTE_LIST_MAPPER);
        mockMvc.perform(get(REST_URL + "votes/" + FIRST_USER_ID)
                .with(userAuth(FIRST_USER)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJsonVoteTo(expected));
    }

    @Test
    void getAllVotesByUserIdAndDate() throws Exception {
        VoteTo expected = modelMapper.map(FIRST_VOTE, VoteTo.class);
        mockMvc.perform(get(REST_URL + String.format("votes/%d?date=%s", FIRST_USER_ID, FIRST_VOTE.getDate()))
                .with(userAuth(FIRST_USER)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJsonVoteToArray(expected));
    }

    @Test
    void getAllVotes() throws Exception {
        List<VoteTo> expected = modelMapper.map(Arrays.asList(SECOND_VOTE, THIRD_VOTE, FIRST_VOTE), VOTE_LIST_MAPPER);
        mockMvc.perform(get(REST_URL + "votes")
                .with(userAuth(FIRST_USER)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJsonVoteTo(expected));
    }

    @Test
    void noPermission() throws Exception {
        mockMvc.perform(get(REST_URL + FIRST_USER_ID)
                .with(userAuth(SECOND_USER)))
                .andExpect(status().isForbidden());
    }

    @Test
    void notAuthorized() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createNotUniqueEmail() throws Exception {
        User expected = new User(null, "new", FIRST_USER_EMAIL, "newPass", new Date(), true, Set.of(ROLE_USER));
        mockMvc.perform(post(REST_URL)
                .with(userAuth(FIRST_USER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(expected, expected.getPassword())))
                .andExpect(status().isConflict())
                .andExpect(errorType(VALIDATION_ERROR));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateNotUniqueEmail() throws Exception {
        User updated = new User(FIRST_USER);
        updated.setEmail(SECOND_USER.getEmail());

        mockMvc.perform(put(REST_URL + FIRST_USER_ID)
                .with(userAuth(FIRST_USER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(updated, updated.getPassword())))
                .andExpect(status().isConflict())
                .andExpect(errorType(VALIDATION_ERROR));
    }

    @Test
    void createUnsafeHtml() throws Exception {
        User user = new User(null, "<script>alert(123)</script>", "email@email.com", "password", ROLE_USER);

        mockMvc.perform(post(REST_URL)
                .with(userAuth(FIRST_USER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(user, user.getPassword())))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR));
    }

    @Test
    void updateUnsafeHtml() throws Exception {
        User updated = new User(FIRST_USER);
        updated.setName("<script>alert(123)</script>");

        mockMvc.perform(put(REST_URL + FIRST_USER_ID)
                .with(userAuth(FIRST_USER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR));
    }
}