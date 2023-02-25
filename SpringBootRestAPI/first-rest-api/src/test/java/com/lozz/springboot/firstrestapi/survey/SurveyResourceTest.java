package com.lozz.springboot.firstrestapi.survey;

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This test class allows oyu to test the SurveyResource class by mocking the environment
 */
@WebMvcTest(controllers = SurveyResource.class)
@AutoConfigureMockMvc(addFilters = false) // Disables all filters including security
public class SurveyResourceTest {
    @MockBean
    private SurveyService surveyService;
    @Autowired
    private MockMvc mockMvc;

    private static String SPECIFIC_QUESTION_URL = "http://localhost:8080/surveys/Survey1/questions/Question1";
    private static String GENERIC_QUESTION_URL = "http://localhost:8080/surveys/Survey1/questions";

    /**
     * This method tests a 404 scenario when calling a get method
     * @throws Exception exception thrown if results does not match
     */
    @Test
    void retrieveSpecificSurveyQuestions_404Scenario() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(SPECIFIC_QUESTION_URL)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        int actualHttpResult = mvcResult.getResponse().getStatus();

        assertEquals(404, actualHttpResult);

    }

    /**
     * This method tests a 200 scenario when calling a get method
     * @throws Exception exception thrown if results does not match
     */
    @Test
    void retrieveSpecificSurveyQuestions_basicScenario() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(SPECIFIC_QUESTION_URL)
                .accept(MediaType.APPLICATION_JSON);

        Question question = new Question("Question1",
                "Most Popular Cloud Platform Today", Arrays.asList(
                "AWS", "Azure", "Google Cloud", "Oracle Cloud"), "AWS");

        when(surveyService.retrieveSpecificSurveyQuestion("Survey1",
                "Question1"))
                .thenReturn(question);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        String expectedResult = """
                {"id":"Question1","description":"Most Popular Cloud Platform Today","options":["AWS","Azure","Google Cloud","Oracle Cloud"],"correctAnswer":"AWS"}
                """;
        String actualResult = mvcResult.getResponse().getContentAsString();

        int actualHttpResult = mvcResult.getResponse().getStatus();

        assertEquals(200, actualHttpResult);
        JSONAssert.assertEquals(expectedResult, actualResult, false);

    }

    /**
     * This method tests a 201 scenario when calling a post method
     * @throws Exception exception thrown if results does not match
     */
    @Test
    void addNewSurveyQuestion_basicScenario() throws Exception{
        String requestBody = """
            {
              "description": "Your Favourite Language",
              "options": [
                "Java",
                "Python",
                "JavaScript",
                "Haskell"
              ],
              "correctAnswer": "Java"
            }
            """;

        when(surveyService.addNewSurveyQuestion(anyString(), any())).thenReturn("SOME_ID");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(GENERIC_QUESTION_URL)
                .accept(MediaType.APPLICATION_JSON).content(requestBody).contentType(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        int actualResult = mvcResult.getResponse().getStatus();

        assertEquals(201, actualResult);
        String locationHeader = mvcResult.getResponse().getHeader("Location");
        assertTrue(locationHeader.contains("surveys/Survey1/questions/SOME_ID"));
    }
}
