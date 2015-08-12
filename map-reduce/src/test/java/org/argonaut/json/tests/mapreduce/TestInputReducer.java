/**
 * Copyright (c) 2014 MapR, Inc.
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
package org.argonaut.json.tests.mapreduce;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Reducer;
import org.argonaut.json.impl.JsonDocument;

public class TestInputReducer extends
Reducer<LongWritable, JsonDocument, LongWritable, JsonDocument> {

  protected void reduce(
      LongWritable val,
      JsonDocument value,
      Reducer<LongWritable, JsonDocument, LongWritable, JsonDocument>.Context context)
          throws IOException, InterruptedException {

    context.write(val, value);
  }

}
