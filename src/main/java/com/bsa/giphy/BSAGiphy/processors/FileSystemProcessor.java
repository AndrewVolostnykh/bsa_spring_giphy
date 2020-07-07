package com.bsa.giphy.BSAGiphy.processors;

import com.bsa.giphy.BSAGiphy.entities.GifEntity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Random;

public class FileSystemProcessor {

    private final String STORAGE_PATH = "D:\\Developing\\git_reposes\\bsa_giphy\\src\\main\\java\\com\\bsa\\giphy\\BSAGiphy\\giphy\\";

    public File downloadGifByUrl(GifEntity inputGifEntity) { // mb input GifEntity ?
        try ( var inputStream = new BufferedInputStream(new URL(inputGifEntity.getUrl()).openStream()) ) {

            File directory = new File(STORAGE_PATH + "cache\\" + inputGifEntity.getQuery());
            if(!directory.exists()) {
                directory.mkdir();
            }

            File gif = new File(directory, inputGifEntity.getId());

            var fileOutputStream = new FileOutputStream(gif);
            byte[] buffer = new byte[1024];
            int inputBytes;

            while((inputBytes = inputStream.read(buffer, 0, 1024)) != -1) {
                fileOutputStream.write(buffer, 0, inputBytes);
            }

            return gif;
        } catch (Exception ex) {
            ex.printStackTrace(); // here have to be logger in future
        }
        return null;
    }

    public File getGifPath(GifEntity gifEntity){
        File gif = new File(STORAGE_PATH + "cache\\" + gifEntity.getQuery(), gifEntity.getId());
        return gif.exists() ? gif : null;
    }

    public File getGifPath(String query) {
        File gif = new File(STORAGE_PATH + "cache\\" + query);
        File[] files = gif.listFiles();

        return files != null ? files[new Random().nextInt(files.length)] : null;
    }

    public void addGifToUserFolder(String user_id, GifEntity gifEntity) {

        String path = STORAGE_PATH + "\\users\\" +  user_id + "\\" + gifEntity.getQuery() + "\\"; // input inside File constructor
        File directory = new File(path);

        if(!directory.exists()) {
            directory.mkdirs();
        }

        File userGif = new File(directory, gifEntity.getId());
        File cacheGif;

        cacheGif = getGifPath(gifEntity);

        if(cacheGif != null) {
            cacheGif = new File(cacheGif.getAbsolutePath());
        } else {
            cacheGif = downloadGifByUrl(gifEntity);
        }

        try { // shitcode! because this is a copy of copyToUserFolder(...) method! After few time i will fix it

            Files.copy(cacheGif.toPath(), userGif.toPath());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public File copyToUserFolder(String user_id, String query, String cachePath) {
        File source = new File(cachePath);
        File dest = new File(STORAGE_PATH + "\\users\\" + user_id + "\\" + query);
        try {
            dest.mkdirs();
            Files.copy(source.toPath(), new File(dest, source.getName()).toPath());
        } catch (IOException ex) {
            System.out.println("file already exists"); // here have to be logger
        }
        return new File(dest, source.getName());
    }

}
