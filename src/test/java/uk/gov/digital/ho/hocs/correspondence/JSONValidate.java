package uk.gov.digital.ho.hocs.correspondence;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.networknt.schema.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

@Slf4j
public class JSONValidate {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);

    @Test
    public void existing() throws Exception {
        try (
                InputStream schemaStream = inputStreamFromClasspath("cmsSchema.json");
                InputStream jsonStream = inputStreamFromClasspath("jsonComplaintExamples/existing.json")
        ) {
            testSchemaValid(schemaStream, jsonStream);
        }
    }

    @Test
    public void existingOther() throws Exception {
        try (
                InputStream schemaStream = inputStreamFromClasspath("cmsSchema.json");
                InputStream jsonStream = inputStreamFromClasspath("jsonComplaintExamples/existingOther.json")
        ) {
            testSchemaValid(schemaStream, jsonStream);
        }
    }

    @Test
    public void makingAppointment() throws Exception {
        try (
                InputStream schemaStream = inputStreamFromClasspath("cmsSchema.json");
                InputStream jsonStream = inputStreamFromClasspath("jsonComplaintExamples/makingAppointment.json")
        ) {
            testSchemaValid(schemaStream, jsonStream);
        }
    }

    @Test
    public void poorInformation() throws Exception {
        try (
                InputStream schemaStream = inputStreamFromClasspath("cmsSchema.json");
                InputStream jsonStream = inputStreamFromClasspath("jsonComplaintExamples/poorInformation.json")
        ) {
            testSchemaValid(schemaStream, jsonStream);
        }
    }

    @Test
    public void biometric() throws Exception {
        try (
                InputStream schemaStream = inputStreamFromClasspath("cmsSchema.json");
                InputStream jsonStream = inputStreamFromClasspath("jsonComplaintExamples/biometric.json")
        ) {
            testSchemaValid(schemaStream, jsonStream);
        }
    }

    @Test
    public void staffBehaviour() throws Exception {
        try (
                InputStream schemaStream = inputStreamFromClasspath("cmsSchema.json");
                InputStream jsonStream = inputStreamFromClasspath("jsonComplaintExamples/staffBehaviour.json")
        ) {
            testSchemaValid(schemaStream, jsonStream);
        }
    }

    @Test
    public void submittingApplication() throws Exception {
        try (
                InputStream schemaStream = inputStreamFromClasspath("cmsSchema.json");
                InputStream jsonStream = inputStreamFromClasspath("jsonComplaintExamples/submittingApplication.json")
        ) {
            testSchemaValid(schemaStream, jsonStream);
        }
    }

    @Test
    public void somethingElseComplaint() throws Exception {
        try (
                InputStream schemaStream = inputStreamFromClasspath("cmsSchema.json");
                InputStream jsonStream = inputStreamFromClasspath("jsonComplaintExamples/somethingElse.json")
        ) {
            testSchemaValid(schemaStream, jsonStream);
        }
    }

    @Test
    public void delaysComplaint() throws Exception {
        try (
                InputStream schemaStream = inputStreamFromClasspath("cmsSchema.json");
                InputStream jsonStream = inputStreamFromClasspath("jsonComplaintExamples/delays.json")
        ) {
            testSchemaValid(schemaStream, jsonStream);
        }
    }

    @Test
    public void decisionComplaint() throws Exception {
        try (
                InputStream schemaStream = inputStreamFromClasspath("cmsSchema.json");
                InputStream jsonStream = inputStreamFromClasspath("jsonComplaintExamples/decision.json")
        ) {
            testSchemaValid(schemaStream, jsonStream);
        }
    }

    @Test
    public void refundComplaint() throws Exception {
        try (
                InputStream schemaStream = inputStreamFromClasspath("cmsSchema.json");
                InputStream jsonStream = inputStreamFromClasspath("jsonComplaintExamples/refund.json")
        ) {
            testSchemaValid(schemaStream, jsonStream);
        }
    }

    @Test
    public void exceedStringMaxLengths() throws Exception {
        try (
                InputStream schemaStream = inputStreamFromClasspath("cmsSchema.json");
                InputStream jsonStream = inputStreamFromClasspath("jsonComplaintExamples/Invalid/exceedStringMaxLengths.json")
        ) {
                Set<ValidationMessage> validationMessages = testSchemaInvalid(schemaStream, jsonStream);

                Set<String> expectedMessages = new HashSet<>();
                expectedMessages.add("$.complaint.reporterDetails.applicantName: may only be 100 characters long");
                expectedMessages.add("$.complaint.reporterDetails.applicantNationality: may only be 100 characters long");
                expectedMessages.add("$.complaint.reporterDetails.applicantEmail: may only be 256 characters long");
                expectedMessages.add("$.complaint.reporterDetails.applicantPhone: may only be 50 characters long");
                expectedMessages.add("$.complaint.complaintDetails.applicationSubmittedWhen: may only be 50 characters long");
                expectedMessages.add("$.complaint.complaintDetails.complaintText: may only be 99999 characters long");
                expectedMessages.add("$.complaint.reference.reference: may only be 100 characters long");

                assertTrue(checkForValidationMessage(validationMessages,expectedMessages));
        }
    }

    private void testSchemaValid(InputStream schemaStream, InputStream jsonStream) throws IOException {
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

    private Set<ValidationMessage> testSchemaInvalid(InputStream schemaStream, InputStream jsonStream) throws IOException {
        JsonNode json = objectMapper.readTree(jsonStream);
        JsonSchema schema = schemaFactory.getSchema(schemaStream);
        return schema.validate(json);

    }

    private boolean checkForValidationMessage (Set<ValidationMessage> validationMessages, Set<String> expectedMessages){
        for (String expectedMessage : expectedMessages){
            if (validationMessages.stream().noneMatch(o -> o.getMessage().equals(expectedMessage))){
                return false;
            }
        }
        return true;
    }


    private static InputStream inputStreamFromClasspath(String path) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }
}
