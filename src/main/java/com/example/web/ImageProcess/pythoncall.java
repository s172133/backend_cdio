package com.example.web.ImageProcess;

import com.example.web.Controller.javaToPy;

import static org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS;

public class pythoncall
{
    public String runpython(int valg, javaToPy coms) throws Exception {
        String ret = "";
        String slash;
        if (IS_OS_WINDOWS) {
            // OS DEPENDENT:
            slash = "\\";
        } else {
            slash = "/";
        }
        String path = System.getProperty("user.dir");
        for (int i = 0 ; i < 3 ; i++) {
            ret = coms.communicate(valg, path + slash + "udklip.jpg");
            if (ret != null) break;
            if (i >= 2) throw new Exception("PythonTimeout");
        }
        return ret;
    }

}
