package com.galvanize.reqres;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TextController {

    @GetMapping("/camelize")
    public String camelize(@RequestParam(name="original", required=true) String orig,
                           @RequestParam(name="initialCap", required=false, defaultValue="false") String cap)
    {
        String[] parts = orig.split("_");
        String retVal = "";
        for (int i=0;i<parts.length;i++) {
            if (i==0) {
                if (cap.toLowerCase().equals("true")) {
                    retVal += parts[i].substring(0, 1).toUpperCase() + parts[i].substring(1);
                }
                else {
                    retVal += parts[i];
                }
            }
            else {
                retVal += parts[i].substring(0, 1).toUpperCase() + parts[i].substring(1);
            }
        }

        return retVal;
    }

    @GetMapping("/redact")
    public String redact(@RequestParam(name="original", required=true) String orig,
                         @RequestParam(name="badWord", required=true) String[] badWords)
    {
        String retVal = orig;
        for (String word : badWords) {
            int length = word.length();
            String repl = "";
            for (int i=0;i<length;i++) repl += "*";
            retVal = retVal.replaceAll(word, repl) ;
        }
        return retVal;
    }

    @PostMapping("/encode")
    public String encode(@RequestParam(name="message", required=true) String message,
                         @RequestParam(name="key", required=true) String key)
    {
        Map<String, String> ring = new HashMap<>();
        String realAlpha = "abcdefghijklmnopqrstuvwxyz";
        String[] codes = key.split("");
        for (int i=0;i<codes.length;i++) {
            ring.put(String.valueOf(realAlpha.toCharArray()[i]), String.valueOf(codes[i]));
        }

        System.out.println(ring);
        StringBuilder ret = new StringBuilder();
        char[] letters = message.toCharArray();
        for (int i=0;i<letters.length;i++) {
            if (letters[i] == ' ') {
                ret.append(' ');
                continue;
            }

            ret.append(ring.get(String.valueOf(letters[i])));
        }

        return ret.toString();
    }

    @PostMapping("/s/{find}/{replacement}")
    public String sed(@RequestBody(required=false) String body,
                      @PathVariable(name="find", required=true) String find,
                      @PathVariable(name="replacement", required=true) String replace)
    {
        if (body == null) return "";
        return body.replaceAll(find, replace);
    }
}
