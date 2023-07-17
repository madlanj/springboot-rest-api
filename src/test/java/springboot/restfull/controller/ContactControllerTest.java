package springboot.restfull.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import springboot.restfull.entity.Contact;
import springboot.restfull.entity.User;
import springboot.restfull.model.ContactResponse;
import springboot.restfull.model.CreateContactRequest;
import springboot.restfull.model.UpdateContactRequest;
import springboot.restfull.model.WebResponse;
import springboot.restfull.repository.ContactRepository;
import springboot.restfull.repository.UserRepository;
import springboot.restfull.security.BCrypt;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(){
        contactRepository.deleteAll();
        userRepository.deleteAll();


        User user = new User();
        user.setUsername("test");
        user.setPassword(BCrypt.hashpw("test", BCrypt.gensalt()));
        user.setName("test");
        user.setToken("test");
        user.setTokenExpiredAt(System.currentTimeMillis() + 1000000000L);
        userRepository.save(user);
    }

    @Test
    void createContactBadRequest() throws Exception{
        CreateContactRequest request = new CreateContactRequest();
        request.setFirstName("");
        request.setEmail("salah");

        mockMvc.perform(
                post("/api/contacts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getErrors());

        });
    }

    @Test
    void createContactSuccess() throws Exception{
        CreateContactRequest request = new CreateContactRequest();
        request.setFirstName("adlan");
        request.setLastName("jazuli");
        request.setPhone("089899991212");
        request.setEmail("benar@example.com");

        mockMvc.perform(
                post("/api/contacts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals("adlan", response.getData().getFirstName());
            assertEquals("jazuli", response.getData().getLastName());
            assertEquals("089899991212", response.getData().getPhone());
            assertEquals("benar@example.com", response.getData().getEmail());
            assertTrue(contactRepository.existsById(response.getData().getId()));
        });
    }

    @Test
    void getContactNotFound() throws Exception{

        mockMvc.perform(
                get("/api/contacts/123")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getErrors());

        });
    }

    @Test
    void getContactSuccess() throws Exception{
        User user = userRepository.findById("test").orElseThrow();

        Contact contact = new Contact();
        contact.setUser(user);
        contact.setId("123");
        contact.setFirstName("adlan");
        contact.setLastName("jazuli");
        contact.setPhone("089899991212");
        contact.setEmail("benar@example.com");
        contactRepository.save(contact);

        mockMvc.perform(
                get("/api/contacts/"+contact.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(contact.getId(), response.getData().getId());
            assertEquals(contact.getFirstName(), response.getData().getFirstName());
            assertEquals(contact.getLastName(), response.getData().getLastName());
            assertEquals(contact.getEmail(), response.getData().getEmail());
            assertEquals(contact.getPhone(), response.getData().getPhone());

        });
    }

    @Test
    void updateContactBadRequest() throws Exception{
        UpdateContactRequest request = new UpdateContactRequest();
        request.setFirstName("");
        request.setEmail("salah");

        mockMvc.perform(
                put("/api/contacts/1231")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getErrors());

        });
    }

    @Test
    void updateContactSuccess() throws Exception{
        User user = userRepository.findById("test").orElseThrow();
        Contact contact = new Contact();
        contact.setUser(user);
        contact.setId("123");
        contact.setFirstName("adlan");
        contact.setLastName("jazuli");
        contact.setPhone("089899991212");
        contact.setEmail("benar@example.com");
        contactRepository.save(contact);

        UpdateContactRequest request = new UpdateContactRequest();
        request.setFirstName("budi");
        request.setLastName("joko");
        request.setPhone("089899991111");
        request.setEmail("update@example.com");

        mockMvc.perform(
                put("/api/contacts/"+contact.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals("budi", response.getData().getFirstName());
            assertEquals("joko", response.getData().getLastName());
            assertEquals("089899991111", response.getData().getPhone());
            assertEquals("update@example.com", response.getData().getEmail());
            assertTrue(contactRepository.existsById(response.getData().getId()));
        });
    }

    @Test
    void deleteContactNotFound() throws Exception{

        mockMvc.perform(
                delete("/api/contacts/123")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getErrors());

        });
    }

    @Test
    void deleteContactSuccess() throws Exception{
        User user = userRepository.findById("test").orElseThrow();

        Contact contact = new Contact();
        contact.setUser(user);
        contact.setId("123");
        contact.setFirstName("adlan");
        contact.setLastName("jazuli");
        contact.setPhone("089899991212");
        contact.setEmail("benar@example.com");
        contactRepository.save(contact);

        mockMvc.perform(
                delete("/api/contacts/"+contact.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());


        });
    }


    @Test
    void searchSuccess() throws Exception{

        User user = userRepository.findById("test").orElseThrow();

        for (int i = 0; i < 100; i++) {
            Contact contact = new Contact();
            contact.setUser(user);
            contact.setId(UUID.randomUUID().toString());
            contact.setFirstName("adlan " +i);
            contact.setLastName("jazuli");
            contact.setPhone("089899991212");
            contact.setEmail("benar@example.com");
            contactRepository.save(contact);
        }

        mockMvc.perform(
                get("/api/contacts")
                        .queryParam("name", "adlan")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(10, response.getData().size());
            log.info("size");
            assertEquals(10, response.getPaging().getTotalPage());
            log.info("total page");
            assertEquals(0, response.getPaging().getCurrentPage());
            log.info("current page");
            assertEquals(10, response.getPaging().getSize());
            log.info("paging size");


        });

        mockMvc.perform(
                get("/api/contacts")
                        .queryParam("name", "jazuli")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(10, response.getData().size());
            log.info("size");
            assertEquals(10, response.getPaging().getTotalPage());
            log.info("total page");
            assertEquals(0, response.getPaging().getCurrentPage());
            log.info("current page");
            assertEquals(10, response.getPaging().getSize());
            log.info("paging size");
        });

        mockMvc.perform(
                get("/api/contacts")
                        .queryParam("email", "example.com")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(10, response.getData().size());
            log.info("size");
            assertEquals(10, response.getPaging().getTotalPage());
            log.info("total page");
            assertEquals(0, response.getPaging().getCurrentPage());
            log.info("current page");
            assertEquals(10, response.getPaging().getSize());
            log.info("paging size");


        });

        mockMvc.perform(
                get("/api/contacts")
                        .queryParam("phone", "1212")
                        .queryParam("page", "1000")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(0, response.getData().size());
            log.info("size");
            assertEquals(10, response.getPaging().getTotalPage());
            log.info("total page");
            assertEquals(1000, response.getPaging().getCurrentPage());
            log.info("current page");
            assertEquals(10, response.getPaging().getSize());
            log.info("paging size");


        });
    }

    @Test
    void searchNotFound() throws Exception{

        log.info("TEST TEST TEST");

        mockMvc.perform(
                get("/api/contacts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(0, response.getData().size());
            assertEquals(0, response.getPaging().getCurrentPage());
            assertEquals(0, response.getPaging().getTotalPage());
            assertEquals(10, response.getPaging().getSize());


        });
    }



}