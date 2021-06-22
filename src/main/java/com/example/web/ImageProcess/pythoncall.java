package com.example.web.ImageProcess;

import com.example.web.Controller.javaToPy;

public class pythoncall
{
    public String runpython(int valg, javaToPy coms) throws Exception {
        String ret = "";
        String path = System.getProperty("user.dir");
        System.out.println("    "+path+"\\udklip.jpg");
        for (int i = 0 ; i < 3 ; i++) {
            ret = coms.communicate(valg, path + "\\udklip.jpg");
            if (ret != null) break;
        }
        System.out.println("Ret: "+ret);
        return ret;
    }

}
