package com.elderlink.backend.integrationtests;

import com.elderlink.backend.repositories.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private RequestHistoryRepository requestHistoryRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private CreditTransactionRepository creditTransactionRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private BeneficiaryRepository beneficiaryRepository;

    @Test
    public void testRegisterUser() throws Exception {
        String userJson = """
                        {
                             "firstName":"John",
                             "lastName":"Doe",
                             "email":"test@gmail.com",
                             "password":"12345678",
                             "phone":"782-882-8445",
                             "birthDate":"1950-02-07",
                             "address":{
                                 "street_name":"brunswick street",
                                 "suite_number":"811",
                                 "city":"Baroda",
                                 "pincode":"B3J 3L8",
                                 "state":"Halifax",
                                 "country":"Canada"
                             }
                         }
                """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated());
    }

    @AfterEach
    public void cleanUpDatabase() {
        beneficiaryRepository.deleteAll();
        messageRepository.deleteAll();
        reviewRepository.deleteAll();
        blogRepository.deleteAll();
        requestHistoryRepository.deleteAll();
        creditTransactionRepository.deleteAll();
        requestRepository.deleteAll();
        // Delete all refresh tokens first to avoid foreign key constraint violation
        refreshTokenRepository.deleteAll();

        // Now it's safe to delete all users
        userRepository.deleteAll();
    }
}
