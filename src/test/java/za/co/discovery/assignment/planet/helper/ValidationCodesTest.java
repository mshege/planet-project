package za.co.discovery.assignment.planet.helper;

import org.junit.Test;

import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.junit.Assert.assertThat;

public class ValidationCodesTest {
    @Test
    public void verifyThatValidationCodesHandlingIsCorrect() throws Exception {
        ValidationCodes expectedCode = ValidationCodes.ROUTE_EXISTS;
        int codeId = ValidationCodes.TRAFFIC_EXISTS.getId();
        ValidationCodes actualMode = ValidationCodes.fromString("ROUTE EXISTS");
        assertThat(expectedCode, sameBeanAs(actualMode));
        assertThat(3, sameBeanAs(codeId));
    }
}
