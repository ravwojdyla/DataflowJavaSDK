/*
 * Copyright (C) 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.cloud.dataflow.sdk.runners.inprocess;


import com.google.auto.value.AutoValue;
import com.google.cloud.dataflow.sdk.runners.inprocess.InProcessPipelineRunner.CommittedBundle;
import com.google.cloud.dataflow.sdk.transforms.AppliedPTransform;

import java.util.Set;

import javax.annotation.Nullable;

/**
 * A {@link InProcessTransformResult} that has been committed.
 */
@AutoValue
abstract class CommittedResult {
  /**
   * Returns the {@link AppliedPTransform} that produced this result.
   */
  public abstract AppliedPTransform<?, ?, ?> getTransform();

  /**
   * Returns the {@link CommittedBundle} that contains the input elements that could not be
   * processed by the evaluation.
   *
   * <p>{@code null} if the input bundle was null.
   */
  @Nullable
  public abstract CommittedBundle<?> getUnprocessedInputs();

  /**
   * Returns the outputs produced by the transform.
   */
  public abstract Iterable<? extends CommittedBundle<?>> getOutputs();

  /**
   * Returns if the transform that produced this result produced outputs.
   *
   * <p>Transforms that produce output via modifying the state of the runner (e.g.
   * {@link CreatePCollectionView}) should explicitly set this to true. If {@link #getOutputs()}
   * returns a nonempty iterable, this will also return true.
   */
  public abstract Set<OutputType> getProducedOutputTypes();

  public static CommittedResult create(
      InProcessTransformResult original,
      CommittedBundle<?> unprocessedElements,
      Iterable<? extends CommittedBundle<?>> outputs,
      Set<OutputType> producedOutputs) {
    return new AutoValue_CommittedResult(original.getTransform(),
        unprocessedElements,
        outputs,
        producedOutputs);
  }

  enum OutputType {
    PCOLLECTION_VIEW,
    BUNDLE
  }
}
