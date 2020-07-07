package com.bsa.giphy.BSAGiphy.processors;

import com.bsa.giphy.BSAGiphy.entities.GifEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.Random;

@Service
public class FileSystemProcessor {

    private final String STORAGE_PATH = "D:\\Developing\\git_reposes\\bsa_giphy\\src\\main\\java\\com\\bsa\\giphy\\BSAGiphy\\giphy\\";

    public File downloadGifByUrl(GifEntity inputGifEntity) { // mb input GifEntity ?
        try ( var inputStream = new BufferedInputStream(new URL(inputGifEntity.getUrl()).openStream()) ) {

            File directory = new File(STORAGE_PATH + "cache\\" + inputGifEntity.getQuery());
            if(!directory.exists()) {
                directory.mkdir();
            }

            File gif = new File(directory, inputGifEntity.getId() + ".gif");

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
        File gif = new File(STORAGE_PATH + "cache\\" + gifEntity.getQuery(), gifEntity.getId() + ".gif");
        return gif.exists() ? gif : null;
    }

    public File getGifPath(String query) {
        File gif = new File(STORAGE_PATH + "cache\\" + query);
        File[] files = gif.listFiles();

        return files != null ? files[new Random().nextInt(files.length)] : null;
    }

    public File getFromUserFolder(String user_id, String query) {
        File userGif = new File(STORAGE_PATH + "users\\" + user_id, query);
        if(userGif.listFiles() != null) {
            return userGif.listFiles()[new Random().nextInt(userGif.listFiles().length)];
        }

        return null;
    }

    public void addGifToUserFolder(String user_id, GifEntity gifEntity) {

        String path = STORAGE_PATH + "\\users\\" +  user_id + "\\" + gifEntity.getQuery() + "\\";
        File directory = new File(path);

        if(!directory.exists()) {
            directory.mkdirs();
        }

        File cacheGif;

        cacheGif = getGifPath(gifEntity);

        if(cacheGif != null) {
            cacheGif = new File(cacheGif.getAbsolutePath());
        } else {
            cacheGif = downloadGifByUrl(gifEntity);
        }

        copyToUserFolder(user_id, gifEntity.getQuery(), cacheGif.getPath());

    }

    public File copyToUserFolder(String user_id, String query, String cachePath) {
        File source = new File(cachePath);
        File dest = new File(STORAGE_PATH + "\\users\\" + user_id + "\\" + query);
        try {
            dest.mkdirs();
            dest = new File(dest, source.getName());
            Files.copy(source.toPath(), dest.toPath());
            historyWriter(user_id, query, dest);
        } catch (IOException ex) {
            System.out.println("file already exists"); // here have to be logger
        }
        return new File(dest, source.getName());
    }

    public void historyWriter(String user_id, String query, File savedFile) {
        File userFile = new File(STORAGE_PATH + "\\users" + "\\" + user_id, "history.csv");
        try (PrintStream printer = new PrintStream(new FileOutputStream(userFile, true))) {

            if(!userFile.exists()) {
                userFile.createNewFile();
            }

            String history = LocalDate.now().toString() + "," + query + "," + savedFile.getAbsolutePath();

            printer.println(history);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String getSTORAGE_PATH() {
        return STORAGE_PATH;
    }
}
