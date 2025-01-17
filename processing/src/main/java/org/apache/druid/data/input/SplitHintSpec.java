/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.druid.data.input;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.apache.druid.data.input.impl.SplittableInputSource;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

/**
 * In native parallel indexing, the supervisor task partitions input data into splits and assigns each of them
 * to a single sub task. How to create splits could mainly depend on the input file format, but sometimes druid users
 * want to give some hints to control the amount of data each sub task will read. SplitHintSpec can be used for this
 * purpose. Implementations can ignore the given hint.
 *
 * @see SplittableInputSource#createSplits
 * @see SplittableInputSource#estimateNumSplits
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(value = {
    @Type(name = SegmentsSplitHintSpec.TYPE, value = SegmentsSplitHintSpec.class),
    @Type(name = MaxSizeSplitHintSpec.TYPE, value = MaxSizeSplitHintSpec.class)
})
public interface SplitHintSpec
{
  /**
   * Returns an iterator of splits. A split has a list of files of the type {@link T}.
   *
   * @param inputIterator           that returns input files.
   * @param inputAttributeExtractor to create {@link InputFileAttribute} for each input file.
   */
  <T> Iterator<List<T>> split(Iterator<T> inputIterator, Function<T, InputFileAttribute> inputAttributeExtractor);
}
