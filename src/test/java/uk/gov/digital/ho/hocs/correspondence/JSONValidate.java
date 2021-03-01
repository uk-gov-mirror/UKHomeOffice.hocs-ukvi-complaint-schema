package uk.gov.digital.ho.hocs.correspondence;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@Slf4j
public class JSONValidate {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);

    @Test
    public void existing() throws Exception {
        try (
                InputStream schemaStream = inputStreamFromClasspath("cmsSchema.json");
                InputStream jsonStream = inputStreamFromClasspath("jsonComplaintExamples/existing.json")
        ) {
            testSchema(schemaStream, jsonStream);
        }
    }

    @Test
    public void makingAppointment() throws Exception {
        try (
                InputStream schemaStream = inputStreamFromClasspath("cmsSchema.json");
                InputStream jsonStream = inputStreamFromClasspath("jsonComplaintExamples/makingAppointment.json")
        ) {
            testSchema(schemaStream, jsonStream);
        }
    }

    @Test
    public void poorInformation() throws Exception {
        try (
                InputStream schemaStream = inputStreamFromClasspath("cmsSchema.json");
                InputStream jsonStream = inputStreamFromClasspath("jsonComplaintExamples/poorInformation.json")
        ) {
            testSchema(schemaStream, jsonStream);
        }
    }

    @Test
    public void biometric() throws Exception {
        try (
                InputStream schemaStream = inputStreamFromClasspath("cmsSchema.json");
                InputStream jsonStream = inputStreamFromClasspath("jsonComplaintExamples/biometric.json")
        ) {
            testSchema(schemaStream, jsonStream);
        }
    }

    @Test
    public void staffBehaviour() throws Exception {
        try (
                InputStream schemaStream = inputStreamFromClasspath("cmsSchema.json");
                InputStream jsonStream = inputStreamFromClasspath("jsonComplaintExamples/staffBehaviour.json")
        ) {
            testSchema(schemaStream, jsonStream);
        }
    }

    private void testSchema(InputStream schemaStream, InputStream jsonStream) throws IOException {
        JsonNode json = objectMapper.readTree(jsonStream);
        JsonSchema schema = schemaFactory.getSchema(schemaStream);
        Set<ValidationMessage> validationResult = schema.validate(json);
        if (validationResult.isEmpty()) {
            assertTrue(true);
        } else {
            for (ValidationMessage validationMessage : validationResult) {
                log.info(validationMessage.getMessage());
            }
            fail();
        }
    }

    private static InputStream inputStreamFromClasspath(String path) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }
}
