/*
 * Copyright (c) 2019 - present  Knacky34. All rights reserved.
 * License terms: https://github.com/knacky34/AbsoluteUI/blob/master/LICENSE
 */

package fr.knacky.absoluteui.util;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import org.lwjgl.BufferUtils;

public class Util {
  private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
    ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
    buffer.flip();
    newBuffer.put(buffer);
    return newBuffer;
  }

  public static ByteBuffer ioResourceToByteBuffer(String resource) throws IOException {
    return ioResourceToByteBuffer(resource, 8192);
  }

  public static ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
    ByteBuffer buffer;
    URL url = Thread.currentThread().getContextClassLoader().getResource(resource);
    File file = new File(url.getFile());

    if (file.isFile()) {
      FileInputStream fis = new FileInputStream(file);
      FileChannel fc = fis.getChannel();
      buffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
      fc.close();
      fis.close();
    } else {
      buffer = BufferUtils.createByteBuffer(bufferSize);
      InputStream source = url.openStream();
      if (source == null)
        throw new FileNotFoundException(resource);
      try {
        byte[] buf = new byte[8192];
        while (true) {
          int bytes = source.read(buf, 0, buf.length);
          if (bytes == -1)
            break;
          if (buffer.remaining() < bytes)
            buffer = resizeBuffer(buffer, buffer.capacity() * 2);
          buffer.put(buf, 0, bytes);
        }
        buffer.flip();
      } finally {
        source.close();
      }
    }
    return buffer;
  }
}
