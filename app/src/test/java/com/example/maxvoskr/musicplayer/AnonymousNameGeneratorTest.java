package com.example.maxvoskr.musicplayer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by mdavi on 3/16/2018.
 */

public class AnonymousNameGeneratorTest {
    @Test
    public void CreateNameTest(){
        String name = AnonymousNameGenerator.generateAnonymousName("mdavis917@gmail.com");
        Assert.assertEquals("Bear9508",name);
    }
}
