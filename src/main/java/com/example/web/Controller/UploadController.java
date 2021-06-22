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

import java.util.ArrayList;


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
    @GetMapping(value="/")
    public String index() {
        return "We received your GET";
    }

    // TESTING POST
    @PostMapping("/")
    public String postTest() {
        return "We received your POST";
    }


    // STARTING GAME
    @GetMapping(value="/start")
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
    @GetMapping(value="/end/{id}")
    public String endGame(@PathVariable Integer id) {

        // DELETE DIRECTORY AND REMOVE FROM HASHMAP
        storageService.deleteAll(id);
        Game.removeID(id);

        return "Game ended!";
    }


    // RESTART GAME
    @GetMapping(value="/restart/{id}")
    public String restartGame(@PathVariable Integer id) {
        Game.resetGame(id);
        return "The game will be restarted";
    }


    // TEST ALGORITHM
    @GetMapping(value="/algo/{id}/{input}")
    public String algoTest(@PathVariable Integer id,@PathVariable String input) {

        try {
            return Game.get(id).update(input).text;
        } catch (Exception e) {
            e.printStackTrace();
            return "did not work";
        }
    }

    // GET number of active users
    @GetMapping(value="/users")
    public String numUsers() {

        int num = Game.numUsers();

        return "Number of players: "+num;
    }

    // GET number of active users
    @GetMapping(value="/active")
    public String activeUsers() {

        return Game.Users();
    }


    // DOWNLOAD IMAGE
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> serveFile(@PathVariable int id){

        // LOAD IMAGE AS RESOURCE FOR RETURN
        String filename = "return-gui.jpg";
        Resource file = storageService.loadAsResource(filename, id);

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

        System.out.println("GAME is : "+Game.numUsers());

        // IMAGE PROCESSING
        long start = System.currentTimeMillis();
        ImageProcessor ip = new ImageProcessor(J2P);
        Returnvalues ret = new Returnvalues();
        try {
            System.out.println(file.getOriginalFilename());
            ret = ip.process("/home/s172133/upload-dir/"+id+"/"+file.getOriginalFilename());
        } catch (Exception e) {
            e.printStackTrace();
        }


        Game.returnGameArray(id).addAll(ret.kortList);


        long end = System.currentTimeMillis();
        long elapsedTime = end - start;
        System.out.println("Image processing took: "+elapsedTime+"ms");



        // ALGORITHM
        String input = ret.Tobias;
        retString response = null;
        try {
            response = Game.get(id).update(input);
        } catch (Exception e) {
            e.printStackTrace();
        }


        // SKAL RETTES
        //  "Træk nye kort."  "Spillet er vundet!"   "Spillet er tabt!"

        System.out.println(response.from.getVal());

        // FIND FROM AND TO CARDS(provided by algorithm)
        Kort foundFrom = Game.findInBlock(id, response.from.getColor(),  response.from.getVal());
        Kort foundTo = Game.findInBlock(id, response.to.getColor(),  response.to.getVal());

        // IF 'FROM' CARD is EMPTY
        if(foundFrom != null){
           System.out.println("We found "+ foundFrom.getCiffer() + foundFrom.getFarve());
        } else {
           System.out.println("Not foundFrom");
        }

        // IF 'TO' CARD is EMPTY
        if(foundTo != null){
            System.out.println("We found "+ foundTo.getCiffer() + foundTo.getFarve());
        } else {
            System.out.println("Not foundTo");
        }



        // SET ARROWS
        String  fromArrow = "";
        String  toArrow = "";
        ArrayList<Kort> find = Game.returnGameArray(id);

        if(find.indexOf(foundFrom) <= 6){
            fromArrow = "UB";
        } else{
            fromArrow = "DB";
        }
        if(find.indexOf(foundTo) <= 6 ){
            toArrow = "UG";
        } else{
            toArrow = "DG";
        }


        // IMAGE GRAPHICS
        start = System.currentTimeMillis();

            ImageGraphics g = new ImageGraphics();
            g.addString(id,foundFrom,fromArrow,foundTo,toArrow);

        end = System.currentTimeMillis();
        elapsedTime = end - start;
        System.out.println("Image graphic took: "+elapsedTime+"ms");


        // IMAGE COMPRESSION
        //start = System.currentTimeMillis();
        ImageCompression compression = new ImageCompression();
        try {
            compression.compress(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //end = System.currentTimeMillis();
        //elapsedTime = end - start;
        //System.out.println("Image compression took: "+elapsedTime+"ms");




        // RESPONSE REGARDING NEXT MOVE
        try {
            System.out.println(Game.get(id).update(input).text);
            return Game.get(id).update(input).text;
        } catch (Exception e) {
            e.printStackTrace();
            return "fejl!";
        }
    }





    // UPLOAD TEST IMAGES
    @PostMapping("/training")
    public String UploadTestFile(@RequestParam("file") MultipartFile file) {

        storageService.store(file);

        return "Training!";
    }

}
