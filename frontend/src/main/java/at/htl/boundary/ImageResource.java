package at.htl.boundary;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Path("/uploadImage")
public class ImageResource {
    String DIRECTORY = "./test-screenshots/";

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public void uploadImage(InputStream is) throws IOException {
        File[] files = new File(DIRECTORY).listFiles();

        if(files != null){
            for (File f:
                    files) {
                try(ByteArrayOutputStream bos = new ByteArrayOutputStream()){
                    is.transferTo(new FileOutputStream(f));
                }

                int read = 0;
                byte[] bytes = new byte[1024];

                try(OutputStream os = new FileOutputStream(f)){
                    while((read = is.read(bytes)) != -1){
                        os.write(bytes, 0, read);
                    }
                    os.flush();
                }

                Files.copy(is, Paths.get(f.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }
}
