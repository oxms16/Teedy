package com.sismics.util.mime;

import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;

/**
 * Tests for MimeTypeUtil.
 */
public class MimeTypeUtilTest {
    @Test
    public void testGetFileExtensionKnownMimeTypes() {
        assertEquals("zip", MimeTypeUtil.getFileExtension(MimeType.APPLICATION_ZIP));
        assertEquals("gif", MimeTypeUtil.getFileExtension(MimeType.IMAGE_GIF));
        assertEquals("jpg", MimeTypeUtil.getFileExtension(MimeType.IMAGE_JPEG));
        assertEquals("png", MimeTypeUtil.getFileExtension(MimeType.IMAGE_PNG));
        assertEquals("pdf", MimeTypeUtil.getFileExtension(MimeType.APPLICATION_PDF));
        assertEquals("odt", MimeTypeUtil.getFileExtension(MimeType.OPEN_DOCUMENT_TEXT));
        assertEquals("docx", MimeTypeUtil.getFileExtension(MimeType.OFFICE_DOCUMENT));
        assertEquals("txt", MimeTypeUtil.getFileExtension(MimeType.TEXT_PLAIN));
        assertEquals("csv", MimeTypeUtil.getFileExtension(MimeType.TEXT_CSV));
        assertEquals("mp4", MimeTypeUtil.getFileExtension(MimeType.VIDEO_MP4));
        assertEquals("webm", MimeTypeUtil.getFileExtension(MimeType.VIDEO_WEBM));
    }

    @Test
    public void testGetFileExtensionUnknownMimeType() {
        assertEquals("bin", MimeTypeUtil.getFileExtension("application/unknown"));
    }

    @Test
    public void testGuessMimeTypeFallsBackToName() throws Exception {
        Path tempFile = Files.createTempFile("mime-test", ".unknown");

        try {
            String mimeType = MimeTypeUtil.guessMimeType(tempFile, "document.pdf");
            assertEquals(MimeType.APPLICATION_PDF, mimeType);
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    @Test
    public void testGuessMimeTypeReturnsDefaultForUnknownFileAndUnknownName() throws Exception {
        Path tempFile = Files.createTempFile("mime-test", ".unknownextensionforteedy");

        try {
            String mimeType = MimeTypeUtil.guessMimeType(tempFile, "file.unknownextensionforteedy");
            assertEquals(MimeType.DEFAULT, mimeType);
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }
}
