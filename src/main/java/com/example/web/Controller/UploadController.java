package com.example.web.Controller;

import com.example.web.Algorithm.retString;
import com.example.web.ImageProcess.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.web.storage.StorageService;

import static org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS;

@RestController
@RequestMapping(value = "/api/v1/")
public class UploadController {

    private final StorageService storageService;

    @Autowired
    public UploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @Autowired
    private StatefulGame Game;

    @Autowired
    private javaToPy J2P;

    // TESTING GET
    @GetMapping(value = "/")
    public String index() {
        return "We received your GET";
    }

    // TESTING POST
    @PostMapping("/")
    public String postTest() {
        return "We received your POST";
    }


    // STARTING GAME
    @GetMapping(value = "/start")
    public ResponseEntity<String> startGame() {

        int newGame = Game.setID();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("gameID", String.valueOf(newGame));

        // REMOVE DIRECTORY (previous owners info)
        storageService.deleteAll(newGame);

        return ResponseEntity.ok().
                headers(responseHeaders).
                body("Game is starting");
    }

    // ENDING GAME
    @GetMapping(value = "/end/{id}")
    public String endGame(@PathVariable Integer id) {

        // DELETE DIRECTORY AND REMOVE FROM HASHMAP
        storageService.deleteAll(id);
        Game.removeID(id);

        return "Game ended!";
    }


    // RESTART GAME
    @GetMapping(value = "/restart/{id}")
    public String restartGame(@PathVariable Integer id) {
        Game.resetGame(id);
        return "The game will be restarted";
    }


    // TEST ALGORITHM
    @GetMapping(value = "/algo/{id}/{input}")
    public String algoTest(@PathVariable Integer id, @PathVariable String input) {

        try {
            return Game.get(id).update(input).text;
        } catch (Exception e) {
            e.printStackTrace();
            return "did not work";
        }
    }

    // GET number of active users
    @GetMapping(value = "/users")
    public String numUsers() {

        int num = Game.numUsers();

        return "Number of players: " + num;
    }

    // GET number of active users
    @GetMapping(value = "/active")
    public String activeUsers() {
        return Game.Users();
    }


    // DOWNLOAD IMAGE
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> serveFile(@PathVariable int id) {

        // LOAD IMAGE AS RESOURCE FOR RETURN
        String filename = "return-gui.jpg";
        Resource file = storageService.loadAsResource(filename, id);
        System.out.println("    SENDT RET:");
        return ResponseEntity.ok().
                contentType(MediaType.IMAGE_JPEG).
                body(file);
    }


    // UPLOAD IMAGE
    @PostMapping("/upload/{id}")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, @PathVariable int id) {


        // DELETE previous
        storageService.deleteAll(id);
        storageService.store(file, id);

        // IMAGE PROCESSING
        long start = System.currentTimeMillis();
        ImageProcessor ip = new ImageProcessor(J2P);
        Returnvalues ret = new Returnvalues();
        try {
            String path;
            String slash;
            if (IS_OS_WINDOWS) {
                // OS DEPENDENT:
                slash = "\\";
                path = "C:\\Users\\tobias\\IdeaProjects\\backend_cdio\\upload-dir\\";
            } else {
                path = "/home/s195170/upload-dir/";
                slash = "/";
            }

            // image detection process
            ret = ip.process(path + id + slash + file.getOriginalFilename());

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("   bad_image");
            return "bad_image";
        }
        // Tilføj kort til arraylist
        for (Kort element : ret.kortList) {
            Game.returnGameArray(id).add(element);
        }
        long end = System.currentTimeMillis();
        long elapsedTime = end - start;
        System.out.println("Image processing took: " + elapsedTime + "ms");

        // RETTELSE AF OUTPUT FRA IMAGE DETECTION
        KasperFunc kasper = new KasperFunc();
        String correctString = kasper.kasper(ret.Tobias);
        kasper.kasperSet(Game.returnGameArray(id),correctString);
        
        // ALGORITHM
        retString response = null;
        try {
            response = Game.get(id).update(correctString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //  "Træk nye kort."  "Spillet er vundet!"   "Spillet er tabt!"
        // Hvad med return billede? Hvad skal app'en kigge efter?
        if (response.text.contains("Træk") || response.text.contains("Spillet")) {
            return response.text;
        }

        // FIND FROM AND TO CARDS(provided by algorithm)
        Kort foundFrom = Game.findInBlock(id, response.from.getSuit(), response.from.getVal());
        Kort foundTo = Game.findInBlock(id, response.to.getSuit(), response.to.getVal());

        // SET ARROWS
        String fromArrow = "";
        String toArrow = "";
        // IF 'FROM' CARD is EMPTY
        if (foundFrom != null) {
            System.out.println("We found " + foundFrom.getCiffer() + foundFrom.getFarve());
            if (Game.returnGameArray(id).indexOf(foundFrom) <= 6) {
                fromArrow = "UB";
            } else {
                fromArrow = "DB";
            }
        } else {
            System.out.println("Not foundFrom");
        }
        // IF 'TO' CARD is EMPTY
        if (foundTo != null) {
            System.out.println("We found " + foundTo.getCiffer() + foundTo.getFarve());
            if (Game.returnGameArray(id).indexOf(foundTo) <= 6) {
                toArrow = "UG";
            } else {
                toArrow = "DG";
            }
        } else {
            System.out.println("Not foundTo");
        }

        // IMAGE GRAPHICS
        start = System.currentTimeMillis();
        ImageGraphics g = new ImageGraphics();
        try {
            g.addString(id, foundFrom, fromArrow, foundTo, toArrow, file.getOriginalFilename());
        } catch (Exception e) {
            e.printStackTrace();
        }
        end = System.currentTimeMillis();
        elapsedTime = end - start;
        System.out.println("Image graphic took: " + elapsedTime + "ms");

        // IMAGE COMPRESSION
        //start = System.currentTimeMillis();
        /*ImageCompression compression = new ImageCompression();
        try {
            compression.compress(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        */
        //end = System.currentTimeMillis();
        //elapsedTime = end - start;
        //System.out.println("Image compression took: "+elapsedTime+"ms");
        System.out.println("    "+response.text);
        return response.text;
    }


    // UPLOAD TEST IMAGES
    @PostMapping("/training")
    public String UploadTestFile(@RequestParam("file") MultipartFile file) {

        storageService.store(file);

        return "Training!";
    }

}
