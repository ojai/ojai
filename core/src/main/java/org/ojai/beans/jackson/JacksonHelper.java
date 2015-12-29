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

import org.ojai.annotation.API;
import org.ojai.types.ODate;
import org.ojai.types.OInterval;
import org.ojai.types.OTime;
import org.ojai.types.OTimestamp;

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

    module.addSerializer(OInterval.class, new IntervalSerializer());
    module.addDeserializer(OInterval.class, new IntervalDeserializer());

    module.addSerializer(ODate.class, new DateSerializer());
    module.addDeserializer(ODate.class, new DateDeserializer());

    module.addSerializer(OTime.class, new TimeSerializer());
    module.addDeserializer(OTime.class, new TimeDeserializer());

    module.addSerializer(OTimestamp.class, new TimestampSerializer());
    module.addDeserializer(OTimestamp.class, new TimestampDeserializer());

    MAPPER.registerModule(module);
  }

  public static class ByteSerializer extends JsonSerializer<Byte> {
    @Override
    public void serialize(Byte value, JsonGenerator jgen, SerializerProvider provider)
        throws IOException, JsonProcessingException {
      jgen.writeObject(value);
    }
  }

  public static class IntervalSerializer extends JsonSerializer<OInterval> {
    @Override
    public void serialize(OInterval value, JsonGenerator jgen, SerializerProvider provider)
        throws IOException, JsonProcessingException {
      jgen.writeObject(value);
    }
  }

  public static class IntervalDeserializer extends JsonDeserializer<OInterval> {
    @Override
    public OInterval deserialize(JsonParser p, DeserializationContext ctxt) throws IOException,
        JsonProcessingException {
      return (OInterval) p.getEmbeddedObject();
    }
  }

  public static class DateSerializer extends JsonSerializer<ODate> {
    @Override
    public void serialize(ODate value, JsonGenerator jgen, SerializerProvider provider)
        throws IOException, JsonProcessingException {
      jgen.writeObject(value);
    }
  }

  public static class DateDeserializer extends JsonDeserializer<ODate> {
    @Override
    public ODate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException,
        JsonProcessingException {
      return (ODate) p.getEmbeddedObject();
    }
  }

  public static class TimeSerializer extends JsonSerializer<OTime> {
    @Override
    public void serialize(OTime value, JsonGenerator jgen, SerializerProvider provider)
        throws IOException, JsonProcessingException {
      jgen.writeObject(value);
    }
  }

  public static class TimeDeserializer extends JsonDeserializer<OTime> {
    @Override
    public OTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException,
        JsonProcessingException {
      return (OTime) p.getEmbeddedObject();
    }
  }

  public static class TimestampSerializer extends JsonSerializer<OTimestamp> {
    @Override
    public void serialize(OTimestamp value, JsonGenerator jgen, SerializerProvider provider)
        throws IOException, JsonProcessingException {
      jgen.writeObject(value);
    }
  }

  public static class TimestampDeserializer extends JsonDeserializer<OTimestamp> {
    @Override
    public OTimestamp deserialize(JsonParser p, DeserializationContext ctxt) throws IOException,
        JsonProcessingException {
      return (OTimestamp) p.getEmbeddedObject();
    }
  }

}
