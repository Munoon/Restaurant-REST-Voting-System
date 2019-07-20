package com.train4game.munoon.web.controller.vote;

import com.train4game.munoon.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class VoteRestController extends AbstractVoteController {
    @Autowired
    public VoteRestController(VoteService service) {
        super(service);
    }
}
