package com.lozz.springboot.firstrestapi.survey;

import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * This class enables you to perform any service you would like, E.G: Retrieve all survey questions,
 * Look at a specific question, etc
 */
@Service
public class SurveyService {

    private static List<Survey> surveys = new ArrayList<>();
    private static List<Question> questions = new ArrayList<>();

    static {
        Question question1 = new Question("Question1",
                "Most Popular Cloud Platform Today", Arrays.asList(
                "AWS", "Azure", "Google Cloud", "Oracle Cloud"), "AWS");
        Question question2 = new Question("Question2",
                "Fastest Growing Cloud Platform", Arrays.asList(
                "AWS", "Azure", "Google Cloud", "Oracle Cloud"), "Google Cloud");
        Question question3 = new Question("Question3",
                "Most Popular DevOps Tool", Arrays.asList(
                "Kubernetes", "Docker", "Terraform", "Azure DevOps"), "Kubernetes");

        List<Question> questions = new ArrayList<>(Arrays.asList(question1,
                question2, question3));

        Survey survey = new Survey("Survey1", "My Favorite Survey",
                "Description of the Survey", questions);

        surveys.add(survey);
    }

    /**
     * This method retrieves all Surveys
     * @return surveys List of all surveys returned
     */
    public List<Survey> retrieveAllSurveys() {
        return surveys;
    }

    /**
     * This method retrieves a specific Survey by the surveyId
     * @param surveyId unique ID for each survey
     * @return The specific Survey from the SurveyId
     */
    public Survey retrieveSurveyById(String surveyId) {
        Predicate<? super Survey> predicate =
                survey -> survey.getId().equalsIgnoreCase(surveyId);
        Optional<Survey> optionalSurvey =
                surveys.stream().filter(predicate).findFirst();
        if(optionalSurvey.isEmpty()){
            return null;
        }
        return optionalSurvey.get();
    }

    /**
     * This method retrieves all questions
     * @param surveyId unique ID for each survey
     * @return All questions from that specific Survey
     */
    public List<Question> retrieveAllSurveyQuestions(String surveyId) {
        Survey survey =retrieveSurveyById(surveyId);
        if(survey == null){
            return null;
        }
        return survey.getQuestions();
    }

    /**
     * This method retrieves each Question individually based off the questionId
     * @param surveyId unique ID for each survey
     * @param questionId unique ID for each question
     * @return The specific question based off questionId
     */
    public Question retrieveSpecificSurveyQuestion(String surveyId, String questionId) {
        List<Question> surveyQuestions = retrieveAllSurveyQuestions(surveyId);
        if(surveyQuestions == null){
            return null;
        }
        Optional<Question> optionalQuestion =
                surveyQuestions.stream()
                .filter(q -> q.getId().equalsIgnoreCase(questionId)).findFirst();
        if(optionalQuestion.isEmpty()){
            return null;
        }
        return optionalQuestion.get();
    }

    /**
     * Method to add a question
     * @param surveyId surveyId
     * @param question question
     */
    public String addNewSurveyQuestion(String surveyId, Question question) {
        List<Question> questions = retrieveAllSurveyQuestions(surveyId);
        question.setId(generateRandomId());
        questions.add(question);
        return question.getId();
    }

    /**
     * Method to generate a random int
     * @return randomId
     */
    private static String generateRandomId() {
        SecureRandom secureRandom = new SecureRandom();
        String randomId = new BigInteger(32, secureRandom).toString();
        return randomId;
    }

    /**
     * Method to filter through questions and delete a question based off questionId
     * @param surveyId surveyId
     * @param questionId questionId
     * @return questionId
     */
    public String deleteSurveyQuestion(String surveyId, String questionId) {

        List<Question> surveyQuestions = retrieveAllSurveyQuestions(surveyId);
        if(surveyQuestions == null){
            return null;
        }
        Predicate<? super Question> predicate = q -> q.getId().equalsIgnoreCase(questionId);
        boolean removed = surveyQuestions.removeIf(predicate);
        if(!removed){
            return null;
        }

        return questionId;
    }

    /**
     * Method to update an existing question by deleting and replacing the question
     * @param surveyId surveyId
     * @param questionId questionId
     * @param question question List
     */
    public void updateSurveyQuestion(String surveyId, String questionId, Question question) {
        List<Question> questions = retrieveAllSurveyQuestions(surveyId);
        questions.removeIf(q -> q.getId().equalsIgnoreCase(questionId));
        questions.add(question);
    }
}
