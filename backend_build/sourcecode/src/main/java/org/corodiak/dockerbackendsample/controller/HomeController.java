package org.corodiak.dockerbackendsample.controller;

import lombok.RequiredArgsConstructor;
import org.corodiak.dockerbackendsample.service.DiaryService;
import org.corodiak.dockerbackendsample.type.DiaryDto;
import org.corodiak.dockerbackendsample.type.Health;
import org.corodiak.dockerbackendsample.type.ResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class HomeController {

    private final DiaryService diaryService;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseModel upload(
            @RequestBody DiaryDto.Request diaryDto
    ) {
        Long seq = diaryService.addDiary(diaryDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData("seq", seq);
        responseModel.addData("hostname", System.getenv("HOSTNAME"));
        return responseModel;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseModel getDiaryList() {
        List<DiaryDto.Response> results = diaryService.getDiaryList();
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData("results", results);
        responseModel.addData("hostname", System.getenv("HOSTNAME"));
        return responseModel;
    }

    @RequestMapping(value = "/{seq}", method = RequestMethod.GET)
    public ResponseModel getDiary(
            @PathVariable("seq") Long seq,
            @RequestBody String password
    ) {
        DiaryDto.Response result = diaryService.getDiary(seq,password);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData("result", result);
        responseModel.addData("hostname", System.getenv("HOSTNAME"));
        return responseModel;
    }

    @RequestMapping(value = "/{seq}", method = RequestMethod.DELETE)
    public ResponseModel deleteDiary(
            @PathVariable("seq") Long seq,
            @RequestBody String password
    ) {
        diaryService.deleteDiary(seq,password);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData("hostname", System.getenv("HOSTNAME"));
        return responseModel;
    }

    @RequestMapping(value = "/healthcheck", method = RequestMethod.GET)
    public ResponseModel healthCheck(HttpServletResponse response) {
        if (Health.health) {
            return ResponseModel.builder().build();
        } else {
            response.setStatus(500);
            return ResponseModel.builder().httpStatus(HttpStatus.INTERNAL_SERVER_ERROR).message("의도적인 fail").build();
        }
    }

    @RequestMapping(value = "/switch", method = RequestMethod.GET)
    public ResponseModel switchHealth() {
        Health.health = !Health.health;
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData("health", Health.health);
        return responseModel;
    }

    @RequestMapping(value = "/{seq}", method = RequestMethod.PUT)
    public ResponseModel updateDiary(
            @PathVariable("seq") Long seq,
            @RequestBody DiaryDto.Request diaryDto
    ) {
            DiaryDto.Response result = diaryService.updateDiary(seq,diaryDto);
            ResponseModel responseModel = ResponseModel.builder().build();
            responseModel.addData("result",result);
            return responseModel;
    }

}
