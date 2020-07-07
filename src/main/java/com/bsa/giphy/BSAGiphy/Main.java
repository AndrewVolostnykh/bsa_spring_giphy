package com.bsa.giphy.BSAGiphy;

import com.bsa.giphy.BSAGiphy.entities.GifEntity;
import com.bsa.giphy.BSAGiphy.processors.FileSystemProcessor;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        GifEntity gifEntity = new GifEntity();
        gifEntity.setId("onotherFile");
        gifEntity.setUrl("https://giphy.com/gifs/gif-artist-rheabambulu-tIwmTQ64D52XTuL8xd");
        gifEntity.setQuery("newFile");

        var fileProcessor = new FileSystemProcessor();

        File file = fileProcessor.getGifPath(gifEntity.getQuery());
        System.out.println(file.getName());
    }
}
