package org.example.issuetracker.web.api;

import org.example.issuetracker.configuration.exception.ResourceNotFoundException;
import org.example.issuetracker.model.Tache;
import org.example.issuetracker.model.TacheStatus;
import org.example.issuetracker.repository.TacheRepository;
import org.example.issuetracker.web.dto.error.ErrorDetail;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@AutoConfigureJsonTesters
@SpringBootTest
@AutoConfigureMockMvc
class TacheControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TacheRepository tacheRepository;

    // This object will be initialized thanks to @AutoConfigureJsonTesters
    @Autowired
    private JacksonTester<Tache> jsonTache;

    @Autowired
    private JacksonTester<ErrorDetail> jsonError;
    @Test
    void canRetrieveByUuidWhenExists() throws Exception {

        Tache tache1 = new Tache(UUID.randomUUID(), "Tester le générateur d'appli FUN", "Guillaume", null, 1, null, TacheStatus.NEW);
        // given
        given(tacheRepository.findById(tache1.getId())).willReturn(Optional.of(tache1));

        // when
        MockHttpServletResponse response = mvc.perform(get(format("/api/issues/%s", tache1.getId())).accept(APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(OK.value());

        assertThat(response.getContentAsString(StandardCharsets.UTF_8)).isEqualTo(jsonTache.write(tache1).getJson());
    }

    @Test
    void throwsResourceNotFoundExceptionWhenDoesNotExist() throws Exception {

        UUID uuid = UUID.randomUUID();

        ErrorDetail error = new ErrorDetail("Resource Not Found", 404,
                String.format("Issue with id %s not found", uuid),
                1L,
        "org.example.issuetracker.configuration.exception.ResourceNotFoundException", null);

        // given
        given(tacheRepository.findById(uuid))
                .willThrow(new ResourceNotFoundException(format("Issue with id %s not found", uuid)));

        // when
        MockHttpServletResponse response = mvc.perform(get(format("/api/issues/%s", uuid)).accept(APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(NOT_FOUND.value());

        final String errorAsString = response.getContentAsString();
        ErrorDetail errorDetail = jsonError.parse(errorAsString).getObject();

        ErrorDetail errorDetailBuild = errorDetail.toBuilder().timeStamp(errorDetail.getTimeStamp()).build();

        assertThat(errorAsString).isEqualTo(jsonError.write(errorDetailBuild).getJson());
    }

    @Test
    void canCreateANewIssue() throws Exception {

        Tache tache1 = new Tache(UUID.randomUUID(), "Tester le générateur d'appli FUN", "Guillaume", null, 1, null, TacheStatus.NEW);

        // when
        MockHttpServletResponse response = mvc.perform(
                post("/api/issues/").contentType(APPLICATION_JSON).content(jsonTache.write(tache1).getJson()
                )).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(CREATED.value());
    }
}