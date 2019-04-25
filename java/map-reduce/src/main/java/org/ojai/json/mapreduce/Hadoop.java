package org.ojai.json.mapreduce;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.ojai.DocumentStream;
import org.ojai.Value.Type;
import org.ojai.FieldPath;
import org.ojai.annotation.API;
import org.ojai.annotation.API.NonNullable;
import org.ojai.annotation.API.Nullable;
import org.ojai.exceptions.DecodingException;
import org.ojai.exceptions.OjaiException;
import org.ojai.json.Events;
import org.ojai.json.impl.JsonDocumentStream;

/**
 * This class serves as a factory for a creating OJAI {@link DocumentStream}
 * from Hadoop FileSystem files.
 */
@API.Public
@API.Factory
public class Hadoop {

  public static DocumentStream newDocumentStream(
      @NonNullable FileSystem fs, @NonNullable String path)
          throws DecodingException, IOException {
    return newDocumentStream(fs, new Path(path), null, null);
  }

  public static DocumentStream newDocumentStream(@NonNullable FileSystem fs, @NonNullable String path,
      @NonNullable Map<FieldPath, Type> fieldPathTypeMap) throws DecodingException, IOException {
    checkNotNull(fieldPathTypeMap);

    return newDocumentStream(fs, new Path(path), fieldPathTypeMap, null);
  }

  public static DocumentStream newDocumentStream(@NonNullable FileSystem fs, @NonNullable String path,
      @NonNullable Events.Delegate eventDelegate) throws DecodingException, IOException {
    checkNotNull(eventDelegate);

    return newDocumentStream(fs, new Path(path), null, eventDelegate);
  }

  public static DocumentStream newDocumentStream(FileSystem fs, String path,
      Map<FieldPath, Type> map, Events.Delegate delegate) throws IllegalArgumentException, IOException {
    checkNotNull(map);
    checkNotNull(delegate);

    return newDocumentStream(fs, new Path(path), map, delegate);
  }

  private static DocumentStream newDocumentStream(@NonNullable FileSystem fs, @NonNullable Path path,
      @Nullable Map<FieldPath, Type> map, @Nullable Events.Delegate delegate)
          throws IllegalArgumentException, IOException {
    checkNotNull(fs);

    final InputStream in = fs.open(path);
    return new JsonDocumentStream(in, map, delegate) {
      @Override
      public void close() {
        try {
          super.close();
        } finally {
          try {
            in.close();
          } catch (IOException e) {
            throw new OjaiException(e);
          }
        }
      }
    };
  }
  
}
