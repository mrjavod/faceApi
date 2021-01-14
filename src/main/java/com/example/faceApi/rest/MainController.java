package com.example.faceApi.rest;

import com.example.faceApi.models.Person;
import com.example.faceApi.models.Response;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/faceApi")
public class MainController {

    @Value("${image.folder}")
    private String imageFolder;

    @PostMapping("/create")
    public Response createImage(@RequestBody Person person) throws IOException {

        String personName = person.getName();
        List<String> imageBase64 = person.getImgBase64();

        File file = new File(imageFolder + personName);
        if (!file.exists()) {
            if (file.mkdir()) {

                fileWrite(imageBase64, imageFolder + personName);

                return new Response(1, "Person is created", null);
            } else {
                return new Response(1, "Failed to create directory!", null);
            }
        }

        return new Response(1, "Person already exists", null);
    }

    @GetMapping("/getNames")
    public List<String> getFolderNames() {

        List<String> list = new ArrayList<>();
        File folder = new File(imageFolder);

        if (folder.listFiles() != null) {

            File[] files = folder.listFiles();
            for (File file : files) {
                list.add(file.getName());
            }
        }

        return list;
    }

    @GetMapping("/getImagesNumer/{folderName}")
    public int getImagesByFolderName(@PathVariable("folderName") String folderName) throws IOException {

        int imgsNumber = 0;

        File folder = new File(imageFolder + "\\" + folderName);
        if(folder.listFiles() != null) {
            imgsNumber = folder.listFiles().length;
        }

        return imgsNumber;
    }

    @GetMapping("/getImage/{folderName}/{imgName}")
    public void getImagesByFolderName(@PathVariable("folderName") String folderName,
                                              @PathVariable("imgName") String imgName,
                                              HttpServletResponse response)
            throws IOException {

        byte[] fileContent = FileUtils.readFileToByteArray(new File(imageFolder + "\\"
                                                                             + folderName + "\\"
                                                                             + imgName + ".png"));

        response.setHeader("Content-Type", "image/png");
        response.setHeader("Content-Length", String.valueOf(fileContent.length));
        response.setHeader("Content-Disposition", "inline; filename=\"" + imgName + "\"");

        ByteArrayInputStream is = new ByteArrayInputStream(fileContent);
        int bytesRead;

        while ((bytesRead = is.read(fileContent)) != -1) {
            response.getOutputStream().write(fileContent, 0, bytesRead);
        }
        is.close();
        response.getOutputStream().close();
    }

    private void fileWrite(List<String> text, String file) throws IOException {

        for (int i = 0; i < text.size(); i++) {
            File outputFile = new File( file + "\\" + (i + 1) + ".png");

            String encodedImg = text.get(i).split(",")[1];

            byte[] decodedBytes = Base64
                    .getDecoder()
                    .decode(encodedImg.getBytes(StandardCharsets.UTF_8));
            FileUtils.writeByteArrayToFile(outputFile, decodedBytes);
        }
    }

}
