package org.fatmansoft.teach.util;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class MultipartFileUtils {
    public static MultipartFile convertToMultipartFile(byte[] barr, String fileName) throws IOException {
        return new MockMultipartFile(fileName, barr);
    }
}
