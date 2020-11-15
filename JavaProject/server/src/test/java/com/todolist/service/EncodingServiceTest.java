package com.todolist.service;

import org.junit.Test;

import static org.junit.Assert.*;

public class EncodingServiceTest {

    private EncodingService encodingService = new EncodingService();

    @Test
    public void canCryptPasswordWithMD5Algorithm() throws Exception {
        String plainText = "password123";

        // encrypt 1 time
        final String firstEncryption = encodingService.hash(plainText);

        assertNotNull(firstEncryption);
        assertNotEquals(firstEncryption, plainText);

        // encrypt 2 times
        final String secondEncryption = encodingService.hash(plainText);

        assertNotNull(secondEncryption);
        assertEquals(firstEncryption, secondEncryption);



    }
}