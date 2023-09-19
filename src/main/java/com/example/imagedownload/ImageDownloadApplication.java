package com.example.imagedownload;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ImageDownloadApplication {
	@GetMapping("/")
	public String showForm() {
		return "index";  // This refers to the 'index.html' template we created earlier.
	}
	@PostMapping("/download")
	public String downloadImage(@RequestParam String url, Model model) {
		// Logic to download the image and handle errors will go here

		// For now, just return to the form
		return "index";
	}

	public static void main(String[] args) {
		SpringApplication.run(ImageDownloadApplication.class, args);
	}

}
