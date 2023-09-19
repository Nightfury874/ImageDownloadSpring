package com.example.imagedownload;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

@controller
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

                // Save the image (we'll cover this in the next step)
                saveImage(imageBytes, url);
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error downloading the image: " + e.getMessage());
            return "index";
        }

        // Return success message or redirect as needed
        return "index";
    }
    private void saveImage(byte[] imageBytes, String url) throws IOException {
        // Extract the image name from the URL (this is a simple approach; you might want to refine it)
        String imageName = url.substring(url.lastIndexOf("/") + 1);

        // Specify the download location
        String downloadLocation = "/Users/niharbasisth/Desktop"; // This can be externalized to application.properties

        // Check if the image already exists
        File imageFile = new File(downloadLocation + File.separator + imageName);
        if (imageFile.exists()) {
            throw new IOException("Image has already been downloaded. Please provide a new link.");
        }

        // Save the image
        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
            fos.write(imageBytes);
        }
    }


}
