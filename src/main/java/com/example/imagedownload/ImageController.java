package com.example.imagedownload;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

@Controller
public class ImageController {
    @GetMapping("/")
    public String showForm() {
        return "index";  // This refers to the 'index.html' template we created earlier.
    }
    @PostMapping("/download")
    public String downloadImage(@RequestParam String url, Model model) {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                // Read the image data
                byte[] imageBytes = EntityUtils.toByteArray(entity);

                // Save the image
                saveImage(imageBytes, url);

                // Set success message
                model.addAttribute("success", true);
            }
        } catch (IOException e) {
            if ("Image has already been downloaded. Please provide a new link.".equals(e.getMessage())) {
                model.addAttribute("error", e.getMessage());
            } else {
                model.addAttribute("error", "Error downloading the image: " + e.getMessage());
            }
        } catch (Exception e) {
            model.addAttribute("error", "Unexpected error: " + e.getMessage());
        }

        return "index";
    }


    private void saveImage(byte[] imageBytes, String url) throws IOException {
        try {
            // Generate the hash for the image content
            String imageHash = generateHash(imageBytes);

            // Specify the download location
            String downloadLocation = "/Users/niharbasisth/Desktop/Landing"; // This can be externalized to application.properties

            // Check if an image with the same hash already exists
            File imageFile = new File(downloadLocation + File.separator + imageHash + ".jpg"); // Using .jpg extension, adjust if needed
            if (imageFile.exists()) {
                throw new IOException("Image has already been downloaded. Please provide a new link.");
            }

            // Save the image
            try (FileOutputStream fos = new FileOutputStream(imageFile)) {
                fos.write(imageBytes);
            }
        } catch (NoSuchAlgorithmException e) {
            throw new IOException("Error generating image hash: " + e.getMessage());
        }
    }

    private String generateHash(byte[] imageBytes) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(imageBytes);
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }



}
