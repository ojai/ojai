/**
 * Copyright (c) 2017 MapR, Inc.
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
package org.ojai.tests;

import static org.ojai.DocumentConstants.DOCUMENT_KEY;

import org.junit.Test;
import org.ojai.FieldPath;
import org.ojai.FieldSegment;
import org.ojai.FieldSegment.NameSegment;
import org.ojai.util.FieldProjector;
import org.ojai.util.impl.ProjectionTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestProjectionTree extends BaseTest {
  private static final Logger _logger = LoggerFactory.getLogger(TestProjectionTree.class);

  @Test
  public void testSimpleInclusion() {
    FieldProjector tree = new FieldProjector("a", "a.b", "a.b.c");
    _logger.info(tree.asJsonString());
  }

  private ProjectionTree buildProjectionTree(String[] paths) {
    FieldSegment DOCUMENT_ROOT = new NameSegment(DOCUMENT_KEY, null, false);
    ProjectionTree tree = new ProjectionTree(DOCUMENT_ROOT, null);

    for (String path : paths) {
      tree.addOrGetChild(FieldPath.parseFrom(path).getRootSegment());
    }
    return tree;
  }

  //Visually verify the printed documents
  @Test
  public void testEntireArrayProjection() {
    String[] projFields1 = {"a[].b","a[].c"};
    ProjectionTree tree1 = buildProjectionTree(projFields1);
    _logger.info("========== ProjectionTree for : " + projFields1[0] + ", " + projFields1[1] + "========== ");
    _logger.info(tree1.asJsonString());

    String[] projFields2 = {"a[].b","a[1].c"};
    ProjectionTree tree2 = buildProjectionTree(projFields2);
    _logger.info("========== ProjectionTree for : " + projFields2[0] + ", " + projFields2[1] + "========== ");
    _logger.info(tree2.asJsonString());

    String[] projFields3 = {"a[][].b","a[].c"};
    ProjectionTree tree3 = buildProjectionTree(projFields3);
    _logger.info("========== ProjectionTree for : " + projFields2[0] + ", " + projFields3[1] + "========== ");
    _logger.info(tree3.asJsonString());

    String[] projFields4 = {"a[][].b[]","a[].c[]"};
    ProjectionTree tree4 = buildProjectionTree(projFields4);
    _logger.info("========== ProjectionTree for : " + projFields4[0] + ", " + projFields4[1] + "========== ");
    _logger.info(tree4.asJsonString());

    String[] projFields5 = {"a[][].b[].c","a[].b[][].c[]"};
    ProjectionTree tree5 = buildProjectionTree(projFields5);
    _logger.info("========== ProjectionTree for : " + projFields5[0] + ", " + projFields5[1] + "========== ");
    _logger.info(tree5.asJsonString());
  }

}
