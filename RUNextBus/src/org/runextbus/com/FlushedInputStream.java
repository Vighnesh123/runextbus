package org.runextbus.com;


import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FlushedInputStream extends FilterInputStream {

    // Function #01
        public FlushedInputStream(InputStream inputStream){
                super(inputStream);
        }

@Override
    //Function #02
        
public long skip (long n) throws IOException {
        long totalBytesSkipped = 0L;
        while (totalBytesSkipped < n){
                long bytesSkipped = in.skip(n - totalBytesSkipped);
                        if (bytesSkipped == 0L) {
                                int b = read();
                                        if (b < 0) {
                                                break;// we reached EOF
                                    } // end of inner If 
                                                else{
                                                                        bytesSkipped = 1; // we read one byte
                                            } // End of else
                    } // end of outer if
                                        totalBytesSkipped += bytesSkipped;
     } // end of while
                                return totalBytesSkipped;
}// end of function                                                                     
}// class 