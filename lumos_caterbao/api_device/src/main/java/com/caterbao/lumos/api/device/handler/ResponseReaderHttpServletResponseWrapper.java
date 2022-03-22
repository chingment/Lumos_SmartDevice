package com.caterbao.lumos.api.device.handler;


import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ResponseReaderHttpServletResponseWrapper extends HttpServletResponseWrapper {


    private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    /**
     * Constructs a response adaptor wrapping the given response.
     *
     * @param response The response to be wrapped
     * @throws IllegalArgumentException if the response is null
     */
    public ResponseReaderHttpServletResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    public byte[] getBytes() {
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return new ServletOutputStream() {
            @Override
            public void write(int b) throws IOException {
                byteArrayOutputStream.write(b);
                getResponse().getOutputStream().write(b);
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setWriteListener(WriteListener listener) {

            }
        };
    }

}