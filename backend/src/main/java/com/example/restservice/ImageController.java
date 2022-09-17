package com.example.restservice;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.UUID;

import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.example.helper.CustomVision;

@RestController
//@RequestMapping("/images")
@CrossOrigin(origins = "${FRONTEND_HOST:http://localhost:8000}")
public class ImageController {
    
    final String connectStr = "DefaultEndpointsProtocol=https;AccountName=imagestoragehty31;AccountKey=nJzlbGPD2cinnxZtI+KTBbkUZnmVijyn8hSCnVR/uUNPyZ2xllxaTBeXILkP4C4GvNjAY3OVyAXG+AStKSa9Jw==;EndpointSuffix=core.windows.net";
    @RequestMapping(value = "/images", method = RequestMethod.POST)
    public ResponseEntity<Object> upload(@RequestBody String data) throws IOException, JSONException{
        String base64 = data.replace("data:image/png;base64,", "");
        byte[] decode = Base64.getDecoder().decode(base64);
        String imageName = UUID.randomUUID() + ".png";
        saveImageToFile(decode, imageName);
        //CustomVision.uploadImage(CustomVision.tagEric, "./images/" + imageName);
        //uploadToCloud("./images/" + imageName);
        System.out.println(data);
        return new ResponseEntity<Object>("image saved sucessfully", HttpStatus.OK);
    }

    private void saveImageToFile(byte[] image, String imageName) throws IOException {
        if (!(new File("./images/").exists())) {
            new File("./images/").mkdir();
        }
        Files.write(new File("./images/" + imageName).toPath(), image);    
    }

    private void uploadToCloud(String fileName){
        // Create a BlobServiceClient object which will be used to create a container client
        
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(connectStr).buildClient();

        // Create the container and return a container client object
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient("images");

        // Get a reference to a blob
        BlobClient blobClient = containerClient.getBlobClient(fileName);

        System.out.println("\nUploading to Blob storage as blob:\n\t" + blobClient.getBlobUrl());

        // Upload the blob
        blobClient.uploadFromFile(fileName);
    }

    @RequestMapping(value = "/validate", method = RequestMethod.POST)
    public ResponseEntity<String> validate(@RequestBody String data) throws IOException {
        String base64 = data.replace("data:image/png;base64,", "");
        byte[] decode = Base64.getDecoder().decode(base64);
        ResponseEntity<String> result = CustomVision.validate(decode);
        
        return result;
    }


}
