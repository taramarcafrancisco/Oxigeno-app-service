package com.oxigeno.portal.filter;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DelegatingServletInputStream extends ServletInputStream {

    private final InputStream inputStream;

    public DelegatingServletInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public int read() throws IOException {
        return inputStream.read();
    }

    @Override
    public boolean isFinished() {
        try {
            return inputStream.available() == 0;
        } catch (IOException e) {
            return true;
        }
    }

    @Override
    public boolean isReady() {
        // Siempre retorna true, ya que el InputStream está listo para leer
        return true;
    }

    @Override
    public void setReadListener(ReadListener readListener) {
        // Este método se utiliza para lectura asincrónica; no es necesario implementarlo aquí
    }
}
