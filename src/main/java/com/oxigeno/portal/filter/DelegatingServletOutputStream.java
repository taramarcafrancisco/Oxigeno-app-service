package com.oxigeno.portal.filter;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.IOException;
import java.io.OutputStream;

public class DelegatingServletOutputStream extends ServletOutputStream {

    private final OutputStream outputStream;

    public DelegatingServletOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public void write(int b) throws IOException {
        outputStream.write(b);
    }

    @Override
    public void flush() throws IOException {
        outputStream.flush();
    }

    @Override
    public void close() throws IOException {
        outputStream.close();
    }

    @Override
    public boolean isReady() {
        // En este caso, siempre retornamos true ya que este stream siempre está listo para escribir
        return true;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {
        // Aquí no hacemos nada porque estamos usando un OutputStream síncrono.
        // Si necesitas un manejo asincrónico, tendrías que implementar esto de manera diferente.
    }
}
