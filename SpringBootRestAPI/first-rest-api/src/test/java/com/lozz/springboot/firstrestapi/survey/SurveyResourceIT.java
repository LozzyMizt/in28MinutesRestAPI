package com.lozz.springboot.firstrestapi.survey;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * This test class allows oyu to test the SurveyResource class by JUnit testing
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SurveyResourceIT {

    private static String SPECIFIC_SURVEY_URL = "/surveys/Survey1";
    private static String GENERIC_SURVEY_URL = "/surveys";
    private static String SPECIFIC_QUESTION_URL = "/surveys/Survey1/questions/Question1";
    private static String GENERIC_QUESTION_URL = "/surveys/Survey1/questions";


    @Autowired
    private TestRestTemplate template;

    /**
     * Test to see if the question in the response is as expected.
     * @throws JSONException Throws an exception if test fails
     */
    @Test
    void retrieveSpecificSurveyQuestions() throws JSONException {
        HttpHeaders headers = getHttpHeaders();

        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);

        ResponseEntity<String> responseEntity = template.exchange(SPECIFIC_QUESTION_URL,
                HttpMethod.GET, httpEntity, String.class);

        String expectedResponse =
                """
                {"id":"Question1","description":"Most Popular Cloud Platform Today","options":["AWS","Azure","Google Cloud","Oracle Cloud"],"correctAnswer":"AWS"}
                """;

        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertEquals("application/json", responseEntity.getHeaders().get("Content-Type").get(0));

        JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(), false);

    }

    /**
     * Test to see if the full list of questions in the response is as expected.
     * @throws JSONException Throws an exception if test fails
     */
    @Test
    void retrieveAllSurveyQuestions() throws JSONException {

        HttpHeaders headers = getHttpHeaders();

        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);

        ResponseEntity<String> responseEntity = template.exchange(GENERIC_QUESTION_URL,
                HttpMethod.GET, httpEntity, String.class);

        String expectedResponse =
                """
                        [
                          {
                            "id": "Question1"
                          },
                          {
                            "id": "Question2"
                          },
                          {
                            "id": "Question3"
                          }
                        ]
                """;

        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertEquals("application/json", responseEntity.getHeaders().get("Content-Type").get(0));

        JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(), false);

    }

    /**
     * Test to see if the survey in the response is as expected.
     * @throws JSONException Throws an exception if test fails
     */
    @Test
    void retrieveSpecificSurvey() throws JSONException {

        HttpHeaders headers = getHttpHeaders();

        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);

        ResponseEntity<String> responseEntity = template.exchange(SPECIFIC_SURVEY_URL,
                HttpMethod.GET, httpEntity, String.class);

        String expectedResponse =
                """
                        {
                          "id": "Survey1",
                          "title": "My Favorite Survey",
                          "description": "Description of the Survey",
                          "questions": [
                            {
                              "id": "Question1",
                              "description": "Most Popular Cloud Platform Today",
                              "options": [
                                "AWS",
                                "Azure",
                                "Google Cloud",
                                "Oracle Cloud"
                              ],
                              "correctAnswer": "AWS"
                            },
                            {
                              "id": "Question2",
                              "description": "Fastest Growing Cloud Platform",
                              "options": [
                                "AWS",
                                "Azure",
                                "Google Cloud",
                                "Oracle Cloud"
                              ],
                              "correctAnswer": "Google Cloud"
                            },
                            {
                              "id": "Question3",
                              "description": "Most Popular DevOps Tool",
                              "options": [
                                "Kubernetes",
                                "Docker",
                                "Terraform",
                                "Azure DevOps"
                              ],
                              "correctAnswer": "Kubernetes"
                            }
                          ]
                        }
                """;

        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertEquals("application/json", responseEntity.getHeaders().get("Content-Type").get(0));

        JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(), false);

    }

    /**
     * Test to see if the full list of surveys in the response is as expected.
     * @throws JSONException Throws an exception if test fails
     */
    @Test
    void retrieveAllSurvey() throws JSONException {

        HttpHeaders headers = getHttpHeaders();

        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);

        ResponseEntity<String> responseEntity = template.exchange(GENERIC_SURVEY_URL,
                HttpMethod.GET, httpEntity, String.class);

        String expectedResponse =
                """
                        [
                            {
                              "id": "Survey1"
                            }
                        ]
                """;

        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertEquals("application/json", responseEntity.getHeaders().get("Content-Type").get(0));

        JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(), false);
    }

    /**
     * This method tests if a question can be added then deleted after.
     */
    @Test
    void addNewSurveyQuestion() {
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

        HttpHeaders headers = getHttpHeaders();

        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity = template.exchange(GENERIC_QUESTION_URL,
                HttpMethod.POST, httpEntity, String.class);

        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());

        String locationHeader = responseEntity.getHeaders().get("Location").get(0);
        assertTrue(locationHeader.contains("/surveys/Survey1/questions/"));

        //Delete Request
        ResponseEntity<String> responseEntityDelete = template.exchange(locationHeader,
                HttpMethod.DELETE, httpEntity, String.class);
        assertTrue(responseEntityDelete.getStatusCode().is2xxSuccessful());


    }

    /**
     *  Method to gather all possible headers needed for a successful request.
     *  Setting Content-Type and Authorization
     * @return headers
     */
    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Basic " + performBasicAuthEncoding("admin", "password"));
        return headers;
    }

    /**
     * Method to concatenate the username and password, then encoding it into Base64
     * @param user Username
     * @param password Password
     * @return Base64 String of username and password
     */
    String performBasicAuthEncoding(String user, String password){
        String combined = user + ":" + password;
        //Base64 Encoding => Bytes
        byte [] encodedBytes = Base64.getEncoder().encode(combined.getBytes());
        return new String(encodedBytes);
    }
}
