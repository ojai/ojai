/**
 * Copyright (c) 2015 MapR, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ojai.beans.jackson;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import org.ojai.annotation.API;
import org.ojai.types.Interval;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.util.VersionUtil;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

@API.Internal
public class JacksonHelper {
  public final static Version VERSION;

  public static final ObjectMapper MAPPER = new ObjectMapper();
  static {
    String version = JacksonHelper.class.getPackage().getImplementationVersion();
    String version_str = version == null ? "<unknown>" : version;
    VERSION = VersionUtil.parseVersion(version_str, "org.ojai", "core");

    SimpleModule module = new SimpleModule("OjaiSerializers", VERSION);
    ByteSerializer byteSerializer = new ByteSerializer();
    module.addSerializer(byte.class, byteSerializer);
    module.addSerializer(Byte.class, byteSerializer);

    module.addSerializer(Interval.class, new IntervalSerializer());
    module.addDeserializer(Interval.class, new IntervalDeserializer());

    module.addSerializer(Date.class, new DateSerializer());
    module.addDeserializer(Date.class, new DateDeserializer());

    module.addSerializer(Time.class, new TimeSerializer());
    module.addDeserializer(Time.class, new TimeDeserializer());

    module.addSerializer(Timestamp.class, new TimestampSerializer());
    module.addDeserializer(Timestamp.class, new TimestampDeserializer());

    MAPPER.registerModule(module);
  }

  public static class ByteSerializer extends JsonSerializer<Byte> {
    @Override
    public void serialize(Byte value, JsonGenerator jgen, SerializerProvider provider)
        throws IOException, JsonProcessingException {
      jgen.writeObject(value);
    }
  }

  public static class IntervalSerializer extends JsonSerializer<Interval> {
    @Override
    public void serialize(Interval value, JsonGenerator jgen, SerializerProvider provider)
        throws IOException, JsonProcessingException {
      jgen.writeObject(value);
    }
  }

  public static class IntervalDeserializer extends JsonDeserializer<Interval> {
    @Override
    public Interval deserialize(JsonParser p, DeserializationContext ctxt) throws IOException,
        JsonProcessingException {
      return (Interval) p.getEmbeddedObject();
    }
  }

  public static class DateSerializer extends JsonSerializer<Date> {
    @Override
    public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider)
        throws IOException, JsonProcessingException {
      jgen.writeObject(value);
    }
  }

  public static class DateDeserializer extends JsonDeserializer<Date> {
    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException,
        JsonProcessingException {
      return (Date) p.getEmbeddedObject();
    }
  }

  public static class TimeSerializer extends JsonSerializer<Time> {
    @Override
    public void serialize(Time value, JsonGenerator jgen, SerializerProvider provider)
        throws IOException, JsonProcessingException {
      jgen.writeObject(value);
    }
  }

  public static class TimeDeserializer extends JsonDeserializer<Time> {
    @Override
    public Time deserialize(JsonParser p, DeserializationContext ctxt) throws IOException,
        JsonProcessingException {
      return (Time) p.getEmbeddedObject();
    }
  }

  public static class TimestampSerializer extends JsonSerializer<Timestamp> {
    @Override
    public void serialize(Timestamp value, JsonGenerator jgen, SerializerProvider provider)
        throws IOException, JsonProcessingException {
      jgen.writeObject(value);
    }
  }

  public static class TimestampDeserializer extends JsonDeserializer<Timestamp> {
    @Override
    public Timestamp deserialize(JsonParser p, DeserializationContext ctxt) throws IOException,
        JsonProcessingException {
      return (Timestamp) p.getEmbeddedObject();
    }
  }

}
