package com.lozz.springboot.firstrestapi.survey;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * This class performs the service that you want from SurveyService
 */
@RestController
public class SurveyResource {

    private SurveyService surveyService;

    public SurveyResource(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    /**
     * Method to get all Surveys
     * @return All surveys
     */
    // /surveys => surveys
    @RequestMapping("/surveys")
    public List<Survey> retrieveAllSurveys(){
        return surveyService.retrieveAllSurveys();
    }

    /**
     * Method to get a specific survey based off surveyId
     * @param surveyId surveyId
     * @return survey based of surveyId
     */
    @RequestMapping("/surveys/{surveyId}")
    public Survey retrieveSurveyById(@PathVariable String surveyId){
        Survey survey = surveyService.retrieveSurveyById(surveyId);

        if(survey == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return survey;
    }

    /**
     * Method to get all questions
     * @param surveyId survey Id
     * @return All questions within surveyId
     */
    @RequestMapping("/surveys/{surveyId}/questions")
    public List<Question> retrieveAllSurveyQuestions(@PathVariable String surveyId){
        List<Question> questions = surveyService.retrieveAllSurveyQuestions(surveyId);
        if(questions == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return surveyService.retrieveAllSurveyQuestions(surveyId);
    }

    /**
     * Method to get a specific question from a survey
     * @param surveyId surveyId
     * @param questionId questionId
     * @return Specific question based off surveyId and questionId
     */
    @RequestMapping("/surveys/{surveyId}/questions/{questionId}")
    public Question retrieveSpecificSurveyQuestions(@PathVariable String surveyId,
                                                     @PathVariable String questionId){
        Question question = surveyService.retrieveSpecificSurveyQuestion(surveyId, questionId);
        if(question == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return question;
    }

    /**
     * Method to add a new question and retrieve
     * the location of the new question
     * @param surveyId survey Id
     * @return All questions within surveyId
     */
    @RequestMapping(value="/surveys/{surveyId}/questions", method = RequestMethod.POST)
    public ResponseEntity<Object> addNewSurveyQuestion(@PathVariable String surveyId,
                                                        @RequestBody Question question){

        String questionId = surveyService.addNewSurveyQuestion(surveyId, question);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().
                path("/{questionId}").buildAndExpand(questionId).toUri();
        return ResponseEntity.created(location).build();

    }

    /**
     * Method to delete a survey question
     * @param surveyId survey Id
     * @return All questions within surveyId
     */
    @RequestMapping(value="/surveys/{surveyId}/questions/{questionId}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteSurveyQuestion(@PathVariable String surveyId,
                                                        @PathVariable String questionId){

        surveyService.deleteSurveyQuestion(surveyId, questionId);

        return ResponseEntity.noContent().build();

    }
    /**
     * Method to delete a survey question
     * @param surveyId survey Id
     * @return All questions within surveyId
     */
    @RequestMapping(value="/surveys/{surveyId}/questions/{questionId}", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateSurveyQuestion(@PathVariable String surveyId,
                                                       @PathVariable String questionId,
                                                       @RequestBody Question question){

        surveyService.updateSurveyQuestion(surveyId, questionId, question);

        return ResponseEntity.noContent().build();

    }
}
