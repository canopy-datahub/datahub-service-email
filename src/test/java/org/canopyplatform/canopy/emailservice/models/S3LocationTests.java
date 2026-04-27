package org.canopyplatform.canopy.emailservice.models;

import org.canopyplatform.canopy.emailservice.exceptions.S3AttachmentException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class S3LocationTests {

    @Test
    void testS3Location() {
        String s3Path = "bucket/23/helloworld.txt";
        S3Location location = new S3Location(s3Path);
        assertEquals("23/helloworld.txt", location.getKey());
        assertEquals("bucket", location.getBucket());
    }

    @Test
    void testS3Location_Long() {
        String s3Path = "covid-review-dev/cc4cc54e-4635-11ec-a271-9e4df42bafff/1017/rad_016_105-01_DATA_6e306edee01840cba848e5706d41deb5-HR_DATA_transformcopy_v1.csv";
        S3Location location = new S3Location(s3Path);
        assertEquals("cc4cc54e-4635-11ec-a271-9e4df42bafff/1017/rad_016_105-01_DATA_6e306edee01840cba848e5706d41deb5-HR_DATA_transformcopy_v1.csv", location.getKey());
        assertEquals("covid-review-dev", location.getBucket());
    }

    @Test
    void testS3Location_NoFileExtension() {
        String s3Path = "bucket/377/blob";
        S3Location location = new S3Location(s3Path);
        assertEquals("377/blob", location.getKey());
        assertEquals("bucket", location.getBucket());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "bucketpathtofile",
            "bucketpathtofile/",
            "/",
            "//",
            "////"
    })
    void testS3Location_BadPaths(String s3Path) {
        assertThrows(S3AttachmentException.class, () -> new S3Location(s3Path));
    }

    @Test
    void testS3Location_NullPath() {
        String s3Path = null;
        assertThrows(S3AttachmentException.class, () -> new S3Location(s3Path));
    }
}
